package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.dto.WidgetMetadata;
import com.memento.tech.backoffice.entity.Widget;
import com.memento.tech.backoffice.exception.BackofficeException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;

@Service
public class WidgetMetadataPopulator {

    public WidgetMetadata populateWidgetMetadata(Widget widget) {
        if (Objects.isNull(widget)) {
            throw new BackofficeException("EntityMetadataPopuplator::populateWidgetMetadata : Widget object must not be null.", INTERNAL_BACKOFFICE_ERROR);
        }

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
