package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.annotations.BackofficePasswordFlag;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.handler.FieldAnnotationHandler;
import com.memento.tech.backoffice.repository.EntityFieldSettingsRepository;
import com.memento.tech.backoffice.service.FieldSettingsService;
import com.memento.tech.backoffice.service.LanguageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinTable;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.metamodel.MappingMetamodel;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.metamodel.mapping.internal.BasicAttributeMapping;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static org.apache.commons.collections4.SetUtils.emptyIfNull;

@Service
@RequiredArgsConstructor
public class DefaultFieldSettingsService implements FieldSettingsService {

    private final EntityManager entityManager;

    private final EntityFieldSettingsRepository entityFieldSettingsRepository;

    private final FieldAnnotationHandler fieldAnnotationHandler;

    private final LanguageService languageService;

    private static Type getNonBasicFieldType(EntityType<?> entityType, String fieldName) {
        Type actualType;
        try {
            actualType = ((ParameterizedType) entityType.getJavaType().getDeclaredField(fieldName).getGenericType()).getActualTypeArguments()[0];
        } catch (NoSuchFieldException e) {
            throw new BackofficeException(
                    "DefaultFieldSettingsService::populateIdentifierFieldMetadata : There is no field [" + fieldName + "] in entity type " + entityType.getName() + "].",
                    INTERNAL_BACKOFFICE_ERROR);
        }
        return actualType;
    }

    @Override
    public Set<EntityFieldSettings> createEntityFields(EntityType<?> entityType) {
        final var fieldSettings = new HashSet<EntityFieldSettings>();

        final var entityDescriptor = ((MappingMetamodel) entityManager.getEntityManagerFactory().getMetamodel())
                .getEntityDescriptor(entityType.getJavaType());

        var identifierFieldMetadata = populateIdentifierFieldMetadata(entityDescriptor, entityType);

        entityDescriptor.getAttributeMappings()
                .forEach(attributeMapping -> {
                    if (!identifierFieldMetadata.getField().equals(attributeMapping.getPartName())) {
                        fieldSettings.add(populateFieldSettings(attributeMapping, entityType));
                    }
                });

        fieldSettings.add(identifierFieldMetadata);

        var orderedFieldSettings = orderFields(fieldSettings);

        entityFieldSettingsRepository.saveAll(orderedFieldSettings);

        return orderedFieldSettings;
    }

    @Override
    public Set<EntityFieldSettings> orderFields(Set<EntityFieldSettings> unorderedFieldSettings) {
        return emptyIfNull(unorderedFieldSettings)
                .stream()
                .sorted()
                .sorted((first, second) -> Integer.compare(second.getFieldOrder(), first.getFieldOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private EntityFieldSettings populateIdentifierFieldMetadata(EntityPersister entityDescriptor, EntityType<?> entityType) {
        final var entityClassSimpleName = entityType.getJavaType().getSimpleName();

        final var fieldMapping = entityDescriptor.getIdentifierMapping();
        final var fieldClass = fieldMapping.getJavaType().getJavaTypeClass();
        final var fieldName = entityDescriptor.getIdentifierPropertyName();

        final Field field;
        try {
            field = getEntityField(entityType, fieldName);
        } catch (NoSuchFieldException e) {
            throw new BackofficeException(
                    "DefaultFieldSettingsService::populateIdentifierFieldMetadata : There is no field [" + fieldName + "] in entity type " + entityType.getName() + "].",
                    INTERNAL_BACKOFFICE_ERROR);
        }

        final EntityFieldSettings idFieldSettings = EntityFieldSettings.builder()
                .fieldId(entityClassSimpleName + "_" + fieldName)
                .entityName(fieldClass.getSimpleName())
                .field(fieldName)
                .uniqueField(true)
                .required(false)
                .optional(false)
                .changeOptionalAllowed(false)
                .updatable(false)
                .changeUpdatableAllowed(false)
                .basic(true)
                .fieldClass(fieldClass)
                .build();

        fieldAnnotationHandler.handleFieldAnnotation(idFieldSettings, field);

        return idFieldSettings;
    }

    private EntityFieldSettings populateFieldSettings(AttributeMapping attributeMapping, EntityType<?> entityType) {
        final var entityClassSimpleName = entityType.getJavaType().getSimpleName();

        final var fieldName = attributeMapping.getPartName();

        final Field field;
        try {
            field = getEntityField(entityType, fieldName);
        } catch (NoSuchFieldException e) {
            throw new BackofficeException(
                    "DefaultFieldSettingsService::populateIdentifierFieldMetadata : There is no field [" + fieldName + "] in entity type " + entityType.getName() + "].",
                    INTERNAL_BACKOFFICE_ERROR);
        }

        final var fieldMetadata = attributeMapping.getAttributeMetadata();
        final var fieldClass = attributeMapping.getJavaType().getJavaTypeClass();
        final var isRequired = BooleanUtils.isFalse(fieldMetadata.isNullable()) && !field.getType().isPrimitive();
        final var isPasswordField = Objects.nonNull(field.getAnnotation(BackofficePasswordFlag.class));
        final var isEnumeratedField = Objects.nonNull(field.getAnnotation(Enumerated.class));

        EntityFieldSettings fieldSettings = EntityFieldSettings.builder()
                .entityName(fieldClass.getSimpleName())
                .fieldId(entityClassSimpleName + "_" + fieldName)
                .field(fieldName)
                .required(isRequired)
                .isPassword(isPasswordField)
                .optional(fieldMetadata.isNullable())
                .changeOptionalAllowed(fieldMetadata.isNullable())
                .updatable(fieldMetadata.isUpdatable())
                .changeUpdatableAllowed(fieldMetadata.isUpdatable())
                .fieldClass(fieldClass)
                .multivalue(false)
                .isEnumerated(isEnumeratedField)
                .basic(!isEnumeratedField && attributeMapping instanceof BasicAttributeMapping)
                .build();

        fieldAnnotationHandler.handleFieldAnnotation(fieldSettings, field);

        if ((!fieldSettings.isBasic() || isEnumeratedField) && Collection.class.isAssignableFrom(fieldClass)) {
            var actualType = getNonBasicFieldType(entityType, fieldName);

            fieldSettings.setFieldClass((Class<?>) actualType);
            fieldSettings.setEntityName(((Class<?>) actualType).getSimpleName());
            fieldSettings.setMultivalue(true);
            fieldSettings.setCollectionClass(fieldClass);
            fieldSettings.setUpdatable(true);

            //assure that collection is required or not
            var joinColumns = Optional.ofNullable(field.getAnnotation(JoinTable.class))
                    .map(JoinTable::joinColumns)
                    .orElse(null);
            if (Objects.nonNull(joinColumns) && joinColumns.length > 0) {
                fieldSettings.setRequired(!joinColumns[0].nullable());
            }
        }

        return fieldSettings;
    }

    private Field getEntityField(EntityType<?> entityType, String fieldName) throws NoSuchFieldException {
        Field result;
        try {
            result = entityType.getJavaType().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            result = entityType.getSupertype().getJavaType().getDeclaredField(fieldName);
        }

        return result;
    }
}