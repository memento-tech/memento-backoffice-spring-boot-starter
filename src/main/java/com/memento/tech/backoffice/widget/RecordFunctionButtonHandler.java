package com.memento.tech.backoffice.widget;

public interface RecordFunctionButtonHandler<T> extends WidgetHandler<T> {

    void handle(T entity);
}
