package com.memento.tech.backoffice.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.memento.tech.backoffice.entity.BaseEntity;
import com.memento.tech.backoffice.entity.EntitySettings;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityWrapperDeserializerTest {

    private static final String ENTITY_NAME_NODE = "entityName";

    private static final String ENTITY_DATA_NODE = "entityData";

    private static final String ENTITY_TEST_NAME = "test";

    @InjectMocks
    private EntityWrapperDeserializer entityWrapperDeserializer;

    @Mock
    private EntitySettingsService entitySettingsService;

    @Mock
    private EntityService entityService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private TreeNode mockTreeNode;

    @BeforeEach
    void beforeEach() throws IOException {
        var objectCodec = mock(ObjectCodec.class);
        when(jsonParser.getCodec()).thenReturn(objectCodec);

        when(objectCodec.readTree(any())).thenReturn(mockTreeNode);
    }

    @Test
    void testDeserialize_nullEntityNameNode() {
        when(mockTreeNode.get(ENTITY_NAME_NODE)).thenReturn(NullNode.getInstance());
        when(mockTreeNode.get(ENTITY_DATA_NODE)).thenReturn(new ObjectNode(null));

        assertThrows(BackofficeException.class, () -> entityWrapperDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    void testDeserialize_nullEntityDataNode() {
        when(mockTreeNode.get(ENTITY_NAME_NODE)).thenReturn(TextNode.valueOf("test"));
        when(mockTreeNode.get(ENTITY_DATA_NODE)).thenReturn(NullNode.getInstance());

        assertThrows(BackofficeException.class, () -> entityWrapperDeserializer.deserialize(jsonParser, deserializationContext));
    }


    @Test
    void testDeserialize_nullEntitySettingsForName() {
        when(mockTreeNode.get(ENTITY_NAME_NODE)).thenReturn(TextNode.valueOf(ENTITY_TEST_NAME));
        when(mockTreeNode.get(ENTITY_DATA_NODE)).thenReturn(new ObjectNode(null));

        when(entitySettingsService.getEntitySettingsForName(any())).thenReturn(Optional.empty());

        assertThrows(BackofficeException.class, () -> entityWrapperDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    void testDeserialize_emptyDataFields() {
        Map<String, JsonNode> map = Map.of();

        var entityDataNode = mock(ObjectNode.class);
        when(entityDataNode.fields()).thenReturn(map.entrySet().iterator());

        when(mockTreeNode.get(ENTITY_NAME_NODE)).thenReturn(TextNode.valueOf(ENTITY_TEST_NAME));
        when(mockTreeNode.get(ENTITY_DATA_NODE)).thenReturn(entityDataNode);

        var entitySettings = new EntitySettings();
        entitySettings.setEntityClass(BaseEntity.class);
        entitySettings.setFieldSettings(Set.of());

        when(entitySettingsService.getEntitySettingsForName(any())).thenReturn(Optional.of(entitySettings));

        var result = entityWrapperDeserializer.deserialize(jsonParser, deserializationContext);

        assertNotNull(result);
        assertNotNull(result.entityData());
        assertInstanceOf(BaseEntity.class, result.entityData());
        assertEquals(ENTITY_TEST_NAME, result.entityName());
    }

    @Test
    void testDeserialize_nonExistingFieldSettings() {
        Map<String, JsonNode> map = new HashMap<>();
        map.put("test", TextNode.valueOf("test"));

        var entityDataNode = mock(ObjectNode.class);
        when(entityDataNode.fields()).thenReturn(map.entrySet().iterator());

        when(mockTreeNode.get(ENTITY_NAME_NODE)).thenReturn(TextNode.valueOf(ENTITY_TEST_NAME));
        when(mockTreeNode.get(ENTITY_DATA_NODE)).thenReturn(entityDataNode);

        var entitySettings = new EntitySettings();
        entitySettings.setEntityClass(BaseEntity.class);
        entitySettings.setFieldSettings(Set.of());

        when(entitySettingsService.getEntitySettingsForName(any())).thenReturn(Optional.of(entitySettings));

        assertThrows(BackofficeException.class, () -> entityWrapperDeserializer.deserialize(jsonParser, deserializationContext));
    }
}