package com.memento.tech.backoffice.validator;

import com.memento.tech.backoffice.entity.BaseEntity;

public interface EntityValidator<T extends BaseEntity> {

    void validate(T entityToValidate);

    void validateWithExisting(T entityToValidate, T existingEntity);

}