package com.memento.tech.backoffice.converter;

import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslationConverter implements Converter<Translation, String> {

    private final TranslationService translationService;

    @Override
    public String convert(MappingContext<Translation, String> context) {
        var translationCode = Optional.ofNullable(context)
                .map(MappingContext::getSource)
                .map(Translation::getCode)
                .orElseThrow(() -> new BackofficeException("Translation object not found in context.", ""));

        return translationService.getTranslation(translationCode);
    }
}
