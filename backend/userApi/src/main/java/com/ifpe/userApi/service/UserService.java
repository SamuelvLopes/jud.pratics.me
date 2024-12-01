package com.ifpe.userApi.service;

import com.ifpe.userApi.DTO.UserCreateDTO;
import com.ifpe.userApi.DTO.UserResponseDTO;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.repository.UserRepository;
import com.ifpe.userApi.util.dto.DTOUtil;
import com.ifpe.userApi.exceptions.UserCreationException;
import com.ifpe.userApi.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public URI create(UserCreateDTO data) {
        log.info("UserService :: create :: Starting user creation process...");

        if (data == null) {
            log.error("UserService :: create :: Received null UserCreateDTO. Aborting operation.");
            throw new UserCreationException("User data cannot be null.");
        }

        validateUserCreateDTO(data);

        User user = DTOUtil.userCreateDTOToUser(data);
        log.info("UserService :: create :: User entity created.");

        try {
            user = userRepository.save(user);
            log.info("UserService :: create :: User successfully saved. ID: {}", user.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("UserService :: create :: Failed to save user. CPF or Email conflict. DTO: {}", data, e);
            throw new UserCreationException("CPF or Email already exists!", e);
        } catch (Exception e) {
            log.error("UserService :: create :: Unexpected error occurred during user creation.", e);
            throw new RuntimeException("An unexpected error occurred while saving the user.", e);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(DTOUtil.encryptId(user.getId()))
                .toUri();

        log.info("UserService :: create :: User creation process completed. Location: {}", location);

        return location;
    }

    private void validateUserCreateDTO(UserCreateDTO data) {
        if (data.name() == null || data.name().isEmpty()) {
            log.error("UserService :: validateUserCreateDTO :: Name cannot be empty.");
            throw new UserCreationException("Name cannot be empty.");
        }

        if (data.birthDate() == null || data.birthDate().isBlank()) {
            log.error("UserService :: validateUserCreateDTO :: Birth date cannot be empty or blank.");
            throw new UserCreationException("Birth date cannot be empty.");
        }

        if (data.cpf() == null || data.cpf().isBlank()) {
            log.error("UserService :: validateUserCreateDTO :: CPF cannot be empty or blank.");
            throw new UserCreationException("CPF cannot be empty.");
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<UserResponseDTO> findById(String id) {
        log.info("UserService :: findById :: Fetching user by ID: {}", id);

        if (id == null || id.isBlank()) {
            log.error("UserService :: findById :: ID cannot be null or blank.");
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }

        try {
            Long decryptedId = DTOUtil.decryptId(id);
            Optional<User> user = userRepository.findById(decryptedId);

            if (user.isEmpty()) {
                log.error("UserService :: findById :: User not found for ID: {}", id);
                throw new UserNotFoundException("User not found for ID: " + id);
            }

            UserResponseDTO userResponseDTO = DTOUtil.convertToUserResponseDTO(user.get());
            log.info("UserService :: findById :: User found. Returning response.");
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception e) {
            log.error("UserService :: findById :: Error while finding user by ID: {}", id, e);
            throw new RuntimeException("An unexpected error occurred while fetching the user.", e);
        }
    }
}
