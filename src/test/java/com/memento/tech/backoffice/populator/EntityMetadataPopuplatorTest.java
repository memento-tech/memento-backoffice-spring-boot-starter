package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityMetadataPopuplatorTest {

    @InjectMocks
    private EntityMetadataPopuplator entityMetadataPopuplator;

    @Mock
    private EntityFieldMetadataPopulator entityFieldMetadataPopulator;

    @Mock
    private WidgetMetadataPopulator widgetMetadataPopulator;

    @Test
    void testPopulateEntityMetadata_nullEntity() {
        assertThrows(BackofficeException.class, () -> entityMetadataPopuplator.populateEntityMetadata(null, 0));
    }

    @Test
    void testPopulateEntityMetadata_validEntity() {
        var testEntitySettings = EntitySettings.builder()
                .fieldSettings(Set.of())
                .entityWidgets(List.of())
                .entityName("testName")
                .title("testTitle")
                .entityGroup("testGroup")
                .fieldForShowInList("testFieldForShow")
                .creationSettings(null)
                .excludeEntity(true)
                .translation(true)
                .media(true)
                .build();

        var result = entityMetadataPopuplator.populateEntityMetadata(testEntitySettings, 1000);

        assertNotNull(result);
        assertEquals(testEntitySettings.getEntityName(), result.entityName());
        assertEquals(testEntitySettings.getTitle(), result.entityTitle());
        assertEquals(testEntitySettings.getEntityGroup(), result.entityGroup());
        assertEquals(testEntitySettings.getFieldForShowInList(), result.fieldForShowInList());
        assertEquals(1000, result.numOfRecords());
        assertTrue(result.exclude());
        assertTrue(result.translation());
        assertTrue(result.media());
        assertNotNull(result.entityFields());
        assertNull(result.creationFields());
        assertNotNull(result.widgets());
    }

}