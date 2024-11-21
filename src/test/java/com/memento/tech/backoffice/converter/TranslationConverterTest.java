package com.memento.tech.backoffice.converter;

import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationConverterTest {

    private static final String TEST_CODE = "translation.test.code";

    private static final String TRANSLATION = "TEST_TRANSLATION";

    @InjectMocks
    private TranslationConverter translationConverter;

    @Mock
    private TranslationService translationService;

    @Mock
    private MappingContext<Translation, String> mockContext;

    @Mock
    private Translation mockSource;

    @Test
    void testConvert_validMock() {
        when(mockContext.getSource()).thenReturn(mockSource);
        when(translationService.getTranslation(TEST_CODE)).thenReturn(TRANSLATION);
        when(mockSource.getCode()).thenReturn(TEST_CODE);

        var result = translationConverter.convert(mockContext);

        assertNotNull(result);
        assertEquals(TRANSLATION, result);
    }

    @Test
    void testConvert_nullSource() {
        assertThrows(BackofficeException.class, () -> translationConverter.convert(mockContext));
    }
}