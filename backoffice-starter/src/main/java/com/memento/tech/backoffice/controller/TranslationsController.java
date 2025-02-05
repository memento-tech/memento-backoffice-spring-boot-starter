package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.dto.LanguageDTO;
import com.memento.tech.backoffice.dto.SaveTranslationDTO;
import com.memento.tech.backoffice.service.LanguageService;
import com.memento.tech.backoffice.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/backoffice/translation")
@RequiredArgsConstructor
public class TranslationsController {

    private final TranslationService translationService;

    private final LanguageService languageService;

    private final ModelMapper backofficeModelMapper;

    @GetMapping("/lang")
    public List<LanguageDTO> getAllLanguages() {
        return CollectionUtils.emptyIfNull(languageService.getAllLanguages())
                .stream()
                .map(language -> backofficeModelMapper.map(language, LanguageDTO.class))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> saveTranslation(@Valid @RequestBody SaveTranslationDTO translation, @RequestParam(defaultValue = "false") boolean update) {
        var savedTranslation = translationService.saveTranslation(translation.getTranslationCode(), translation.getTranslations(), update);

        if (Objects.nonNull(savedTranslation)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
