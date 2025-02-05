package com.memento.tech.backoffice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GroupMetadata implements MetadataWrapper {

    private String groupTitle;

    private boolean isGroup;

    private List<EntityMetadata> groupMetadata;

    private int order;

    private boolean configurational;

}