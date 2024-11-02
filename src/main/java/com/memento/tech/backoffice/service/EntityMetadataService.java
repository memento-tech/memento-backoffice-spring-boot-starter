package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.dto.EntityMetadata;

import java.util.List;

public interface EntityMetadataService {

    List<EntityMetadata> getAllEntityMetadata();

    List<EntityMetadata> getRefreshedEntityMetadata();

}