package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.dto.EntityDataDTO;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.dto.NonBasicFieldValueDTO;
import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.entity.Language;
import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.service.DataService;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import com.memento.tech.backoffice.service.LanguageService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Service
@AllArgsConstructor
public class DefaultDataService implements DataService {

    private final EntityService entityService;

    private final EntitySettingsService entitySettingsService;

    private final LanguageService languageService;

    @Override
    public void saveRecord(EntityWrapper<? extends BaseEntity> entityWrapper, boolean isNew) {
        var entitySettings = getEntitySettings(entityWrapper.entityName());

        entityService.saveRecord(entityWrapper.entityData(), entitySettings, isNew);
    }

    @Override
    public Set<EntityDataDTO> getDataForEntity(String entityName, Pagination pagination) {
        var entitySettings = getEntitySettings(entityName);

        return entityService.getAllEntities(entitySettings.getEntityName(), entitySettings.getEntityClass(), pagination)
                .stream()
                .map(data -> getData(entitySettings, data))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public EntityDataDTO getDataForEntityWithId(String entityName, String recordId) {
        var entitySettings = getEntitySettings(entityName);

        return entityService.getEntityForId(entitySettings.getEntityName(), recordId, entitySettings.getEntityClass())
                .map(data -> getData(entitySettings, data))
                .orElseThrow();
    }

    @Override
    public List<EntityDataDTO> getDataForEntitiesWithIds(String entityName, List<String> recordIds) {
        var entitySettings = getEntitySettings(entityName);

        return entityService.getEntitiesForIds(entitySettings.getEntityName(), recordIds, entitySettings.getEntityClass())
                .stream()
                .map(data -> getData(entitySettings, data))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void removeRecordWithId(String entityName, String recordId) {
        var entitySettings = getEntitySettings(entityName);

        entityService.removeRecord(entitySettings.getEntityName(), recordId, entitySettings.getEntityClass());
    }

    private EntitySettings getEntitySettings(String entityName) {
        return entitySettingsService.getEntitySettingsForName(entityName)
                .orElseThrow();
    }

    private EntityDataDTO getData(EntitySettings entitySettings, Object data) {
        requireNonNull(data);

        var fieldsToPopulate = emptyIfNull(entitySettings.getFieldSettings())
                .stream()
                .filter(entityFieldSettings -> !entityFieldSettings.isExcludeField())
                .toList();

        var dataMap = new HashMap<>(collectFieldValues(entitySettings.getEntityClass(), getFieldsNamesAndTypesMap(fieldsToPopulate), data));

        return EntityDataDTO
                .builder()
                .data(dataMap)
                .translationData(entitySettings.getEntityClass().equals(Translation.class) ? data : null)
                .entityName(entitySettings.getEntityName())
                .build();
    }

    private Map<String, EntityFieldSettings> getFieldsNamesAndTypesMap(List<EntityFieldSettings> fieldSettings) {
        return emptyIfNull(fieldSettings)
                .stream()
                .collect(Collectors.toMap(EntityFieldSettings::getField, entityFieldSettings -> entityFieldSettings));
    }

    private Map<String, Object> collectFieldValues(Class<?> entityClass, Map<String, EntityFieldSettings> fieldsNamesTypesMap, Object data) {
        var dataMap = new HashMap<String, Object>();

        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> fieldsNamesTypesMap.containsKey(field.getName()))
                .forEach(field -> {
                    field.setAccessible(true);

                    var fieldSettings = fieldsNamesTypesMap.remove(field.getName());

                    Object value = "";
                    if (!field.getName().equals("password")) {
                        try {
                            var fieldValue = field.get(data);
                            if (fieldSettings.isBasic()) {
                                value = basicFieldValueToString(fieldValue);
                            } else {
                                value = nonBasicFieldValueToString(fieldValue, fieldSettings);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    dataMap.put(field.getName(), value);
                });

        var superclass = entityClass.getSuperclass();
        if (Objects.nonNull(superclass)) {
            dataMap.putAll(collectFieldValues(superclass, fieldsNamesTypesMap, data));
        }

        return dataMap;
    }

    private Object nonBasicFieldValueToString(Object value, EntityFieldSettings fieldSettings) {
        Object result;
        var fieldClass = fieldSettings.getFieldClass();

        if (value instanceof Collection<?> collection) {
            var entityName = fieldClass.getSimpleName();

            List<String> valueIds = List.of();

            if (!collection.isEmpty()) {
                valueIds = collection
                        .stream()
                        .map(colValue -> getIdentifierValue(colValue, fieldClass))
                        .toList();
            }

            result = NonBasicFieldValueDTO.builder()
                    .entityName(entityName)
                    .simpleName(valueIds.isEmpty() ? "" : entityName + "(..." + collection.size() + ")")
                    .valueIds(valueIds)
                    .multivalue(true)
                    .build();
        } else {
            var entityName = fieldClass.getSimpleName();
            var idValue = getIdentifierValue(value, fieldClass);

            var simpleName = idValue.equals(StringUtils.EMPTY) ? "" : entityName + "(" + idValue + ")";

            if (value instanceof Translation translation) {
                var suffixSimpleName = CollectionUtils.emptyIfNull(translation.getTranslationWrappers())
                        .stream()
                        .filter(wrapper -> wrapper.getLanguage().getLangIsoCode().equals(languageService.getDefaultLanguageIsoCode()))
                        .findFirst()
                        .map(_translation -> "('" + _translation.getTranslation() + "')")
                        .orElse(StringUtils.EMPTY);

                simpleName = translation.getCode() + suffixSimpleName;
            }

            result = NonBasicFieldValueDTO.builder()
                    .entityName(entityName)
                    .simpleName(simpleName)
                    .valueIds(idValue.equals(StringUtils.EMPTY) ? List.of() : List.of(idValue))
                    .multivalue(false)
                    .translationData(fieldClass.equals(Translation.class) ? value : null)
                    .build();
        }

        return result;
    }

    private String basicFieldValueToString(Object value) {
        if (value instanceof Class<?> clazz) {
            return clazz.getName();
        } else if (value instanceof String stringValue) {
            return stringValue;
        } else if (value instanceof LocalDateTime dateTime) {
            return dateTime.toString();
        } else if (value instanceof Collection<?> collectionValue) {
            if (collectionValue.isEmpty()) {
                return StringUtils.EMPTY;
            } else {
                return collectionValue
                        .stream()
                        .map(objectValue -> {
                            try {
                                return (String) objectValue.getClass().getMethod("toString", (Class<?>[]) null).invoke(objectValue, (Object[]) null);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.joining(","));
            }
        }

        return Objects.isNull(value)
                ? StringUtils.EMPTY
                : value.toString();
    }

    private String getIdentifierValue(Object value, Class<?> valueClass) {
        var result = StringUtils.EMPTY;

        if (Objects.nonNull(value) && Objects.nonNull(valueClass)) {
            try {
                var idField = valueClass.getDeclaredField("id");
                idField.setAccessible(true);
                result = idField.get(value).toString();
            } catch (NoSuchFieldException e) {
                result = getIdentifierValue(value, valueClass.getSuperclass());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}