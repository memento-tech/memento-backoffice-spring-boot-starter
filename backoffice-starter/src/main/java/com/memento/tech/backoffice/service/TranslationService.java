package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.dto.TranslationDTO;
import com.memento.tech.backoffice.entity.Translation;

import java.util.List;

public interface TranslationService {

    String getTranslation(final String translationCode);

    String getTranslation(final String translationCode, Object... params);

    String getTranslation(final String translationCode, final String defaultLangIsoCode);

    String getTranslation(final String translationCode, final String defaultLangIsoCode, Object... params);

    String getTranslationForLangIsoCode(final String translationCode, final String langIsoCode);

    Translation getTranslationData(final String translationCode);

    Translation saveTranslation(final String code, final String langIsoCode, final String translation);

    Translation saveTranslation(final String code, List<TranslationDTO> translations, boolean update);

    Translation getTranslationWrapper(String translationCode, List<TranslationDTO> translations);

    Translation updateTranslationWrapper(String translationCode, List<TranslationDTO> translations);

    void validateTranslations(final List<TranslationDTO> translations, List<String> requiredLangIsoCodes);
}
