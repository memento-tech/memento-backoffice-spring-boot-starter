package com.memento.tech.backoffice.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public final class BackofficeAccessTokenCookieService {

    @Value("${memento.tech.backoffice.auth.cookie.name:backofficeAccessCookie}")
    private String backofficeUserAccessTokenCookieName;

    @Value("${memento.tech.backoffice.auth.cookie.expiry:0}")
    private int cookieExpiry;

    public Optional<Cookie> getBackofficeAccessTokenCookie(final HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[]{}))
                .filter(cookie -> backofficeUserAccessTokenCookieName.equals(cookie.getName()))
                .findAny();
    }

    public Cookie createHttpOnlyCookie(final String cookieValue) {
        requireNonNull(cookieValue);

        final var cookie = new Cookie(backofficeUserAccessTokenCookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(cookieExpiry);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createBlankoHttpOnlyCookie() {
        final var cookie = new Cookie(backofficeUserAccessTokenCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(1);
        cookie.setPath("/");

        return cookie;
    }
}