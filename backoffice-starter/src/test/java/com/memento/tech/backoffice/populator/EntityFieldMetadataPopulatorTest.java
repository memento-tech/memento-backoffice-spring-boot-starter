package com.memento.tech.backoffice.populator;

import com.memento.tech.backoffice.entity.EntityFieldSettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityFieldMetadataPopulatorTest {

    @InjectMocks
    private EntityFieldMetadataPopulator entityFieldMetadataPopulator;

    @Test
    void testPopulateFieldMetadata_nullEntityField() {
        assertThrows(BackofficeException.class, () -> entityFieldMetadataPopulator.populateFieldMetadata(null));
    }

    @Test
    void testPopulateFieldMetadata_validEntityField() {
        var testFieldSettings = EntityFieldSettings
                .builder()
                .field("testField")
                .entityName("testFieldName")
                .name("testName")
                .collectionFieldForShow("testCollectionFieldForShow")
                .multivalue(true)
                .basic(true)
                .uniqueField(true)
                .updatable(true)
                .changeUpdatableAllowed(true)
                .isPassword(true)
                .required(true)
                .changeOptionalAllowed(true)
                .build();

        var result = entityFieldMetadataPopulator.populateFieldMetadata(testFieldSettings);

        assertNotNull(result);
        assertEquals(testFieldSettings.getField(), result.id());
        assertEquals(testFieldSettings.getEntityName(), result.entityName());
        assertEquals(testFieldSettings.getName(), result.name());
        assertEquals(testFieldSettings.getCollectionFieldForShow(), result.collectionFieldForShow());
        assertEquals(testFieldSettings.getField(), result.id());
        assertTrue(result.multivalue());
        assertTrue(result.basic());
        assertTrue(result.unique());
        assertTrue(result.updatable());
        assertTrue(result.changeRequiredAllowed());
        assertTrue(result.changeUpdatableAllowed());
        assertTrue(result.isPassword());
    }
}