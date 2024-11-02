package com.memento.tech.backoffice.dao.impl;

import com.memento.tech.backoffice.dao.EntityDao;
import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.exception.BackofficeException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@AllArgsConstructor
public class DefaultEntityDao implements EntityDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<?> getEntityForId(String entityName, String recordId, Class<?> entityClass) {
        if (isBlank(entityName) || isNull(entityClass)) {
            throw new BackofficeException("DefaultEntityDao::getEntityForId : Entity name, record ID and entity class must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        if (isBlank(recordId)) {
            return Optional.empty();
        }

        return entityManager
                .createQuery("SELECT a FROM " + entityName + " a WHERE a.id=:id", entityClass)
                .setParameter("id", recordId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<?> getEntitiesForIds(String entityName, List<String> recordIds, Class<?> entityClass) {
        if (isBlank(entityName) || isNull(entityClass)) {
            throw new BackofficeException("DefaultEntityDao::getEntityForId : Entity name, record ID and entity class must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        if (CollectionUtils.isEmpty(recordIds)) {
            return List.of();
        }

        return entityManager
                .createQuery("SELECT a FROM " + entityName + " a WHERE a.id IN :ids ORDER BY a.id", entityClass)
                .setParameter("ids", recordIds)
                .getResultList();
    }

    @Override
    public List<?> getAllEntities(String entityName, Class<?> entityClass, Pagination pagination) {
        if (isBlank(entityName) || isNull(entityClass) || isNull(pagination)) {
            throw new BackofficeException("DefaultEntityDao::getAllEntities : Entity name and entity class must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        return entityManager.createQuery("SELECT a FROM " + entityName + " a ORDER BY a.id", entityClass)
                .setFirstResult(pagination.pageNumber() * pagination.pageSize())
                .setMaxResults(pagination.pageSize() > 0 ? pagination.pageSize() : Integer.MAX_VALUE)
                .getResultList()
                .parallelStream()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public long getEntityRecordCount(String tableName) {
        if (isBlank(tableName)) {
            throw new BackofficeException("DefaultEntityDao::getEntityRecordCount : Entity name must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        return (Long) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + tableName).getSingleResult();
    }

    @Override
    @Transactional
    public void saveOrUpdate(Object entityData) {
        if (isNull(entityData)) {
            throw new BackofficeException("DefaultEntityDao::saveOrUpdate : Entity data must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        entityManager.merge(entityData);
    }

    @Override
    @Transactional
    public void removeRecord(Object record) {
        if (isNull(record)) {
            throw new BackofficeException("DefaultEntityDao::removeRecord : Record must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        try {
            entityManager.remove(record);
            entityManager.flush();
        } catch (ConstraintViolationException exception) {
            throw new BackofficeException("BACKOFFICE: Cannot remove entity because there is a reference to existing entity which can not be undone this way", INTERNAL_BACKOFFICE_ERROR);
        }
    }

    @Override
    public List<String> getEntityIdsForProperty(String entityName, String propertyName, Object propertyValue) {
        if (isBlank(entityName) || isBlank(propertyName) || isNull(propertyValue) || (propertyValue instanceof String && isBlank((String) propertyValue))) {
            throw new BackofficeException("DefaultEntityDao::getEntityForProperty : Entity name, property name and property value must not be null or empty.", INTERNAL_BACKOFFICE_ERROR);
        }

        return entityManager
                .createQuery("SELECT a.id FROM " + entityName + " a WHERE a." + propertyName + "=:" + propertyName, String.class)
                .setParameter(propertyName, propertyValue)
                .getResultList();
    }
}