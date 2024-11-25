package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.entity.Widget;
import com.memento.tech.backoffice.exception.BackofficeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WidgetMetadataPopulatorTest {

    @InjectMocks
    private WidgetMetadataPopulator widgetMetadataPopulator;

    @Test
    void testPopulateWidgetMetadata_nullWidget() {
        assertThrows(BackofficeException.class, () -> widgetMetadataPopulator.populateWidgetMetadata(null));
    }

    @Test
    void testPopulateWidgetMetadata_validWidget() {
        var testWidget = Widget.builder()
                .widgetId("testID")
                .handlerMapping("testHandlerMapping")
                .entityLevel(true)
                .recordLevel(true)
                .label("testLabel")
                .build();

        var result = widgetMetadataPopulator.populateWidgetMetadata(testWidget);

        assertNotNull(result);
        assertEquals(testWidget.getWidgetId(), result.widgetId());
        assertEquals(testWidget.getHandlerMapping(), result.handleMapping());
        assertEquals(testWidget.isEntityLevel(), result.entityLevel());
        assertEquals(testWidget.isRecordLevel(), result.recordLevel());
    }

}