package com.memento.tech.backoffice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@Conditional(BackofficeEnabledCondition.class)
@ComponentScan(basePackages = "com.memento.tech.starter")
@RequiredArgsConstructor
public class BackofficeSecurityAutoconfiguration {

    private final BackofficeJwtAuthenticationFilter backofficeJwtAuthenticationFilter;

    private final AuthenticationProvider backofficeAuthenticationProvider;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @Bean
    public SecurityFilterChain backofficeSecurity(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/backoffice**", "/api/backoffice**", mediaMapping)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://localhost:3000"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "UPDATE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**", mediaMapping)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(sessionConfigurer -> sessionConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .authenticationProvider(backofficeAuthenticationProvider)
                .addFilterBefore(backofficeJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
