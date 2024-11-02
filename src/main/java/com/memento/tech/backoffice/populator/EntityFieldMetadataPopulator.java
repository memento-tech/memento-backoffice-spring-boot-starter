package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.dto.EntityFieldMetadata;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import org.springframework.stereotype.Service;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static java.util.Objects.isNull;

@Service
public class EntityFieldMetadataPopulator {

    public EntityFieldMetadata populateFieldMetadata(EntityFieldSettings entityFieldSettings) {
        if (isNull(entityFieldSettings)) {
            throw new BackofficeException("EntityFieldMetadataPopulator::populateFieldMetadata : Entity field settings object must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

        return EntityFieldMetadata
                .builder()
                .id(entityFieldSettings.getField())
                .entityName(entityFieldSettings.getEntityName())
                .name(entityFieldSettings.getName())
                .collectionFieldForShow(entityFieldSettings.getCollectionFieldForShow())
                .multivalue(entityFieldSettings.isMultivalue())
                .basic(entityFieldSettings.isBasic())
                .unique(entityFieldSettings.isUniqueField())
                .updatable(entityFieldSettings.isUpdatable())
                .changeUpdatableAllowed(entityFieldSettings.isChangeUpdatableAllowed())
                .isPassword(entityFieldSettings.isPassword())
                .required(entityFieldSettings.isRequired())
                .changeRequiredAllowed(entityFieldSettings.isChangeOptionalAllowed())
                .build();
    }
}