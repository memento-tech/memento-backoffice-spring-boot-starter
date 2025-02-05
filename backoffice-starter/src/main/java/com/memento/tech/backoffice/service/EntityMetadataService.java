package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.dto.EntityMetadata;
import com.memento.tech.backoffice.dto.MetadataWrapper;

import java.util.List;

public interface EntityMetadataService {

    List<MetadataWrapper> getAllEntityMetadata();

    List<MetadataWrapper> getRefreshedEntityMetadata();

}