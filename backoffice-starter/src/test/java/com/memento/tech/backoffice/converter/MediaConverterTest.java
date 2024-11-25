package com.memento.tech.backoffice.converter;

import com.memento.tech.backoffice.entity.Media;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaConverterTest {

    @InjectMocks
    private MediaConverter mediaConverter;

    private static final String TEST_URL = "test";

    @Mock
    private Media mockSource;

    @Mock
    private MappingContext<Media, String> mockContext;

    @BeforeEach
    void before() {
        when(mockContext.getSource()).thenReturn(mockSource);
    }

    @Test
    void testConvert_validMock() {
        when(mockSource.getMediaUrl()).thenReturn(TEST_URL);

        var result = mediaConverter.convert(mockContext);

        assertNotNull(result);
        assertEquals(TEST_URL, result);
    }

    @Test
    void testConvert_nullSource() {
        when(mockContext.getSource()).thenReturn(null);

        var result = mediaConverter.convert(mockContext);

        assertNotNull(result);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    void testConvert_emptyMediaUrl() {
        var result = mediaConverter.convert(mockContext);

        assertNotNull(result);
        assertEquals(StringUtils.EMPTY, result);
    }
}