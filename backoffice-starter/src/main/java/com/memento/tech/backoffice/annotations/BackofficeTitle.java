package com.memento.tech.backoffice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
public @interface BackofficeTitle {

    String value();
}