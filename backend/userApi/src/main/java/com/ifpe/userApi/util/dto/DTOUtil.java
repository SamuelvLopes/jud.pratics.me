package com.ifpe.userApi.util.dto;

import com.ifpe.userApi.DTO.chat.ChatCreateDTO;
import com.ifpe.userApi.DTO.user.UserCreateDTO;
import com.ifpe.userApi.DTO.user.UserResponseDTO;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.exceptions.ChatCreationException;
import com.ifpe.userApi.exceptions.DecryptionException;
import com.ifpe.userApi.exceptions.EncryptionException;
import com.ifpe.userApi.exceptions.UserCreationException;
import com.ifpe.userApi.util.encrypt.EncryptUtil;
import com.ifpe.userApi.util.entities.UserUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@UtilityClass
public class DTOUtil {

    public  String encryptUniqueToken(String id) throws EncryptionException {
        try {
            log.info("DTOUtil :: encryptId :: Starting encryption of ID: {}", id);
            return EncryptUtil.encrypt(id);
        } catch (Exception e) {
            log.error("DTOUtil :: encryptId :: Error encrypting ID: {}", id, e);
            throw new EncryptionException("Error encrypting ID", e);
        }
    }

    public  String decryptIdUniqueToken(String encryptedId) throws DecryptionException {
        try {
            log.info("DTOUtil :: decryptId :: Starting decryption of ID: {}", encryptedId);
            return EncryptUtil.decrypt(encryptedId);
        } catch (Exception e) {
            log.error("DTOUtil :: decryptId :: Error decrypting ID: {}", encryptedId, e);
            throw new DecryptionException("Error decrypting ID", e);
        }
    }

    public UserResponseDTO convertToUserResponseDTO(User user) throws EncryptionException {
        log.info("DTOUtil :: convertToUserResponseDTO :: Converting User to UserResponseDTO with encrypted ID.");
        String encryptedUniqueToken = encryptUniqueToken(user.getUniqueToken());

        return new UserResponseDTO(
                encryptedUniqueToken,
                user.getName(),
                user.getEmail(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhone()
        );
    }

    public User userCreateDTOToUser(UserCreateDTO dto) throws Exception {
         LocalDate birthDate = LocalDate.parse(dto.birthDate());

        return User.builder()
               .name(dto.name())
               .birthDate(birthDate)
               .cpf(dto.cpf())
               .email(dto.email())
               .address(dto.address())
               .phone(dto.phone())
               .role(dto.role())
               .pictureURL(dto.pictureURL())
               .isAccountActive(true)
               .uniqueToken(UserUtils.generateUniqueUserToken(
                       dto.name(),
                       dto.cpf(),
                       dto.email()
               ))
               .build();
    }

    public void validateUserCreateDTO(UserCreateDTO data) {
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

    public void validateChatCreateDTO(ChatCreateDTO data) {
        if (data == null) {
            throw new UserCreationException("Chat data cannot be null.");
        }
        if (data.firstId().isBlank()) {
            throw new ChatCreationException("Users id cannot be null");
        }
        if (data.secondId().isBlank()) {
            throw new ChatCreationException("Users id cannot be null");
        }

    }
}
