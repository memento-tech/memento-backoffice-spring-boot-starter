package com.memento.tech.backoffice.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.memento.tech.backoffice.converter.MediaConverter;
import com.memento.tech.backoffice.converter.TranslationConverter;
import com.memento.tech.backoffice.deserializer.EntityWrapperDeserializer;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.interceptor.RequestTranslationInterceptor;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Conditional(BackofficeEnabledCondition.class)
@ComponentScan(basePackages = "com.memento.tech.starter")
@RequiredArgsConstructor
@EntityScan("com.memento.tech.backoffice.entity")
public class BackofficeAutoconfiguration implements WebMvcConfigurer {

    private final EntitySettingsService entitySettingsService;
    private final EntityService entityService;
    private final PasswordEncoder passwordEncoder;
    private final TranslationConverter translationConverter;
    private final MediaConverter mediaConverter;
    private final ModelMapper modelMapper;
    private final RequestTranslationInterceptor requestTranslationInterceptor;

    @Value("${memento.tech.backoffice.translation.enabled:false}")
    private boolean translationsEnabled;

    @Value("${memento.tech.backoffice.media.file.import.upload.directory}")
    private String uploadDirectory;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @Bean
    public Module customDeserializer() {
        var module = new SimpleModule();
        module.addDeserializer(EntityWrapper.class, new EntityWrapperDeserializer(entitySettingsService, entityService, passwordEncoder));
        return module;
    }

    @PostConstruct
    public void configureModelMapper() {
        if (translationsEnabled) {
            modelMapper.addConverter(translationConverter);
            modelMapper.addConverter(mediaConverter);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        if (translationsEnabled) {
            registry.addInterceptor(requestTranslationInterceptor).addPathPatterns("/**");
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path mediaDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        String mediaPath = mediaDirectory.toUri().toString();

        registry.addResourceHandler(mediaMapping + "/**")
                .addResourceLocations(mediaPath);
    }
}
