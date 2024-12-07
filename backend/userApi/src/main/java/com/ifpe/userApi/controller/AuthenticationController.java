package com.ifpe.userApi.controller;

import com.ifpe.userApi.infra.security.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


@Slf4j
@RestController
@RequestMapping("/api/v1/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(Authentication authentication) {
        log.info("AuthenticationController :: login :: Request to authenticate user: {}", authentication.getName());
        try {
            String response = authenticationService.authenticate(authentication);
            log.info("AuthenticationController :: login ::ser authenticated successfully: {}", authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("AuthenticationController :: login :: authentication failed for user: {}. Error: {}", authentication.getName(), e.getMessage());
            return ResponseEntity.status(401).body("Authentication failed.");
        }
    }
}
