package com.memento.tech.backoffice.service.impl;

import com.memento.tech.backoffice.dto.TranslationDTO;
import com.memento.tech.backoffice.entity.Translation;
import com.memento.tech.backoffice.entity.TranslationWrapper;
import com.memento.tech.backoffice.exception.BackofficeException;
import com.memento.tech.backoffice.interceptor.TranslationRequestData;
import com.memento.tech.backoffice.repository.TranslationRepository;
import com.memento.tech.backoffice.service.LanguageService;
import com.memento.tech.backoffice.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.memento.tech.backoffice.exception.ExceptionCodeConstants.INTERNAL_BACKOFFICE_ERROR;
import static org.apache.commons.lang3.ObjectUtils.requireNonEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultTranslationService implements TranslationService {

    private final TranslationRepository translationRepository;

    private final TranslationRequestData translationRequestData;

    private final LanguageService languageService;

    @Value("${memento.tech.backoffice.translation.useDefaultIfBlank:false}")
    private boolean useDefaultIfBlank;

    @Override
    public String getTranslation(String translationCode) {
        final var defaultLanguage = languageService.getDefaultLanguage()
                .orElseThrow();

        return getTranslation(translationCode, defaultLanguage.getLangIsoCode());
    }

    @Override
    public String getTranslation(String translationCode, Object... params) {
        var translation = getTranslation(translationCode);

        if (Objects.isNull(params) || params.length == 0) {
            return translation;
        }

        return String.format(translation, params);
    }

    @Override
    public String getTranslation(String translationCode, String defaultLangIsoCode) {
        var translationWrapper = Optional.ofNullable(translationCode)
                .filter(StringUtils::isNotBlank)
                .flatMap(translationRepository::findByCode)
                .orElseThrow(() -> new BackofficeException("Translation code can not be blank.", INTERNAL_BACKOFFICE_ERROR));

        var result = getTranslationForLangIsoCode(translationWrapper, translationRequestData.getCurrentLangIsoCode());

        if (StringUtils.isBlank(result) && useDefaultIfBlank) {
            if (defaultLangIsoCode.equals("unknown")) {
                log.error("BACKOFFICE: Please set up environment properties for translation default iso code using property [backoffice.translation.defaultLanguage]");
            } else {
                final var defaultLanguage = languageService.getLanguageForIsoCode(defaultLangIsoCode)
                        .orElseThrow();

                log.info("BACKOFFICE: Defaulting to default language with iso code [{}] for translation with code [{}].", defaultLanguage, translationCode);
                result = getTranslationForLangIsoCode(translationWrapper, defaultLangIsoCode);
            }
        }


        return result;
    }

    @Override
    public String getTranslation(String translationCode, String defaultLangIsoCode, Object... params) {
        var translation = getTranslation(translationCode, defaultLangIsoCode);

        if (Objects.isNull(params) || params.length == 0) {
            return translation;
        }

        return String.format(translation, params);
    }

    @Override
    public String getTranslationForLangIsoCode(String translationCode, String langIsoCode) {
        var translationWrapper = Optional.ofNullable(translationCode)
                .filter(StringUtils::isNotBlank)
                .flatMap(translationRepository::findByCode)
                .orElseThrow(() -> new BackofficeException("", ""));

        return getTranslationForLangIsoCode(translationWrapper, langIsoCode);
    }

    @Override
    public Translation getTranslationData(String translationCode) {
        return translationRepository.findByCode(translationCode)
                .orElseThrow(() -> new BackofficeException("", ""));
    }

    @Override
    public Translation saveTranslation(String code, String langIsoCode, String translation) {
        requireNonEmpty(code);
        requireNonEmpty(langIsoCode);
        requireNonEmpty(translation);

        final var language = languageService.getLanguageForIsoCode(langIsoCode)
                .orElseThrow();

        var toSave = translationRepository.findByCode(code)
                .orElse(null);

        var translationWrapper = TranslationWrapper
                .builder()
                .language(language)
                .translation(translation)
                .build();

        if (Objects.isNull(toSave)) {
            toSave = Translation
                    .builder()
                    .code(code)
                    .translationWrappers(List.of(translationWrapper))
                    .build();

        } else {
            toSave.addOrUpdateTranslation(translationWrapper);
        }

        return translationRepository.save(toSave);
    }

    @Override
    public Translation saveTranslation(String code, List<TranslationDTO> translations, boolean update) {
        requireNonEmpty(code);
        requireNonEmpty(translations);

        var toSave = translationRepository.findByCode(code)
                .orElse(null);

        if (Objects.nonNull(toSave)) {
            if (!update) {
                throw new BackofficeException("BACKOFFICE: Translation with provided code already exists.", "Translation with provided code already exists.");
            }

            CollectionUtils.emptyIfNull(translations)
                    .stream()
                    .map(translation -> {
                        var language = languageService.getLanguageForIsoCode(translation.getLangIsoCode())
                                .orElseThrow();

                        return TranslationWrapper
                                .builder()
                                .language(language)
                                .translation(translation.getTranslation())
                                .build();
                    })
                    .forEach(toSave::addOrUpdateTranslation);
        } else {
            var translationWrappers = translations
                    .stream()
                    .map(translation -> {
                        var language = languageService.getLanguageForIsoCode(translation.getLangIsoCode())
                                .orElseThrow();

                        return TranslationWrapper.builder()
                                .language(language)
                                .translation(translation.getTranslation())
                                .build();
                    })
                    .toList();

            toSave = Translation.builder()
                    .code(code)
                    .translationWrappers(translationWrappers)
                    .build();
        }

        return translationRepository.save(toSave);
    }

    @Override
    public Translation getTranslationWrapper(String translationCode, List<TranslationDTO> translations) {
        var translation = Translation
                .builder()
                .code(translationCode)
                .build();

        translations
                .stream()
                .map(_translation -> {
                    final var language = languageService.getLanguageForIsoCode(_translation.getLangIsoCode())
                            .orElseThrow();

                    return TranslationWrapper
                            .builder()
                            .language(language)
                            .translation(_translation.getTranslation())
                            .build();
                })
                .forEach(translation::addOrUpdateTranslation);

        return translation;
    }

    @Override
    public Translation updateTranslationWrapper(String translationCode, List<TranslationDTO> translations) {
        var translationWrapper = translationRepository.findByCode(translationCode)
                .orElse(null);

        if (Objects.isNull(translationWrapper)) {
            return translationRepository.save(getTranslationWrapper(translationCode, translations));
        } else {
            translations.stream()
                    .map(_translation -> {
                        final var language = languageService.getLanguageForIsoCode(_translation.getLangIsoCode())
                                .orElseThrow();

                        return TranslationWrapper
                                .builder()
                                .language(language)
                                .translation(_translation.getTranslation())
                                .build();
                    })
                    .forEach(translationWrapper::addOrUpdateTranslation);

            return translationRepository.save(translationWrapper);
        }
    }

    @Override
    public void validateTranslations(List<TranslationDTO> translations, List<String> requiredLangIsoCodes) {
        var _translationIsoCodes = CollectionUtils.emptyIfNull(translations)
                .stream()
                .map(TranslationDTO::getLangIsoCode)
                .distinct()
                .toList();

        var _requiredLangIsoCodes = new HashSet<>(CollectionUtils.emptyIfNull(requiredLangIsoCodes));

        if (_translationIsoCodes.isEmpty() && CollectionUtils.isNotEmpty(_requiredLangIsoCodes)) {
            throw new BackofficeException("BACKOFFICE: translation lang iso codes list is empty!", INTERNAL_BACKOFFICE_ERROR);
        }

        if (_requiredLangIsoCodes.isEmpty() && CollectionUtils.isNotEmpty(_translationIsoCodes)) {
            throw new BackofficeException("BACKOFFICE: required lang iso codes list is empty!", INTERNAL_BACKOFFICE_ERROR);
        }

        if (_translationIsoCodes.size() != _requiredLangIsoCodes.size()) {
            throw new BackofficeException("BACKOFFICE: translations iso codes and required lang iso codes length are different!", INTERNAL_BACKOFFICE_ERROR);
        }

        if (!_requiredLangIsoCodes.containsAll(_translationIsoCodes)) {
            throw new BackofficeException("BACKOFFICE: translations iso codes and required lang iso codes are different!", INTERNAL_BACKOFFICE_ERROR);
        }
    }

    private String getTranslationForLangIsoCode(Translation translation, String langIsoCode) {
        var result = CollectionUtils.emptyIfNull(translation.getTranslationWrappers())
                .stream()
                .filter(translationWrapper -> translationWrapper.getLanguage().getLangIsoCode().equals(langIsoCode))
                .findFirst()
                .map(TranslationWrapper::getTranslation)
                .orElse(StringUtils.EMPTY);

        if (StringUtils.isBlank(result)) {
            log.info("BACKOFFICE: Translation for code [{}] not found for iso code [{}].", translation.getCode(), langIsoCode);
        }

        return result;
    }
}
