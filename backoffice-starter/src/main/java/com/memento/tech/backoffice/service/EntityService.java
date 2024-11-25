package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntitySettings;

import java.util.List;
import java.util.Optional;

public interface EntityService {

    Optional<?> getEntityForId(final String entityName, final String recordId, Class<?> entityClass);

    List<?> getEntitiesForIds(final String entityName, final List<String> recordIds, Class<?> entityClass);

    List<?> getAllEntities(final String entityName, Class<?> entityClass, Pagination pagination);

    void saveRecord(BaseEntity entityData, EntitySettings entitySettings, boolean isNew);

    void removeRecord(String entityName, String recordId, Class<?> entityClass);
}