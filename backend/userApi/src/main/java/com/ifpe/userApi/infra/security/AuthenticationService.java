package com.ifpe.userApi.infra.security;

import com.ifpe.userApi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String authenticate(Authentication authentication) {
        log.info("AuthenticationService :: authenticate :: Authenticating user: {}", authentication.getName());
        String token = jwtService.generateToken(authentication);
        log.info("AuthenticationService :: authenticate :: User authenticated successfully: {}", authentication.getName());
        return token;
    }
}
