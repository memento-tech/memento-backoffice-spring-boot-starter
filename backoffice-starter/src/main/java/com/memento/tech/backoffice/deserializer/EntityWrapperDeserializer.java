package com.memento.tech.backoffice.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.dto.NonBasicFieldValueDTO;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.UN_SUFFICIENT_DATA_ERROR;
import static com.memento.tech.backoffice.util.ObjectsUtil.getField;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
public final class EntityWrapperDeserializer extends JsonDeserializer<EntityWrapper<?>> {

    private final EntitySettingsService entitySettingsService;

    private final EntityService entityService;

    private final PasswordEncoder backofficePasswordEncoder;

    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public EntityWrapper<?> deserialize(JsonParser jsonParser, DeserializationContext context) {
        var node = jsonParser.getCodec().readTree(jsonParser);

        TreeNode entityNameNode = node.get("entityName");
        TreeNode entityDataNode = node.get("entityData");
        TreeNode isCreationNode = node.get("isCreation");

        var isCreation = Optional.ofNullable(isCreationNode)
                .filter(BooleanNode.class::isInstance)
                .map(BooleanNode.class::cast)
                .map(BooleanNode::booleanValue)
                .orElse(false);

        if (entityNameNode instanceof NullNode || entityDataNode instanceof NullNode) {
            throw new BackofficeException("EntityWrapperDeserializer::deserialize : Entity name and/or entity data can not be null!", UN_SUFFICIENT_DATA_ERROR);
        }

        var entityName = ((TextNode) entityNameNode).asText();
        var entityDataJson = (ObjectNode) entityDataNode;

        var entitySettings = getEntitySettings(entityName);

        var entity = getEntityInstance(entitySettings.getEntityClass());

        var fields = isCreation
                ? entitySettings.getCreationSettings().getCreationFields()
                : entitySettings.getFieldSettings();

        entityDataJson.fields().forEachRemaining(entry -> {
            var propertyName = entry.getKey();

            var fieldSettings = fields
                    .stream()
                    .filter(settings -> settings.getField().equals(propertyName))
                    .findFirst()
                    .orElseThrow(() -> new BackofficeException("EntityWrapperDeserializer::deserialize : Field settings not found for property - [" + propertyName + "].", INTERNAL_BACKOFFICE_ERROR));

            Object propertyValue;

            if (fieldSettings.isBasic()) {
                try {
                    if (fieldSettings.getFieldClass().getName().startsWith("[")) {
                        propertyValue = getObjectMapper().readValue(entry.getValue().toString(), String.class);
                    } else {
                        propertyValue = getObjectMapper().readValue(entry.getValue().toString(), fieldSettings.getFieldClass());
                    }
                } catch (JsonProcessingException e) {
                    throw new BackofficeException("EntityWrapperDeserializer::deserialize : Basic field could not be deserialized.", INTERNAL_BACKOFFICE_ERROR);
                }

                if (fieldSettings.isPassword() && StringUtils.isNoneBlank((String) propertyValue)) {
                    propertyValue = backofficePasswordEncoder.encode((String) propertyValue);
                }
            } else {
                try {
                    var nonBasicPropertyDTO = getObjectMapper().readValue(entry.getValue().toString(), NonBasicFieldValueDTO.class);

                    if (fieldSettings.isMultivalue()) {
                        propertyValue = getNonBasicPropertyValues(nonBasicPropertyDTO, fieldSettings.getCollectionClass());
                    } else {
                        propertyValue = getNonBasicPropertyValue(nonBasicPropertyDTO);
                    }
                } catch (JsonProcessingException e) {
                    throw new BackofficeException("EntityWrapperDeserializer::deserialize : Non basic property could not be deserialized.", INTERNAL_BACKOFFICE_ERROR);
                }
            }

            if (Objects.nonNull(propertyValue)) {
                try {
                    populatePropertyValue(entity, propertyValue, fieldSettings);
                } catch (IllegalAccessException e) {
                    throw new BackofficeException("EntityWrapperDeserializer::deserialize : Property [" + propertyName + "] could not be populated.", INTERNAL_BACKOFFICE_ERROR);
                }
            }
        });

        return EntityWrapper
                .builder()
                .entityData((BaseEntity) entity)
                .entityName(entityName)
                .build();
    }

