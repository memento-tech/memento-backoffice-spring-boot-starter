package com.memento.tech.backoffice.configuration;

import com.memento.tech.backoffice.repository.BackofficeUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@AutoConfiguration
@ConditionalOnProperty(name = "memento.tech.backoffice.enabled", havingValue = "true")
@RequiredArgsConstructor
public class BackofficeSecurityAutoconfiguration {

    private final BackofficeJwtAuthenticationFilter backofficeJwtAuthenticationFilter;

    private final AuthenticationProvider backofficeAuthenticationProvider;

    private final BackofficeUserRepository backofficeUserRepository;

    @Value("${memento.tech.backoffice.media.mapping}")
    private String mediaMapping;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain backofficeSecurity(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/backoffice/**", "/api/backoffice/**", mediaMapping)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of()); // Allow requests from the React app
                    configuration.setAllowedMethods(List.of());
                    configuration.setAllowedHeaders(List.of("*")); // Allow all headers
                    configuration.setAllowCredentials(false); // Allow credentials
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless session
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/backoffice/**", "/api/backoffice/**", mediaMapping) // Permit all requests to auth and media mapping
                        .permitAll()
                        .anyRequest()
                        .permitAll())
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .formLogin(form -> form
                        .loginPage("/login") // Set custom login page URL
                        .permitAll()
                        .defaultSuccessUrl("/backoffice", true)
                        .failureUrl("/login?error=true")
                )
                .authenticationProvider(backofficeAuthenticationProvider)
                .addFilterBefore(backofficeJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
    }

    @Bean("backofficeUserDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> backofficeUserRepository.findByUsername(username)
                .orElse(null);
    }
}
