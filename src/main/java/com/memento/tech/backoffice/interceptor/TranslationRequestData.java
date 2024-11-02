package com.memento.tech.backoffice.interceptor;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
public class TranslationRequestData {

    private String currentLangIsoCode;
}
