package com.memento.tech.backoffice.service;

import com.memento.tech.backoffice.entity.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {

    List<Language> getAllLanguages();

    Optional<Language> getLanguageForIsoCode(String langIsoCode);

    String getDefaultLanguageIsoCode();
}