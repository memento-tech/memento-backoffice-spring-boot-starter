package com.memento.tech.backoffice.validator;

import com.memento.tech.backoffice.entity.BaseEntity;

public interface EntityValidators {

    void registerValidator(EntityValidator<? extends BaseEntity> validator, Class<? extends BaseEntity> entityClass);

}