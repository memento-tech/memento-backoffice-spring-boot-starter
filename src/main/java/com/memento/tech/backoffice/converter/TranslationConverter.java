package com.memento.tech.backoffice.converter;

import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationConverter implements Converter<Translation, String> {

    private final TranslationService translationService;

    @Override
    public String convert(MappingContext<Translation, String> context) {
        return translationService.getTranslation(context.getSource().getCode());
    }
}
