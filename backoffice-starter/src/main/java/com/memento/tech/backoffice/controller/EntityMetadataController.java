package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.dto.EntityMetadata;
import com.memento.tech.backoffice.service.EntityMetadataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/metadata")
@AllArgsConstructor
public class EntityMetadataController {

    public final EntityMetadataService entityMetadataService;

    @GetMapping("/refresh")
    public List<EntityMetadata> refreshEntitySettings() {
        return entityMetadataService.getRefreshedEntityMetadata();
    }

    @GetMapping("/all")
    public List<EntityMetadata> getEntitiesDetails() {
        return entityMetadataService.getAllEntityMetadata();
    }
}