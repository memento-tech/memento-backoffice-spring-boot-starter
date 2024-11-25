package com.memento.tech.backoffice.dto;

import lombok.Builder;

@Builder
public record WidgetMetadata(String widgetId,
                             String handleMapping,
                             boolean entityLevel,
                             boolean recordLevel,
                             String label) {

}
