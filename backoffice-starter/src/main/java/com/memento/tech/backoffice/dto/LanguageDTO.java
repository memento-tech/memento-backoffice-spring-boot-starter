package com.memento.tech.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageDTO {

    private String languageCode;

    private String titleCode;

    private boolean defaultLang;

}