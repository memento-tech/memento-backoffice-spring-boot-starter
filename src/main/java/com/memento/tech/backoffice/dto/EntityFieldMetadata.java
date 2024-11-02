package com.memento.tech.backoffice.dto;

import lombok.Builder;

@Builder
public record EntityFieldMetadata(String id,
                                  String entityName,
                                  String name,
                                  String collectionFieldForShow,
                                  boolean multivalue,
                                  boolean basic,
                                  boolean unique,
                                  boolean required,
                                  boolean isPassword,
                                  boolean changeRequiredAllowed,
                                  boolean updatable,
                                  boolean changeUpdatableAllowed) {

}