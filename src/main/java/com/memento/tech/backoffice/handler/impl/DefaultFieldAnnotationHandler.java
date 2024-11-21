package com.memento.tech.backoffice.handler.impl;

import com.memento.tech.backoffice.annotations.BackofficeCreationFieldExclude;
import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.handler.FieldAnnotationHandler;
import jakarta.persistence.Column;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class DefaultFieldAnnotationHandler implements FieldAnnotationHandler {

    @Override
    public void handleFieldAnnotation(EntityFieldSettings fieldSettings, Field field) {
        handleFieldExclude(fieldSettings, field);
        handleCreationFieldExclude(fieldSettings, field);
        handleFieldOrder(fieldSettings, field);
        handleFieldTitle(fieldSettings, field);
        handleUniqueField(fieldSettings, field);
        handleForbidUpdate(fieldSettings, field);
    }

    private void handleFieldExclude(EntityFieldSettings fieldSettings, Field field) {
        fieldSettings.setExcludeField(field.isAnnotationPresent(BackofficeExclude.class));
    }

    private void handleCreationFieldExclude(EntityFieldSettings fieldSettings, Field field) {
        fieldSettings.setCreationExcludeField(field.isAnnotationPresent(BackofficeCreationFieldExclude.class));
    }

    private void handleFieldOrder(EntityFieldSettings fieldSettings, Field field) {
        var priority = Optional.ofNullable(field.getAnnotation(BackofficeOrderPriority.class))
                .map(BackofficeOrderPriority::value)
                .orElse(0);

        fieldSettings.setFieldOrder(priority);
    }

    private void handleFieldTitle(EntityFieldSettings fieldSettings, Field field) {
        var title = Optional.ofNullable(field.getAnnotation(BackofficeTitle.class))
                .map(BackofficeTitle::value)
                .orElse(field.getName());

        fieldSettings.setName(title);
    }

    private void handleUniqueField(EntityFieldSettings fieldSettings, Field field) {
        var isUnique = Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::unique)
                .orElse(false);

        fieldSettings.setUniqueField(isUnique);
    }

    private void handleForbidUpdate(EntityFieldSettings fieldSettings, Field field) {
        Optional.ofNullable(field.getAnnotation(BackofficeForbidUpdate.class))
                .ifPresentOrElse(
                        forbidUpdate -> fieldSettings.setUpdatable(false),
                        () -> fieldSettings.setUpdatable(true));
    }
}