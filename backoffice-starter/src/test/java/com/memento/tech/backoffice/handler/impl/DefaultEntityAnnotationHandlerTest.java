package com.memento.tech.backoffice.handler.impl;


import com.memento.tech.backoffice.annotations.BackofficeDisableCreation;
import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import com.memento.tech.backoffice.annotations.RecordFunctionButton;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.repository.WidgetRepository;
import com.memento.tech.backoffice.widget.RecordFunctionButtonHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.memento.tech.backoffice.widget.WidgetMappingConstants.RECORD_FUNCTION_API_SUFFIX;
import static com.memento.tech.backoffice.widget.WidgetMappingConstants.WIDGET_API_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultEntityAnnotationHandlerTest {

    private static final String BASE_ENTITY_ID = "id";

    @InjectMocks
    private DefaultEntityAnnotationHandler defaultEntityAnnotationHandler;

    @Mock
    private WidgetRepository widgetRepository;

    static class NonAnnotatedClass {

    }

    @Test
    void testHandleEntityAnnotation_handleEntityExcludeAnnotated() {
        @BackofficeExclude
        class HandleEntityExcludeTestClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, HandleEntityExcludeTestClass.class);

        assertTrue(entitySettings.isExcludeEntity());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityExcludeNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertFalse(entitySettings.isExcludeEntity());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityCreationDisableAnnotated() {
        @BackofficeDisableCreation
        class CreationDisabledClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, CreationDisabledClass.class);

        assertTrue(entitySettings.isDisableCreation());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityCreationDisableNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertFalse(entitySettings.isDisableCreation());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityOrderAnnotated() {
        @BackofficeOrderPriority(1000)
        class OrderedClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, OrderedClass.class);

        assertEquals(1000, entitySettings.getEntityOrder());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityOrderNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertEquals(0, entitySettings.getEntityOrder());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityTitleAnnotated() {
        @BackofficeTitle("Test Title")
        class OrderedClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, OrderedClass.class);

        assertEquals("Test Title", entitySettings.getTitle());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityTitleNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertEquals(NonAnnotatedClass.class.getSimpleName(), entitySettings.getTitle());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityGroupAnnotated() {
        @BackofficeGroup(title = "testGroup")
        class OrderedClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, OrderedClass.class);

        assertEquals("testGroup", entitySettings.getEntityGroup());
    }

    @Test
    void testHandleEntityAnnotation_handleEntityGroupNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertNull(entitySettings.getEntityGroup());
    }


    @Test
    void testHandleEntityAnnotation_handleFieldForShowInListAnnotated() {
        @BackofficeFieldForShowInList("testField")
        class OrderedClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, OrderedClass.class);

        assertEquals("testField", entitySettings.getFieldForShowInList());
    }

    @Test
    void testHandleEntityAnnotation_handleFieldForShowInListNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        assertEquals(BASE_ENTITY_ID, entitySettings.getFieldForShowInList());
    }

    @Test
    void testHandleEntityAnnotation_handleWidgetAnnotationsAnnotated() {
        when(widgetRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        class FunctionBeanClass implements RecordFunctionButtonHandler<BaseEntity> {

            @Override
            public void handle(BaseEntity entity) {

            }

            @Override
            public boolean show(BaseEntity entity) {
                return false;
            }
        }

        @RecordFunctionButton(widgetId = "testWidget", buttonLabel = "testLabel", functionBeanClass = FunctionBeanClass.class)
        class OrderedClass {

        }

        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, OrderedClass.class);
        verify(widgetRepository, times(1)).save(any());
        assertNotNull(entitySettings.getEntityWidgets());
        assertEquals(1, entitySettings.getEntityWidgets().size());
        assertNotNull(entitySettings.getEntityWidgets().get(0));

        var savedWidget = entitySettings.getEntityWidgets().get(0);

        assertEquals("testWidget", savedWidget.getWidgetId());
        assertEquals("testLabel", savedWidget.getLabel());
        assertEquals(FunctionBeanClass.class, savedWidget.getWidgetHandlerClass());
        assertEquals(WIDGET_API_PREFIX + RECORD_FUNCTION_API_SUFFIX, savedWidget.getHandlerMapping());
        assertFalse(savedWidget.isEntityLevel());
        assertTrue(savedWidget.isRecordLevel());
    }

    @Test
    void testHandleEntityAnnotation_handleWidgetAnnotationsNotAnnotated() {
        var entitySettings = new EntitySettings();

        defaultEntityAnnotationHandler.handleEntityAnnotation(entitySettings, NonAnnotatedClass.class);

        verify(widgetRepository, times(0)).save(any());
        assertNotNull(entitySettings.getEntityWidgets());
        assertTrue(entitySettings.getEntityWidgets().isEmpty());
    }
}