package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.entity.Language;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.repository.LanguageRepository;
import com.memento.tech.backoffice.service.LanguageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Service
@RequiredArgsConstructor
public class DefaultLanguageService implements LanguageService {

    private static final String DEFAULT_LANGUAGE_TITLE = "DEFAULT_LANGUAGE";

    private final LanguageRepository languageRepository;


    @Value("${memento.tech.backoffice.translation.useDefaultIfBlank:false}")
    private boolean useDefaultIfBlank;

    @Value("${memento.tech.backoffice.translation.default.language:undefined}")
    private String defaultLanguage;

    @PostConstruct
    public void init() {
        if (useDefaultIfBlank) {
            if (StringUtils.equals(defaultLanguage, "undefined")) {
                throw new BackofficeException("BACKOFFICE: Please add default language using property [memento.tech.backoffice.translation.defaultLanguage]", INTERNAL_BACKOFFICE_ERROR);
            }

            languageRepository.findByLangIsoCode(defaultLanguage)
                    .ifPresentOrElse(saved -> {
                        if (!saved.getLangIsoCode().equals(defaultLanguage)) {
                            saved.setLangIsoCode(defaultLanguage);
                            languageRepository.save(saved);
                        }
                    }, () -> {
                        Arrays.stream(Locale.getISOLanguages())
                                .filter(isoCode -> isoCode.equals(defaultLanguage))
                                .findFirst()
                                .orElseThrow(() -> new BackofficeException("ISO code for default language provided via configuration is not valid", ""));

                        var defaultLanguageEntity = Language.builder()
                                .langIsoCode(defaultLanguage)
                                .title(DEFAULT_LANGUAGE_TITLE)
                                .build();

                        languageRepository.save(defaultLanguageEntity);
                    });
        }
    }

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
    public String getDefaultLanguageIsoCode() {
        return defaultLanguage;
    }
}