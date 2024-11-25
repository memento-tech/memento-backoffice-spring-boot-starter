package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.dto.DeleteEntityRequestDTO;
import com.memento.tech.backoffice.dto.EntityDataDTO;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.dto.Pagination;
import com.memento.tech.backoffice.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/backoffice/entity")
@RequiredArgsConstructor
public class EntityController {

    private final DataService dataService;

    @GetMapping
    public EntityDataDTO getEntityRecordForId(@RequestParam String entityName, @RequestParam String recordId) {
        return dataService.getDataForEntityWithId(entityName, recordId);
    }

    @GetMapping("/list")
    public List<EntityDataDTO> getEntityRecordsForIds(@RequestParam String entityName, @RequestParam List<String> recordIds) {
        return dataService.getDataForEntitiesWithIds(entityName, recordIds);
    }

    @GetMapping("/all")
    public Set<EntityDataDTO> getAllRecordsForEntity(@RequestParam String entityName, Pagination pagination) {
        return dataService.getDataForEntity(entityName, pagination);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveEntity(@RequestParam(defaultValue = "false") boolean isNew, @RequestBody EntityWrapper<?> entityToSave) {
        dataService.saveRecord(entityToSave, isNew);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteEntity(@RequestBody DeleteEntityRequestDTO deleteEntityRequestDTO) {
        dataService.removeRecordWithId(deleteEntityRequestDTO.entityName(), deleteEntityRequestDTO.recordId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}