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

        var identifierFieldMetadata = populateIdentifierFieldMetadata(new EntityFieldSettings(), entityDescriptor, entityType);

        entityDescriptor.getAttributeMappings()
                .forEach(attributeMapping -> {
                    if (!identifierFieldMetadata.getField().equals(attributeMapping.getPartName())) {
                        fieldSettings.add(populateFieldSettings(new EntityFieldSettings(), attributeMapping, entityType));
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

    private EntityFieldSettings populateIdentifierFieldMetadata(EntityFieldSettings idFieldSettings, EntityPersister entityDescriptor, EntityType<?> entityType) {
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

        idFieldSettings.setFieldId(entityClassSimpleName + "_" + fieldName);
        idFieldSettings.setField(fieldName);
        idFieldSettings.setUniqueField(true);
        idFieldSettings.setRequired(false);
        idFieldSettings.setOptional(false);
        idFieldSettings.setChangeOptionalAllowed(false);
        idFieldSettings.setUpdatable(false);
        idFieldSettings.setChangeUpdatableAllowed(false);
        idFieldSettings.setBasic(true);
        idFieldSettings.setFieldClass(fieldClass);

        fieldAnnotationHandler.handleFieldAnnotation(idFieldSettings, field);

        return idFieldSettings;
    }

    private EntityFieldSettings populateFieldSettings(EntityFieldSettings fieldSettings, AttributeMapping attributeMapping, EntityType<?> entityType) {
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

        fieldSettings.setEntityName(fieldClass.getSimpleName());
        fieldSettings.setFieldId(entityClassSimpleName + "_" + fieldName);
        fieldSettings.setField(fieldName);

        fieldSettings.setRequired(isRequired);
        fieldSettings.setPassword(isPasswordField);
        fieldSettings.setOptional(fieldMetadata.isNullable());
        fieldSettings.setChangeOptionalAllowed(fieldMetadata.isNullable());

        fieldSettings.setUpdatable(fieldMetadata.isUpdatable());
        fieldSettings.setChangeUpdatableAllowed(fieldMetadata.isUpdatable());

        if (isEnumeratedField) {
            fieldSettings.setBasic(false);
        } else {
            fieldSettings.setBasic(attributeMapping instanceof BasicAttributeMapping);
        }

        fieldSettings.setFieldClass(fieldClass);
        fieldSettings.setMultivalue(false);

        fieldSettings.setEnumerated(isEnumeratedField);

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