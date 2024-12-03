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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public URI create(UserCreateDTO data) {
        log.info("UserService :: create :: Starting user creation process...");

        validateCreateDTO(data);
        User user = DTOUtil.userCreateDTOToUser(data);
        user.setPassword(passwordEncoder.encode(data.password()));
        log.info("UserService :: create :: User entity created.");

        try {
            user = userRepository.save(user);
            log.info("UserService :: create :: User successfully saved. ID: {}", user.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("UserService :: create :: CPF or Email already exists. DTO: {}", data, e);
            throw new UserCreationException("CPF or Email already exists!", e);
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

        validateId(id);
        Long decryptedId = DTOUtil.decryptId(id);
        User user = userRepository.findById(decryptedId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + id));

        return ResponseEntity.ok(DTOUtil.convertToUserResponseDTO(user));
    }

    @Transactional
    public void updateClient(UserUpdateRequestDTO data) {
        log.info("UserService :: updateClient :: Updating user with ID: {}", data.id());

        validateId(data.id());
        Long decryptedId = DTOUtil.decryptId(data.id());
        User user = userRepository.findById(decryptedId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + data.id()));

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
    }

    @Transactional
    public void deleteById(String encryptedID) {
        log.info("UserService :: deleteById :: Deleting user with ID: {}", encryptedID);

        validateId(encryptedID);
        Long decryptedId = DTOUtil.decryptId(encryptedID);
        userRepository.findById(decryptedId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + encryptedID));

        userRepository.deleteById(decryptedId);
        log.info("UserService :: deleteById :: User successfully deleted. ID: {}", encryptedID);
    }

    @Transactional
    public void changeStatus(String encryptedID) {
        log.info("UserService :: changeStatus :: Changing status of user with ID: {}", encryptedID);

        validateId(encryptedID);
        Long decryptedId = DTOUtil.decryptId(encryptedID);
        User user = userRepository.findById(decryptedId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + encryptedID));

        user.setIsAccountActive(!user.getIsAccountActive());
        userRepository.save(user);

        log.info("UserService :: changeStatus :: User status successfully updated. ID: {}", encryptedID);
    }

    @Transactional(readOnly = true)
    public Boolean accountStatus(String encryptedID) {
        log.info("UserService :: accountStatus :: Searching status of user with ID: {}", encryptedID);
        validateId(encryptedID);
        Long decryptedId = DTOUtil.decryptId(encryptedID);
        User user = userRepository.findById(decryptedId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + encryptedID));
        log.info("UserService :: accountStatus :: User status successfully searched. status: {}", user.getIsAccountActive());

        return user.getIsAccountActive();

    }

    private void validateCreateDTO(UserCreateDTO data) {
        if (data == null) {
            throw new UserCreationException("User data cannot be null.");
        }
        if (!validatePassword(data.password())) {
            throw new IllegalArgumentException("Password must meet the required criteria.");
        }
        DTOUtil.validateUserCreateDTO(data);
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank.");
        }
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.length() <= 12
                && Pattern.compile("[A-Z]").matcher(password).find()
                && Pattern.compile("[a-z]").matcher(password).find()
                && Pattern.compile("[0-9]").matcher(password).find()
                && Pattern.compile("[!@#$%^&*()\\-_=+{};:,<.>?]").matcher(password).find();
    }

}
