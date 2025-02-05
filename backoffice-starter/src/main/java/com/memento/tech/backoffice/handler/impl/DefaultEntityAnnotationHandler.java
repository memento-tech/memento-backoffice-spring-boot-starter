package com.memento.tech.backoffice.handler.impl;

import com.memento.tech.backoffice.annotations.BackofficeDisableCreation;
import com.memento.tech.backoffice.annotations.BackofficeExclude;
import com.memento.tech.backoffice.annotations.BackofficeFieldForShowInList;
import com.memento.tech.backoffice.annotations.BackofficeGroup;
import com.memento.tech.backoffice.annotations.BackofficeOrderPriority;
import com.memento.tech.backoffice.annotations.BackofficeTitle;
import com.memento.tech.backoffice.annotations.RecordFunctionButton;
import com.memento.tech.backoffice.annotations.WidgetMarker;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.entity.Widget;
import com.memento.tech.backoffice.handler.EntityAnnotationHandler;
import com.memento.tech.backoffice.repository.WidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.memento.tech.backoffice.widget.WidgetMappingConstants.RECORD_FUNCTION_API_SUFFIX;
import static com.memento.tech.backoffice.widget.WidgetMappingConstants.WIDGET_API_PREFIX;

@Service
@RequiredArgsConstructor
public class DefaultEntityAnnotationHandler implements EntityAnnotationHandler {

    private static final String BASE_ENTITY_ID = "id";

    private final WidgetRepository widgetRepository;

    @Override
    public void handleEntityAnnotation(EntitySettings entitySettings, Class<?> entityClass) {
        handleEntityExclude(entitySettings, entityClass);
        handleEntityCreationDisable(entitySettings, entityClass);
        handleEntityOrder(entitySettings, entityClass);
        handleEntityTitle(entitySettings, entityClass);
        handleEntityGroup(entitySettings, entityClass);
        handleFieldForShowInList(entitySettings, entityClass);
        handleWidgetAnnotations(entitySettings, entityClass);
    }

    private void handleEntityExclude(EntitySettings entitySettings, Class<?> entityClass) {
        entitySettings.setExcludeEntity(entityClass.isAnnotationPresent(BackofficeExclude.class));
    }

    private void handleEntityCreationDisable(EntitySettings entitySettings, Class<?> entityClass) {
        entitySettings.setDisableCreation(entityClass.isAnnotationPresent(BackofficeDisableCreation.class));
    }

    private void handleEntityOrder(EntitySettings entitySettings, Class<?> entityClass) {
        Optional.ofNullable(entityClass.getAnnotation(BackofficeOrderPriority.class))
                .map(BackofficeOrderPriority::value)
                .ifPresent(entitySettings::setEntityOrder);
    }

    private void handleEntityTitle(EntitySettings entitySettings, Class<?> entityClass) {
        var title = Optional.ofNullable(entityClass.getAnnotation(BackofficeTitle.class))
                .map(BackofficeTitle::value)
                .orElse(entityClass.getSimpleName());

        entitySettings.setTitle(title);
    }

    private void handleEntityGroup(EntitySettings entitySettings, Class<?> entityClass) {
        Optional.ofNullable(entityClass.getAnnotation(BackofficeGroup.class))
                .ifPresent(groupAnnotation -> {
                    entitySettings.setEntityGroup(groupAnnotation.title());
                    entitySettings.setGroupOrder(groupAnnotation.order());
                });
    }

    private void handleFieldForShowInList(EntitySettings entitySettings, Class<?> entityClass) {
        var fieldForShow = Optional.ofNullable(entityClass.getAnnotation(BackofficeFieldForShowInList.class))
                .map(BackofficeFieldForShowInList::value)
                .orElse(BASE_ENTITY_ID);

        entitySettings.setFieldForShowInList(fieldForShow);
    }

    private void handleWidgetAnnotations(EntitySettings entitySettings, Class<?> entityClass) {
        var recordFunctionButtonAnnotation = getRecordFunctionButtonAnnotation(entityClass);

        entitySettings.setEntityWidgets(recordFunctionButtonAnnotation);
    }

    private List<Widget> getRecordFunctionButtonAnnotation(Class<?> entityClass) {
        return Stream.of(entityClass.getAnnotationsByType(RecordFunctionButton.class))
                .map(annotation -> {
                    var widgetAnnotation = annotation.annotationType().getAnnotation(WidgetMarker.class);

                    var widget = Widget
                            .builder()
                            .widgetId(annotation.widgetId())
                            .entityLevel(widgetAnnotation.entityLevel())
                            .recordLevel(widgetAnnotation.recordLevel())
                            .label(annotation.buttonLabel())
                            .widgetHandlerClass(annotation.functionBeanClass())
                            .handlerMapping(WIDGET_API_PREFIX + RECORD_FUNCTION_API_SUFFIX)
                            .build();

                    return widgetRepository.save(widget);
                })
                .toList();
    }
}