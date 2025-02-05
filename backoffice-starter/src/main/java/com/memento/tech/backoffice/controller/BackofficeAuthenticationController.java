package com.memento.tech.backoffice.controller;

import com.memento.tech.backoffice.auth.BackofficeAuthenticationService;
import com.memento.tech.backoffice.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/backoffice")
@RequiredArgsConstructor
public class BackofficeAuthenticationController {

    private final BackofficeAuthenticationService backofficeAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateBackofficeUser(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        return backofficeAuthenticationService.authenticate(response, loginRequest);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutBackofficeUser(HttpServletResponse response) {
        backofficeAuthenticationService.logout(response);

        return ResponseEntity.ok().build();
    }
}
