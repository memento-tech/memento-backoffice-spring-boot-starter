package com.memento.tech.backoffice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestTranslationInterceptor implements HandlerInterceptor {

    private final TranslationRequestData translationRequestData;

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler
    ) throws Exception {
        var currentLangIsoCode = Optional.ofNullable(request.getParameter("lang"))
                .filter(StringUtils::isNotBlank)
                .orElse("us");

        translationRequestData.setCurrentLangIsoCode(currentLangIsoCode);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
