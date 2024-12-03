package com.ifpe.userApi.util.dto;

import com.ifpe.userApi.DTO.UserCreateDTO;
import com.ifpe.userApi.DTO.UserResponseDTO;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.exceptions.DecryptionException;
import com.ifpe.userApi.exceptions.EncryptionException;
import com.ifpe.userApi.exceptions.InvalidDateFormatException;
import com.ifpe.userApi.exceptions.UserCreationException;
import com.ifpe.userApi.util.encrypt.EncryptUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@UtilityClass
public class DTOUtil {

    public String encryptId(Long id) throws EncryptionException {
        try {
            log.info("DTOUtil :: encryptId :: Starting encryption of ID: {}", id);
            return EncryptUtil.encrypt(id.toString());
        } catch (Exception e) {
            log.error("DTOUtil :: encryptId :: Error encrypting ID: {}", id, e);
            throw new EncryptionException("Error encrypting ID", e);
        }
    }

    public Long decryptId(String encryptedId) throws DecryptionException {
        try {
            log.info("DTOUtil :: decryptId :: Starting decryption of ID: {}", encryptedId);
            return Long.parseLong(EncryptUtil.decrypt(encryptedId));
        } catch (Exception e) {
            log.error("DTOUtil :: decryptId :: Error decrypting ID: {}", encryptedId, e);
            throw new DecryptionException("Error decrypting ID", e);
        }
    }

    public UserResponseDTO convertToUserResponseDTO(User user) throws EncryptionException {
        log.info("DTOUtil :: convertToUserResponseDTO :: Converting User to UserResponseDTO with encrypted ID.");
        String encryptedId = encryptId(user.getId());

        return new UserResponseDTO(
                encryptedId,
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhone()
        );
    }

    public static User userCreateDTOToUser(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setBirthDate(LocalDate.parse(dto.birthDate()));
        user.setCpf(dto.cpf());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setRole(dto.role());
        user.setPictureURL(dto.pictureURL());
        user.setIsAccountActive(true);
        return user;
    }

    public void  validateUserCreateDTO(UserCreateDTO data) {
        if (data.name() == null || data.name().isEmpty()) {
            log.error("DTOUtil :: validateUserCreateDTO :: Name cannot be empty.");
            throw new UserCreationException("Name cannot be empty.");
        }

        if (data.birthDate() == null || data.birthDate().isBlank()) {
            log.error("DTOUtil :: validateUserCreateDTO :: Birth date cannot be empty.");
            throw new UserCreationException("Birth date cannot be empty.");
        }

        if (data.cpf() == null || data.cpf().isBlank()) {
            log.error("DTOUtil :: validateUserCreateDTO :: CPF cannot be empty.");
            throw new UserCreationException("CPF cannot be empty.");
        }

        if (data.email() == null || data.email().isBlank()) {
            log.error("DTOUtil :: validateUserCreateDTO :: Email cannot be empty.");
            throw new UserCreationException("Email cannot be empty.");
        }
    }

}
