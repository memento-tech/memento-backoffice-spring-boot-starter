package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.dao.EntityDao;
import com.memento.tech.backoffice.dto.EntityMetadata;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.populator.EntityMetadataPopuplator;
import com.memento.tech.backoffice.service.EntityMetadataService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Service
@AllArgsConstructor
public class DefaultEntityMetadataService implements EntityMetadataService {

    private final EntitySettingsService entitySettingsService;

    private final EntityDao entityDao;

    private final EntityMetadataPopuplator entityMetadataPopuplator;

    @Override
    public List<EntityMetadata> getAllEntityMetadata() {
        return emptyIfNull(entitySettingsService.getEntitySettings())
                .stream()
                .map(this::populateEntityMetadata)
                .toList();
    }

    @Override
    public List<EntityMetadata> getRefreshedEntityMetadata() {
        return emptyIfNull(entitySettingsService.createEntitySettings())
                .stream()
                .map(this::populateEntityMetadata)
                .toList();
    }

    private EntityMetadata populateEntityMetadata(EntitySettings entitySettings) {
        var numberOfRecords = entityDao.getEntityRecordCount(entitySettings.getTableName());
        return entityMetadataPopuplator.populateEntityMetadata(entitySettings, numberOfRecords);
    }

}