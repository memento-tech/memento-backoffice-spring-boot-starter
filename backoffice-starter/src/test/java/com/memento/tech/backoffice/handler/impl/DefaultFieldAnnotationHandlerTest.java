package com.memento.tech.backoffice.handler.impl;

import com.memento.tech.backoffice.annotations.BackofficeCreationFieldExclude;
import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeForbidUpdate;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import jakarta.persistence.Column;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DefaultFieldAnnotationHandlerTest {

    @InjectMocks
    private DefaultFieldAnnotationHandler defaultFieldAnnotationHandler;

    static class NonAnnotatedClass {

        private String nonAnnotatedField;
    }

    private static Field nonAnnotatedField;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException {
        nonAnnotatedField = NonAnnotatedClass.class.getDeclaredField("nonAnnotatedField");
    }

    @Test
    void testHandleEntityAnnotation_handleFieldAnnotationAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @BackofficeExclude
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertTrue(fieldSettings.isExcludeField());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldAnnotationNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertFalse(fieldSettings.isExcludeField());
    }

    @Test
    void testHandleEntityAnnotation_handleCreationFieldExcludeAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @BackofficeCreationFieldExclude
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertTrue(fieldSettings.isCreationExcludeField());
    }

    @Test
    void testHandleEntityAnnotation_handleCreationFieldExcludeNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertFalse(fieldSettings.isCreationExcludeField());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldOrderAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @BackofficeOrderPriority(1000)
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertEquals(1000, fieldSettings.getFieldOrder());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldOrderNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertEquals(0, fieldSettings.getFieldOrder());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldTitleAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @BackofficeTitle("Test Field Name")
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertEquals("Test Field Name", fieldSettings.getName());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldTitleNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertEquals(nonAnnotatedField.getName(), fieldSettings.getName());
    }

    @Test
    void testHandleEntityAnnotation_handleUniqueFieldAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @Column(unique = true)
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertTrue(fieldSettings.isUniqueField());
    }

    @Test
    void testHandleEntityAnnotation_handleUniqueFieldNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertFalse(fieldSettings.isUniqueField());
    }

    @Test
    void testHandleEntityAnnotation_handleForbidUpdateAnnotated() throws NoSuchFieldException {
        class HandleEntityExcludeTestClass {

            @BackofficeForbidUpdate
            private String annotatedField;
        }

        var fieldToCheck = HandleEntityExcludeTestClass.class.getDeclaredField("annotatedField");

        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, fieldToCheck);

        assertFalse(fieldSettings.isUpdatable());
    }

    @Test
    void testHandleEntityAnnotation_handleForbidUpdateNotAnnotated() {
        var fieldSettings = new EntityFieldSettings();

        defaultFieldAnnotationHandler.handleFieldAnnotation(fieldSettings, nonAnnotatedField);

        assertTrue(fieldSettings.isUpdatable());
    }
}