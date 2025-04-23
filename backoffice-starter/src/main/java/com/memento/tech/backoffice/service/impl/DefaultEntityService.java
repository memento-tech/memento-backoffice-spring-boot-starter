package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.dao.EntityDao;
import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.mapper.ExistingEntityMapper;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.validator.impl.BasicEntityValidator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEntityService implements EntityService {

    private final EntityDao entityDao;

    private final BasicEntityValidator basicEntityValidator;

    private final ExistingEntityMapper existingEntityMapper;

    @Override
    public Optional<?> getEntityForId(String entityName, String recordId, Class<?> entityClass) {
        return entityDao.getEntityForId(entityName, recordId, entityClass);
    }

    @Override
    public List<?> getEntitiesForIds(String entityName, List<String> recordIds, Class<?> entityClass) {
        return entityDao.getEntitiesForIds(entityName, recordIds, entityClass);
    }

    @Override
    public List<?> getAllEntities(String entityName, Class<?> entityClass, Pagination pagination) {
        return entityDao.getAllEntities(entityName, entityClass, pagination);
    }

    @Override
    public void saveRecord(BaseEntity entityData, EntitySettings entitySettings, boolean isNew) {
        var entityToSave = entityData;

        if (isNew) {
            basicEntityValidator.validateEntity(entityData, entitySettings);
        } else {
            var persistedEntity = entityDao.getEntityForId(entitySettings.getEntityName(), entityData.getId(), entitySettings.getEntityClass())
                    .orElseThrow();

            basicEntityValidator.validateWithExistingEntity(entityData, (BaseEntity) persistedEntity, entitySettings);

            existingEntityMapper.map(entityData, (BaseEntity) persistedEntity, entitySettings);

            //map new fields to existing entity
            entityToSave = (BaseEntity) persistedEntity;
        }

        try {
            entityDao.saveOrUpdate(entityToSave);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BackofficeException("DefaultEntityService::saveRecord : Entity unique values have to be unique.", INTERNAL_BACKOFFICE_ERROR);
        }
    }

    @Override
    public void removeRecord(String entityName, String recordId, Class<?> entityClass) {
        var recordForRemoval = getEntityForId(entityName, recordId, entityClass)
                .orElseThrow();

        entityDao.removeRecord(recordForRemoval);
    }
}