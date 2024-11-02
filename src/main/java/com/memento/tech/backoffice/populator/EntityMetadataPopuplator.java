package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.dto.EntityMetadata;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;

@Service
@RequiredArgsConstructor
public class EntityMetadataPopuplator {

    private final EntityFieldMetadataPopulator entityFieldMetadataPopulator;

    private final WidgetMetadataPopulator widgetMetadataPopulator;

    public EntityMetadata populateEntityMetadata(EntitySettings entitySettings, long numberOfRecords) {
        if (Objects.isNull(entitySettings)) {
            throw new BackofficeException("EntityMetadataPopuplator::populateEntityMetadata : Entity settings object must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        var fieldsMetadata = entitySettings.getFieldSettings()
                .stream()
                .map(entityFieldMetadataPopulator::populateFieldMetadata)
                .toList();

        var widgetsMetadata = CollectionUtils.emptyIfNull(entitySettings.getEntityWidgets())
                .stream()
                .map(widgetMetadataPopulator::populateWidgetMetadata)
                .toList();

        return EntityMetadata
                .builder()
                .entityName(entitySettings.getEntityName())
                .entityTitle(entitySettings.getTitle())
                .entityGroup(entitySettings.getEntityGroup())
                .fieldForShowInList(entitySettings.getFieldForShowInList())
                .entityFields(fieldsMetadata)
                .creationFields(entitySettings.getCreationSettings())
                .widgets(widgetsMetadata)
                .numOfRecords(numberOfRecords)
                .exclude(entitySettings.isExcludeEntity())
                .translation(entitySettings.isTranslation())
                .media(entitySettings.isMedia())
                .build();
    }
}