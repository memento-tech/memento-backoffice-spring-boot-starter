package com.memento.tech.backoffice.auth;

import com.memento.tech.backoffice.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackofficeAuthenticationService {

    private final AuthenticationProvider backofficeAuthenticationProvider;

    private final JWTTokenService jwtTokenService;

    private final BackofficeAccessTokenCookieService backofficeAccessTokenCookieService;

    public ResponseEntity<?> authenticate(HttpServletResponse response, LoginRequest loginRequest) {
        if (StringUtils.isBlank(loginRequest.getUsername())) {
            throw new IllegalStateException("Please insert username!");
        }

        Authentication authentication = backofficeAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return setupCookieTokenAuthentication(response, loginRequest.getUsername());
    }

    public void logout(HttpServletResponse response) {
        var blankoBackofficeUserCookie = backofficeAccessTokenCookieService.createBlankoHttpOnlyCookie();
        response.addCookie(blankoBackofficeUserCookie);
    }

    protected ResponseEntity<?> setupCookieTokenAuthentication(HttpServletResponse response, String username) {
        String accessToken = jwtTokenService.generateToken(username);
        var cookie = backofficeAccessTokenCookieService.createHttpOnlyCookie(accessToken);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
