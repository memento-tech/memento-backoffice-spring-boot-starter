package com.memento.tech.backoffice.dao;

import com.memento.tech.backoffice.dto.Pagination;

import java.util.List;
import java.util.Optional;

public interface EntityDao {

    Optional<?> getEntityForId(String entityName, String recordId, Class<?> entityClass);

    List<?> getEntitiesForIds(String entityName, List<String> recordIds, Class<?> entityClass);

    List<?> getAllEntities(String entityName, Class<?> entityClass, Pagination pagination);

    long getEntityRecordCount(String tableName);

    void saveOrUpdate(Object entityData);

    void removeRecord(Object record);

    List<String> getEntityIdsForProperty(String entityName, String propertyName, Object propertyValue);
}