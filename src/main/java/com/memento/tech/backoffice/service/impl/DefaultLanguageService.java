package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.entity.Language;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.repository.LanguageRepository;
import com.memento.tech.backoffice.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Service
@RequiredArgsConstructor
public class DefaultLanguageService implements LanguageService {

    private static final String DEFAULT_LANGUAGE_ISO_CODE = "us";

    private final LanguageRepository languageRepository;

    @Value("${memento.tech.backoffice.translation.defaultLanguage:undefined}")
    private String defaultLanguage;

    @Override
    public List<Language> getAllLanguages() {
        return emptyIfNull(languageRepository.findAll());
    }

    @Override
    public Optional<Language> getLanguageForIsoCode(String langIsoCode) {
        return Optional.ofNullable(langIsoCode)
                .filter(StringUtils::isNotBlank)
                .flatMap(languageRepository::findByLangIsoCode);
    }

    @Override
    public Optional<Language> getDefaultLanguage() {
        if (StringUtils.equals(defaultLanguage, "undefined")) {
            throw new BackofficeException("BACKOFFICE: Please add default language using property [backoffice.translation.defaultLanguage:undefined]", INTERNAL_BACKOFFICE_ERROR);
        }

        return getLanguageForIsoCode(defaultLanguage);
    }
}