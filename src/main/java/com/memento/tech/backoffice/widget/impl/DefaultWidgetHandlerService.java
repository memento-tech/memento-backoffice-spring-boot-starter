package com.memento.tech.backoffice.widget.impl;

import com.memento.tech.backoffice.widget.WidgetHandler;
import com.memento.tech.backoffice.widget.WidgetHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultWidgetHandlerService<T> implements WidgetHandlerService<T> {

    private final List<WidgetHandler<T>> availableHandlers;

    @SuppressWarnings("rawtypes")
    public WidgetHandler<T> getWidgetHandlerForClass(Class<? extends WidgetHandler> handlerClass) {
        return availableHandlers
                .stream()
                .filter(handler -> handler.getClass().equals(handlerClass))
                .findFirst()
                .orElseThrow();
    }

}
