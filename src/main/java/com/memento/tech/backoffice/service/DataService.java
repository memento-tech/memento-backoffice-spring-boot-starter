package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.dto.EntityDataDTO;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.entity.BaseEntity;

import java.util.List;
import java.util.Set;

public interface DataService {

    void saveRecord(EntityWrapper<? extends BaseEntity> entityWrapper, boolean isNew);

    Set<EntityDataDTO> getDataForEntity(String entityName, Pagination pagination);

    EntityDataDTO getDataForEntityWithId(String entityName, String recordId);

    List<EntityDataDTO> getDataForEntitiesWithIds(String entityName, List<String> recordIds);

    void removeRecordWithId(String entityName, String recordId);

}