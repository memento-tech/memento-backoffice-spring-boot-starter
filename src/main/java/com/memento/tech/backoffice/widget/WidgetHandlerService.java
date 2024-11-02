package com.memento.tech.backoffice.widget;

public interface WidgetHandlerService<T> {

    @SuppressWarnings("rawtypes")
    WidgetHandler<T> getWidgetHandlerForClass(Class<? extends WidgetHandler> handlerClass);

}
