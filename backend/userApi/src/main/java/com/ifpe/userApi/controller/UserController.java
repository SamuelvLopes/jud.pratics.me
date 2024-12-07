package com.ifpe.userApi.controller;

import com.ifpe.userApi.DTO.user.UserCreateDTO;
import com.ifpe.userApi.DTO.user.UserResponseDTO;
import com.ifpe.userApi.DTO.user.UserUpdateRequestDTO;
import com.ifpe.userApi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        log.info("UserController :: createUser :: Received request to create user.");
        URI location = userService.create(userCreateDTO);
        log.info("UserController :: createUser :: User created successfully. Location: {}", location);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("UserController :: getUserById :: Received request to fetch user by ID: {}", id);
        return userService.findByUniqueToken(id);
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequestDTO userUpdateDTO) {
        log.info("UserController :: updateUser :: Received request to update user with ID: {}", userUpdateDTO.id());
        userService.updateClient(userUpdateDTO);
        log.info("UserController :: updateUser :: User updated successfully. ID: {}", userUpdateDTO.id());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("UserController :: deleteUser :: Received request to delete user with ID: {}", id);
        userService.deleteById(id);
        log.info("UserController :: deleteUser :: User deleted successfully. ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeUserStatus(@PathVariable String id) {
        log.info("UserController :: changeUserStatus :: Received request to change status for user with ID: {}", id);
        userService.changeStatus(id);
        log.info("UserController :: changeUserStatus :: User status changed successfully. ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Boolean> accountStatus(@PathVariable String id){
        log.info("UserController :: accountStatus :: searching account status for user with ID: {}",id);
        Boolean status = userService.accountStatus(id);
        log.info("UserController :: accountStatus :: user status sucessfully searched. Status: {}",status);
        return ResponseEntity.ok().body(status);
    }
}
