package com.memento.tech.backoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveTranslationDTO {

    @NotBlank
    private String translationCode;

    private List<TranslationDTO> translations;

}
