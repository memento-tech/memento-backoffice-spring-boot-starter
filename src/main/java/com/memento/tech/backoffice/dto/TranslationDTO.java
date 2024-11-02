package com.memento.tech.backoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranslationDTO {

    @NotBlank
    private String langIsoCode;

    @NotBlank
    private String translation;

}
