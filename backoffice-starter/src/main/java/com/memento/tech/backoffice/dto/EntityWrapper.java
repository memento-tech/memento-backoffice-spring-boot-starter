package com.memento.tech.backoffice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.memento.tech.backoffice.deserializer.EntityWrapperDeserializer;
import com.memento.tech.backoffice.entity.BaseEntity;
import lombok.Builder;

@Builder
@JsonDeserialize(using = EntityWrapperDeserializer.class)
public record EntityWrapper<T extends BaseEntity>(String entityName, T entityData) {

}