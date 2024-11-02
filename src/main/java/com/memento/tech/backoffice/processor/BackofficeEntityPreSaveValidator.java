package com.memento.tech.backoffice.processor;

import com.memento.tech.backoffice.entity.BaseEntity;

public interface BackofficeEntityPreSaveValidator<T extends BaseEntity> {

    void validate(T entity);

}
