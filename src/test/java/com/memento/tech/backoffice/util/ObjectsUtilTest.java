package com.memento.tech.backoffice.util;

import com.memento.tech.backoffice.entity.EntityFieldSettings;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ObjectsUtilTest {

    @Test
    void testGetField_nullClass() {
        var result = ObjectsUtil.getField(null, "test");

        assertNull(result);
    }

    @Test
    void testGetField_objectClass() {
        var result = ObjectsUtil.getField(Object.class, "test");

        assertNull(result);
    }

    @Test
    void testGetField_nonExistingField() {
        class NonExistingFieldClass {

            private String test;
        }

        var result = ObjectsUtil.getField(NonExistingFieldClass.class, "nonExisting");

        assertNull(result);
    }

    @Test
    void testGetField_existingField() {
        class ExistingFieldClass {

            private String test;
        }

        var result = ObjectsUtil.getField(ExistingFieldClass.class, "test");

        assertNotNull(result);
        assertEquals("test", result.getName());
    }

    @Test
    void testGetField_subClassNonExistingField() {
        class TopNonExistingFieldClass {

            private String topField;
        }

        class ChildNonExistingFieldClass {

            private String childField;
        }

        var result = ObjectsUtil.getField(ChildNonExistingFieldClass.class, "nonExisting");

        assertNull(result);
    }

    @Test
    void testGetField_subClassTopExistingField() {
        class TopNonExistingFieldClass {

            private String topField;
        }

        class ChildNonExistingFieldClass extends TopNonExistingFieldClass {

            private String childField;
        }

        var result = ObjectsUtil.getField(ChildNonExistingFieldClass.class, "topField");

        assertNotNull(result);
        assertEquals("topField", result.getName());
    }

    @Test
    void testGetField_subClassChildExistingField() {
        class TopNonExistingFieldClass {

            private String topField;
        }

        class ChildNonExistingFieldClass extends TopNonExistingFieldClass {

            private String childField;
        }

        var result = ObjectsUtil.getField(ChildNonExistingFieldClass.class, "childField");

        assertNotNull(result);
        assertEquals("childField", result.getName());
    }

    @Test
    void testGetFieldValue_nullParams() {
        assertThrows(NullPointerException.class, () -> ObjectsUtil.getFieldValue(null, null));
        assertThrows(NullPointerException.class, () -> ObjectsUtil.getFieldValue(new Object(), null));
        assertThrows(NullPointerException.class, () -> ObjectsUtil.getFieldValue(null, new EntityFieldSettings()));
    }

    @Test
    void testGetFieldValue_nonExistingField() {
        class NonExistingFieldClassForValue {

            private String test;
        }

        var nonExistingFieldSettings = EntityFieldSettings.builder()
                .field("nonExistingField")
                .build();

        assertThrows(NullPointerException.class, () -> ObjectsUtil.getFieldValue(new NonExistingFieldClassForValue(), nonExistingFieldSettings));
    }

    @Test
    void testGetFieldValue_nullValueSet() {
        class ExistingFieldClassNullValue {

            private String existingField;
        }

        var existingFieldSettings = EntityFieldSettings.builder()
                .field("existingField")
                .build();

        var result = ObjectsUtil.getFieldValue(new ExistingFieldClassNullValue(), existingFieldSettings);

        assertNull(result);
    }

    @Test
    void testGetFieldValue_nonNullValueSet() {
        @Data
        class ExistingFieldClassNonNullValue {

            private String existingField;
        }

        var existingFieldSettings = EntityFieldSettings.builder()
                .field("existingField")
                .build();

        var objectToTest = new ExistingFieldClassNonNullValue();

        objectToTest.setExistingField("TEST");

        var result = ObjectsUtil.getFieldValue(objectToTest, existingFieldSettings);

        assertNotNull(result);
        assertEquals("TEST", result);
    }

    @Test
    void testSetFieldValue_nullParams() {
        assertThrows(NullPointerException.class, () -> ObjectsUtil.setFieldValue(null, null, null));
        assertThrows(NullPointerException.class, () -> ObjectsUtil.setFieldValue(new Object(), null, null));
        assertThrows(NullPointerException.class, () -> ObjectsUtil.setFieldValue(null, null, new EntityFieldSettings()));
    }

    @Test
    void testSetFieldValue_nonExistingField() {
        class NonExistingFieldToSetClass {

            private String test;
        }

        var nonExistingFieldSettings = EntityFieldSettings.builder()
                .field("nonExistingField")
                .build();

        assertThrows(NullPointerException.class, () -> ObjectsUtil.setFieldValue(new NonExistingFieldToSetClass(), "Test", nonExistingFieldSettings));
    }

    @Test
    void testSetFieldValue_existingField_setNullToExisting() {
        @Data
        class NonExistingFieldToSetClass {

            private String existingFieldWithValue;
        }

        var nonExistingFieldSettings = EntityFieldSettings.builder()
                .field("existingFieldWithValue")
                .build();

        var objectToTest = new NonExistingFieldToSetClass();

        objectToTest.setExistingFieldWithValue("Test");

        ObjectsUtil.setFieldValue(objectToTest, null, nonExistingFieldSettings);

        assertNull(objectToTest.getExistingFieldWithValue());
    }

    @Test
    void testSetFieldValue_existingField_setValue() {
        @Data
        class NonExistingFieldToSetClass {

            private String existingFieldWithoutValue;
        }

        var nonExistingFieldSettings = EntityFieldSettings.builder()
                .field("existingFieldWithoutValue")
                .build();

        var objectToTest = new NonExistingFieldToSetClass();

        ObjectsUtil.setFieldValue(objectToTest, "TEST", nonExistingFieldSettings);

        assertNotNull(objectToTest.getExistingFieldWithoutValue());
        assertEquals("TEST", objectToTest.getExistingFieldWithoutValue());
    }

    @Test
    void testSetFieldValue_existingField_wrongType() {
        @Data
        class NonExistingFieldToSetClass {

            private int test;
        }

        var nonExistingFieldSettings = EntityFieldSettings.builder()
                .field("test")
                .build();

        var objectToTest = new NonExistingFieldToSetClass();

        assertThrows(IllegalArgumentException.class, () -> ObjectsUtil.setFieldValue(objectToTest, "TEST", nonExistingFieldSettings));
    }
}