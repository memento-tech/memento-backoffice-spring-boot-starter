package com.memento.tech.backoffice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EntityCreationSettingsMetadata {

    private boolean allowCreation;

    List<EntityFieldMetadata> creationFields;
}
