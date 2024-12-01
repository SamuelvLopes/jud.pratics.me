package com.ifpe.userApi.controller;

import com.ifpe.userApi.DTO.UserCreateDTO;
import com.ifpe.userApi.DTO.UserResponseDTO;
import com.ifpe.userApi.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO data) {
        try {
            log.info("UserController :: create :: Starting user creation process...");
            URI uri = userService.create(data);
            log.info("UserController :: create :: User successfully created. URI: {}", uri);
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            log.error("UserController :: create :: Error during user creation.", e);
            throw new RuntimeException("An error occurred while creating the user.", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id) {
        try {
            log.info("UserController :: findById :: Request to find client by ID: {}", id);
            return userService.findById(id);
        } catch (Exception e) {
            log.error("UserController :: findById :: Error finding user by ID: {}", id, e);
            throw new RuntimeException("An error occurred while fetching the user.", e);
        }
    }
}
