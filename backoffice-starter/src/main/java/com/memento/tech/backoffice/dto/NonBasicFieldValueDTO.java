package com.memento.tech.backoffice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NonBasicFieldValueDTO(String entityName, String simpleName, List<String> valueIds, boolean multivalue,
                                    Object translationData) {

}