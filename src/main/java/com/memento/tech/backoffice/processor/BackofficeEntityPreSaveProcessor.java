package com.memento.tech.backoffice.processor;

import com.memento.tech.backoffice.entity.BaseEntity;

public interface BackofficeEntityPreSaveProcessor<T extends BaseEntity> {

    void process(T entity);

}