    private Object getNonBasicPropertyValue(NonBasicFieldValueDTO nonBasicFieldValue) {
        if (Objects.nonNull(nonBasicFieldValue)) {
            if (emptyIfNull(nonBasicFieldValue.valueIds()).size() > 1) {
                throw new BackofficeException("EntityWrapperDeserializer::getNonBasicPropertyValue : Non collection non-basic property can not have more then one id.", INTERNAL_BACKOFFICE_ERROR);
            }

            if (isNotEmpty(nonBasicFieldValue.valueIds())) {
                var nonBasicEntitySettings = getEntitySettings(nonBasicFieldValue.entityName());

                return entityService.getEntityForId(nonBasicEntitySettings.getEntityName(), nonBasicFieldValue.valueIds().get(0), nonBasicEntitySettings.getEntityClass())
                        .orElseThrow(() -> new BackofficeException("EntityWrapperDeserializer::getNonBasicPropertyValue : Non basic entity with id - [" + nonBasicFieldValue.valueIds().get(0) + "] not found.", INTERNAL_BACKOFFICE_ERROR));
            }
        }

        return null;
    }

    private Collection<Object> getNonBasicPropertyValues(NonBasicFieldValueDTO nonBasicFieldValue, Class<?> collectionClass) {
        var nonBasicEntitySettings = getEntitySettings(nonBasicFieldValue.entityName());

        var resultStream = emptyIfNull(nonBasicFieldValue.valueIds())
                .stream()
                .map(recordId ->
                        entityService
                                .getEntityForId(nonBasicEntitySettings.getEntityName(), recordId, nonBasicEntitySettings.getEntityClass())
                                .orElseThrow(() -> new BackofficeException("EntityWrapperDeserializer::getNonBasicPropertyValues : Entity not found for entity for id - [" + recordId + "].", INTERNAL_BACKOFFICE_ERROR))
                );

        if (collectionClass.isAssignableFrom(List.class)) {
            return resultStream.collect(Collectors.toList());
        } else if (collectionClass.isAssignableFrom(Set.class)) {
            return resultStream.collect(Collectors.toSet());
        }

        return null;
    }

    private EntitySettings getEntitySettings(String entityName) {
        return entitySettingsService.getEntitySettingsForName(entityName)
                .orElseThrow(() -> new BackofficeException("EntityWrapperDeserializer::getEntitySettings : Entity settings not found for entity with name - [" + entityName + "].", INTERNAL_BACKOFFICE_ERROR));

    }

    private void populatePropertyValue(Object entityInstance, Object propertyValue, EntityFieldSettings entityFieldSettings) throws IllegalAccessException {
        var propertyField = getField(entityInstance.getClass(), entityFieldSettings.getField());

        if (Objects.isNull(propertyField)) {
            log.info("Property with name [{}] not found in entity [{}]", entityFieldSettings.getField(), entityInstance.getClass().getSimpleName());
        } else {
            propertyField.setAccessible(true);
            if (propertyField.getType().getSimpleName().equals("byte[]") && propertyValue instanceof String) {
                propertyField.set(entityInstance, propertyValue.toString().getBytes());
            } else if (propertyField.getType().getSimpleName().equals("byte") && propertyValue instanceof String) {
                if (propertyValue.toString().length() > 1 || propertyValue.toString().isEmpty()) {
                    throw new BackofficeException("", "");
                }

                propertyField.set(entityInstance, propertyValue.toString().getBytes()[0]);
            } else {
                propertyField.set(entityInstance, propertyValue);
            }
        }
    }

    private Object getEntityInstance(Class<?> classToInstantiate) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return classToInstantiate.getConstructor().newInstance();
    }

    private ObjectMapper getObjectMapper() {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

            objectMapper.registerModule(new JavaTimeModule());
        }

        return objectMapper;
    }
}