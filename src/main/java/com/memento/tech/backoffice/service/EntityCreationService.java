package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.entity.EntityCreationSettings;
import com.memento.tech.backoffice.entity.EntitySettings;

public interface EntityCreationService {

    EntityCreationSettings initCreationSettings(final EntitySettings entitySettings, boolean disableCreation);
}