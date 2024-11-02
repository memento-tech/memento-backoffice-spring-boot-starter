package com.memento.tech.backoffice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component("backofficeAuthenticationProvider")
@RequiredArgsConstructor
public class BackofficeAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = (String) authentication.getCredentials();

        var user = userDetailsService.loadUserByUsername(username);

        if (Objects.nonNull(user) && passwordEncoder.matches(password, user.getPassword())) {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return new UsernamePasswordAuthenticationToken(username, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else {
                throw new BadCredentialsException("User does not have the ADMIN role.");
            }
        } else {
            throw new BadCredentialsException("Invalid credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
