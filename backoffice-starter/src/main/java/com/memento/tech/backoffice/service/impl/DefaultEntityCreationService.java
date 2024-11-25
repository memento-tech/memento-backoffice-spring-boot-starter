package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.entity.EntityCreationSettings;
import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.repository.EntityCreationSettingsRepository;
import com.memento.tech.backoffice.service.EntityCreationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
@AllArgsConstructor
public class DefaultEntityCreationService implements EntityCreationService {

    private final EntityCreationSettingsRepository entityCreationSettingsRepository;

    @Override
    public EntityCreationSettings initCreationSettings(EntitySettings entitySettings, boolean disableCreation) {
        requireNonNull(entitySettings);

        var creationFields = entitySettings.getFieldSettings()
                .stream()
                .filter(EntityFieldSettings::isCreationExcludeField)
                .collect(Collectors.toSet());

        var creationSettings = EntityCreationSettings.builder()
                .allowCreation(!disableCreation)
                .creationFields(creationFields)
                .build();

        entityCreationSettingsRepository.save(creationSettings);

        return creationSettings;
    }
}