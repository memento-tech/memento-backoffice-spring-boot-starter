package com.memento.tech.backoffice.util;

import com.memento.tech.backoffice.entity.EntityFieldSettings;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class ObjectsUtil {

    public static Field getField(Class<?> clazz, String fieldName) {
        var result = Arrays.stream(Optional.ofNullable(clazz)
                        .map(Class::getDeclaredFields)
                        .orElse(new Field[]{}))
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(result) && Objects.nonNull(clazz) && !StringUtils.equals(clazz.getSimpleName(), Object.class.getSimpleName())) {
            return getField(clazz.getSuperclass(), fieldName);
        }

        return result;
    }

    public static Object getFieldValue(Object entity, EntityFieldSettings fieldSettings) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(fieldSettings);

        var field = getField(entity.getClass(), fieldSettings.getField());

        Objects.requireNonNull(field);

        field.setAccessible(true);
        Object fieldValue = null;

        try {
            fieldValue = field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
            //it should never come to this since field is set to accessible
        }

        return fieldValue;
    }

    public static void setFieldValue(Object entity, Object fieldValue, EntityFieldSettings fieldSettings) {
        Objects.requireNonNull(entity);
        Objects.requireNonNull(fieldSettings);

        var field = getField(entity.getClass(), fieldSettings.getField());

        Objects.requireNonNull(field);

        field.setAccessible(true);

        try {
            field.set(entity, fieldValue);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
            //it should never come to this since field is set to accessible
        }
    }
}