package com.memento.tech.backoffice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestTranslationInterceptorTest {

    @InjectMocks
    private RequestTranslationInterceptor requestTranslationInterceptor;

    @Mock
    private TranslationRequestData translationRequestData;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Test
    void testPreHandle_nullLangParam() throws Exception {
        when(request.getParameter("lang")).thenReturn(null);

        requestTranslationInterceptor.preHandle(request, response, handler);

        verify(translationRequestData, times(1)).setCurrentLangIsoCode("us");
    }

    @Test
    void testPreHandle_emptyLangParam() throws Exception {
        when(request.getParameter("lang")).thenReturn("");

        requestTranslationInterceptor.preHandle(request, response, handler);

        verify(translationRequestData, times(1)).setCurrentLangIsoCode("us");
    }

    @Test
    void testPreHandle_validLangParam() throws Exception {
        when(request.getParameter("lang")).thenReturn("rs");

        requestTranslationInterceptor.preHandle(request, response, handler);

        verify(translationRequestData, times(1)).setCurrentLangIsoCode("rs");
    }
}