package com.memento.tech.backoffice.dto;

import com.memento.tech.backoffice.entity.EntityCreationSettings;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EntityMetadata implements MetadataWrapper {

    private String entityName;

    String entityTitle;

    String fieldForShowInList;

    List<EntityFieldMetadata> entityFields;

    EntityCreationSettingsMetadata creationSettingsMetadata;

    List<WidgetMetadata> widgets;

    boolean translation;

    boolean media;

    long numOfRecords;

    boolean exclude;

    int order;

}