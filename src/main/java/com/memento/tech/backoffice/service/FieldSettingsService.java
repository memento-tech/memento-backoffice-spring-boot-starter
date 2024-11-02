package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.entity.EntityFieldSettings;
import jakarta.persistence.metamodel.EntityType;

import java.util.Set;

public interface FieldSettingsService {

    Set<EntityFieldSettings> createEntityFields(EntityType<?> entityType);

    Set<EntityFieldSettings> orderFields(Set<EntityFieldSettings> unorderedFieldSettings);
}