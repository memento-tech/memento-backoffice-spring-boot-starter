package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.entity.Widget;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import com.memento.tech.backoffice.widget.RecordFunctionButtonHandler;
import com.memento.tech.backoffice.widget.WidgetHandlerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.memento.tech.backoffice.widget.WidgetMappingConstants.RECORD_FUNCTION_API_SUFFIX;
import static com.memento.tech.backoffice.widget.WidgetMappingConstants.WIDGET_API_PREFIX;

@RestController
@RequestMapping(WIDGET_API_PREFIX)
@RequiredArgsConstructor
public class WidgetController {

    private final WidgetHandlerService<BaseEntity> widgetHandlerService;

    private final EntityService entityService;

    private final EntitySettingsService entitySettingsService;

    @GetMapping
    public ResponseEntity<?> showRecordWidget(@RequestParam String widgetId, @RequestParam String entityName, @RequestParam String recordId) {
        var entitySettings = entitySettingsService.getEntitySettingsForName(entityName)
                .orElseThrow();

        var entity = entityService.getEntityForId(entityName, recordId, entitySettings.getEntityClass())
                .filter(BaseEntity.class::isInstance)
                .map(BaseEntity.class::cast)
                .orElseThrow();

        var result = getRecordFunctionWidget(entitySettings, widgetId).show(entity);

        return result
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @GetMapping(RECORD_FUNCTION_API_SUFFIX)
    public ResponseEntity<?> executeRecordFunctionWidget(@RequestParam String widgetId, @RequestParam String entityName, @RequestParam String recordId) {
        var entitySettings = entitySettingsService.getEntitySettingsForName(entityName)
                .orElseThrow();

        var entity = entityService.getEntityForId(entityName, recordId, entitySettings.getEntityClass())
                .filter(BaseEntity.class::isInstance)
                .map(BaseEntity.class::cast)
                .orElseThrow();

        getRecordFunctionWidget(entitySettings, widgetId).handle(entity);

        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("unchecked")
    private RecordFunctionButtonHandler<BaseEntity> getRecordFunctionWidget(EntitySettings entitySettings, String widgetId) {
        return CollectionUtils.emptyIfNull(entitySettings.getEntityWidgets())
                .stream()
                .filter(widget -> widget.getWidgetId().equals(widgetId))
                .findFirst()
                .map(Widget::getWidgetHandlerClass)
                .map(widgetHandlerService::getWidgetHandlerForClass)
                .filter(RecordFunctionButtonHandler.class::isInstance)
                .map(RecordFunctionButtonHandler.class::cast)
                .orElseThrow();
    }

}
