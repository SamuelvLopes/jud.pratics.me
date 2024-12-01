package com.ifpe.userApi.service;

import com.ifpe.userApi.DTO.UserCreateDTO;
import com.ifpe.userApi.DTO.UserResponseDTO;
import com.ifpe.userApi.DTO.UserUpdateRequestDTO;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.exceptions.UserCreationException;
import com.ifpe.userApi.exceptions.UserNotFoundException;
import com.ifpe.userApi.repository.UserRepository;
import com.ifpe.userApi.util.dto.DTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

        DTOUtil.validateUserCreateDTO(data);

        User user = DTOUtil.userCreateDTOToUser(data);
        log.info("UserService :: create :: User entity created.");

        try {
            user = userRepository.save(user);
            log.info("UserService :: create :: User successfully saved. ID: {}", user.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("UserService :: create :: CPF or Email already exists. DTO: {}", data, e);
            throw new UserCreationException("CPF or Email already exists!", e);
        } catch (Exception e) {
            log.error("UserService :: create :: Unexpected error during user creation.", e);
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

    @Transactional(readOnly = true)
    public ResponseEntity<UserResponseDTO> findById(String id) {
        log.info("UserService :: findById :: Fetching user by ID: {}", id);

        if (id == null || id.isBlank()) {
            log.error("UserService :: findById :: ID cannot be null or blank.");
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }

        try {
            Long decryptedId = DTOUtil.decryptId(id);
            User user = userRepository.findById(decryptedId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + id));

            UserResponseDTO userResponseDTO = DTOUtil.convertToUserResponseDTO(user);
            log.info("UserService :: findById :: User found. Returning response.");
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception e) {
            log.error("UserService :: findById :: Error while finding user by ID: {}", id, e);
            throw new RuntimeException("An unexpected error occurred while fetching the user.", e);
        }
    }

    @Transactional
    public void updateClient(UserUpdateRequestDTO data) {
        log.info("UserService :: updateClient :: Updating user with ID: {}", data.id());

        if (data.id() == null || data.id().isBlank()) {
            log.error("UserService :: updateClient :: ID cannot be null or blank.");
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }

        try {
            Long decryptedId = DTOUtil.decryptId(data.id());
            User user = userRepository.findById(decryptedId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + data.id()));

            log.info("UserService :: updateClient :: User found: {}", user);

            if (data.name() != null && !data.name().isBlank() && !data.name().equals(user.getName())) {
                user.setName(data.name());
            }
            if (data.birthDate() != null && !data.birthDate().isEqual(user.getBirthDate())) {
                user.setBirthDate(data.birthDate());
            }
            if (data.address() != null && !data.address().isBlank() && !data.address().equals(user.getAddress())) {
                user.setAddress(data.address());
            }
            if (data.phone() != null && !data.phone().isBlank() && !data.phone().equals(user.getPhone())) {
                user.setPhone(data.phone());
            }

            userRepository.save(user);
            log.info("UserService :: updateClient :: User successfully updated. ID: {}", user.getId());
        } catch (Exception e) {
            log.error("UserService :: updateClient :: Unexpected error during user update. ID: {}", data.id(), e);
            throw new RuntimeException("An unexpected error occurred while updating the user.", e);
        }
    }

    @Transactional
    public void deleteById(String encryptedID) {
        log.info("UserService :: deleteById :: Deleting user with ID: {}", encryptedID);

        if (encryptedID == null || encryptedID.isBlank()) {
            log.error("UserService :: deleteById :: ID cannot be null or blank.");
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }

        try {
            Long decryptedId = DTOUtil.decryptId(encryptedID);
            userRepository.findById(decryptedId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + encryptedID));

            userRepository.deleteById(decryptedId);
            log.info("UserService :: deleteById :: User successfully deleted. ID: {}", encryptedID);
        } catch (Exception e) {
            log.error("UserService :: deleteById :: Unexpected error during user deletion. ID: {}", encryptedID, e);
            throw new RuntimeException("An unexpected error occurred while deleting the user.", e);
        }
    }

    @Transactional
    public void changeStatus(String encryptedID) {
        log.info("UserService :: changeStatus :: Changing status of user with ID: {}", encryptedID);

        if (encryptedID == null || encryptedID.isBlank()) {
            log.error("UserService :: changeStatus :: ID cannot be null or blank.");
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }

        try {
            Long decryptedId = DTOUtil.decryptId(encryptedID);
            User user = userRepository.findById(decryptedId)
                    .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + encryptedID));

            user.setIsAccountActive(!user.getIsAccountActive());
            userRepository.save(user);

            log.info("UserService :: changeStatus :: User status successfully updated. ID: {}", encryptedID);
        } catch (Exception e) {
            log.error("UserService :: changeStatus :: Unexpected error during status change. ID: {}", encryptedID, e);
            throw new RuntimeException("An unexpected error occurred while changing the user status.", e);
        }
    }
}
