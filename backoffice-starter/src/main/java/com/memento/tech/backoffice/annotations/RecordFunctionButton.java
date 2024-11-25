package com.memento.tech.backoffice.annotations;

import com.memento.tech.backoffice.widget.RecordFunctionButtonHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Repeatable(RecordFunctionButtons.class)
@WidgetMarker(entityLevel = false, recordLevel = true)
public @interface RecordFunctionButton {

    String widgetId();

    String buttonLabel();

    @SuppressWarnings("rawtypes")
    Class<? extends RecordFunctionButtonHandler> functionBeanClass();

}
