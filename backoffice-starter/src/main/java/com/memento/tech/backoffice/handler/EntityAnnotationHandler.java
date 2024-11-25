package com.memento.tech.backoffice.handler;

import com.memento.tech.backoffice.entity.EntitySettings;

public interface EntityAnnotationHandler {

    void handleEntityAnnotation(EntitySettings entitySettings, Class<?> entityClass);
}