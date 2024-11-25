package com.memento.tech.backoffice.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record EntityDataDTO(String entityName, Map<String, Object> data, Object translationData) {

}