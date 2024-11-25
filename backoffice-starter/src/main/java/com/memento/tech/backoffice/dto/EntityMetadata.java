package com.memento.tech.backoffice.dto;

import com.memento.tech.backoffice.entity.EntityCreationSettings;
import lombok.Builder;

import java.util.List;

@Builder
public record EntityMetadata(String entityName,
                             String entityTitle,
                             String entityGroup,
                             String fieldForShowInList,
                             List<EntityFieldMetadata> entityFields,
                             EntityCreationSettings creationFields,
                             List<WidgetMetadata> widgets,
                             boolean translation,
                             boolean media,
                             long numOfRecords,
                             boolean exclude) {

}