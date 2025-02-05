package com.memento.tech.backoffice.auth;

import com.memento.tech.backoffice.configuration.BackofficeEnabledCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@AutoConfiguration
@Conditional(BackofficeEnabledCondition.class)
@RequiredArgsConstructor
public class BackofficeSecurityAutoconfiguration {

    private final BackofficeJwtAuthenticationFilter backofficeJwtAuthenticationFilter;

    private final AuthenticationEntryPoint backofficeAuthenticationEntryPoint;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain backofficeSecurity(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/backoffice/**", "/api/backoffice/**", mediaMapping)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of());
                    configuration.setAllowedMethods(List.of());
                    configuration.setAllowedHeaders(List.of());
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/backoffice/login")
                        .permitAll()
                        .requestMatchers("/backoffice/console/**", "/api/backoffice/**", mediaMapping)
                        .authenticated()
                        .anyRequest()
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(backofficeAuthenticationEntryPoint)
                )
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(backofficeJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }
}
