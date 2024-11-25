package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.entity.EntitySettings;

import java.util.List;
import java.util.Optional;

public interface EntitySettingsService {

    List<EntitySettings> getEntitySettings();

    List<EntitySettings> createEntitySettings();

    Optional<EntitySettings> getEntitySettingsForName(String entityName);

    Optional<EntitySettings> getEntitySettingsForEntity(Object entity);
}