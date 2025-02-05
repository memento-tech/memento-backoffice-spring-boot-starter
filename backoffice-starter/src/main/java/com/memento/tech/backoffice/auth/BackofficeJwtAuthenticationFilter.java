package com.memento.tech.backoffice.auth;

import com.memento.tech.backoffice.auth.AccessTokenCookieService;
import com.memento.tech.backoffice.auth.JWTTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BackofficeJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;

    private final AccessTokenCookieService accessTokenCookieService;

    private final UserDetailsService backofficeUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().equals("/backoffice/login") || request.getServletPath().equals("/api/backoffice/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessTokenCookieService.getBackofficeAccessTokenCookie(request)
                .map(Cookie::getValue)
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(cookieValue -> {
                    var username = "";

                    try {
                        username = jwtTokenService.extractUsername(cookieValue);
                    } catch (ExpiredJwtException exception) {
                        deleteCookieAndClearSecurityContext(response);
                    }

                    if (StringUtils.isNotBlank(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                        Optional.ofNullable(backofficeUserDetailsService.loadUserByUsername(username))
                                .ifPresentOrElse(userDetails -> {
                                    if (jwtTokenService.validateToken(cookieValue, userDetails)) {
                                        var authToken = new UsernamePasswordAuthenticationToken(
                                                userDetails,
                                                null,
                                                userDetails.getAuthorities()
                                        );
                                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                        SecurityContextHolder.getContext().setAuthentication(authToken);
                                    }
                                }, () -> {
                                    // user is removed from database or cookie is messed up for some reason, remove cookie data
                                    deleteCookieAndClearSecurityContext(response);
                                });
                    }
                }, SecurityContextHolder::clearContext);

        filterChain.doFilter(request, response);
    }

    private void deleteCookieAndClearSecurityContext(HttpServletResponse response) {
        var blankoBackofficeUserCookie = accessTokenCookieService.createBlankoHttpOnlyCookie();
        response.addCookie(blankoBackofficeUserCookie);

        SecurityContextHolder.clearContext();
    }
}