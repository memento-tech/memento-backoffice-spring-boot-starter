package com.memento.tech.backoffice.handler;

import com.memento.tech.backoffice.entity.EntityFieldSettings;

import java.lang.reflect.Field;

public interface FieldAnnotationHandler {

    void handleFieldAnnotation(EntityFieldSettings fieldSettings, Field field);
}