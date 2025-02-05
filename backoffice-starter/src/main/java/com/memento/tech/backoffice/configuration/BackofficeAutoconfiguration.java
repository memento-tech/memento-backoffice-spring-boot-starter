package com.memento.tech.backoffice.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.memento.tech.backoffice.converter.MediaConverter;
import com.memento.tech.backoffice.converter.TranslationConverter;
import com.memento.tech.backoffice.deserializer.EntityWrapperDeserializer;
import com.memento.tech.backoffice.dto.EntityWrapper;
import com.memento.tech.backoffice.entity.BackofficeUser;
import com.memento.tech.backoffice.interceptor.RequestTranslationInterceptor;
import com.memento.tech.backoffice.repository.BackofficeUserRepository;
import com.memento.tech.backoffice.service.EntityService;
import com.memento.tech.backoffice.service.EntitySettingsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Conditional(BackofficeEnabledCondition.class)
@ComponentScan(basePackages = "com.memento.tech.backoffice")
@RequiredArgsConstructor
@EntityScan("com.memento.tech.backoffice.entity")
@EnableJpaRepositories(basePackages = {"com.memento.tech.backoffice.repository"})
@ConfigurationProperties
@EnableWebSecurity
@Slf4j
public class BackofficeAutoconfiguration implements WebMvcConfigurer {

    private final RequestTranslationInterceptor requestTranslationInterceptor;
    private final BackofficeUserRepository backofficeUserRepository;

    @Value("${memento.tech.backoffice.development.mode:true}")
    private boolean developmentMode;

    @Value("${memento.tech.backoffice.translation.enabled:false}")
    private boolean translationsEnabled;

    @Value("${memento.tech.backoffice.media.file.import.upload.directory}")
    private String uploadDirectory;

    @Value("${memento.tech.backoffice.media.enabled:false}")
    private boolean mediaStorageEnabled;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @PostConstruct
    public void initDevelopmentMode() {
        if (developmentMode) {
            var testUser = BackofficeUser.builder()
                    .username("test")
                    .password(backofficePasswordEncoder().encode("password"))
                    .enabled(true)
                    .build();

            backofficeUserRepository.save(testUser);

            log.info("Backoffice development mode active!");
            log.info("Test backoffice user is created with credentials:");
            log.info("username: test");
            log.info("password: password");
            log.info("NOTE: Please set configuration memento.tech.backoffice.development.mode to false for production environment!");
        }
    }

    @Bean
    public ModelMapper backofficeModelMapper(
            final TranslationConverter translationConverter,
            final MediaConverter mediaConverter
    ) {
        var backofficeModelMapper = new ModelMapper();

        if (translationsEnabled) {
            backofficeModelMapper.addConverter(translationConverter);
        }

        if (mediaStorageEnabled) {
            backofficeModelMapper.addConverter(mediaConverter);
        }

        return backofficeModelMapper;
    }

    @Bean
    public Module customDeserializer(
            final EntitySettingsService entitySettingsService,
            final EntityService entityService
    ) {
        var module = new SimpleModule();
        module.addDeserializer(EntityWrapper.class, new EntityWrapperDeserializer(entitySettingsService, entityService, backofficePasswordEncoder()));
        return module;
    }

    @Bean("backofficeUserDetailsService")
    public UserDetailsService backofficeUserDetailsService() {
        return username -> backofficeUserRepository.findByUsername(username)
                .orElse(null);
    }

    @Bean("backofficePasswordEncoder")
    public PasswordEncoder backofficePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider backofficeAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(backofficeUserDetailsService());
        authProvider.setPasswordEncoder(backofficePasswordEncoder());
        return authProvider;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        if (translationsEnabled) {
            registry.addInterceptor(requestTranslationInterceptor).addPathPatterns("/**");
        }
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        if (mediaStorageEnabled) {
            Path mediaDirectory = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            String mediaPath = mediaDirectory.toUri().toString();

            registry.addResourceHandler(mediaMapping + "/**")
                    .addResourceLocations(mediaPath);
        }
    }
}
