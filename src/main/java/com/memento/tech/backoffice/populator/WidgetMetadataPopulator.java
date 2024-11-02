package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.dto.WidgetMetadata;
import com.memento.tech.backoffice.entity.Widget;
import org.springframework.stereotype.Service;

@Service
public class WidgetMetadataPopulator {

    public WidgetMetadata populateWidgetMetadata(Widget widget) {
        return WidgetMetadata
                .builder()
                .widgetId(widget.getWidgetId())
                .handleMapping(widget.getHandlerMapping())
                .entityLevel(widget.isEntityLevel())
                .recordLevel(widget.isRecordLevel())
                .label(widget.getLabel())
                .build();
    }
}
