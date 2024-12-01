package com.ifpe.userApi.util.dto;

import com.ifpe.userApi.DTO.UserCreateDTO;
import com.ifpe.userApi.DTO.UserResponseDTO;
import com.ifpe.userApi.entities.User;
import com.ifpe.userApi.exceptions.DecryptionException;
import com.ifpe.userApi.exceptions.EncryptionException;
import com.ifpe.userApi.exceptions.InvalidDateFormatException;
import com.ifpe.userApi.util.encrypt.EncryptUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@UtilityClass
public class DTOUtil {

    public static String encryptId(Long id) throws EncryptionException {
        try {
            log.info("DTOUtil :: encryptId :: Starting encryption of ID: {}", id);
            return EncryptUtil.encrypt(id.toString());
        } catch (Exception e) {
            log.error("DTOUtil :: encryptId :: Error encrypting ID: {}", id, e);
            throw new EncryptionException("Error encrypting ID", e);
        }
    }

    public static Long decryptId(String encryptedId) throws DecryptionException {
        try {
            log.info("DTOUtil :: decryptId :: Starting decryption of ID: {}", encryptedId);
            return Long.parseLong(EncryptUtil.decrypt(encryptedId));
        } catch (Exception e) {
            log.error("DTOUtil :: decryptId :: Error decrypting ID: {}", encryptedId, e);
            throw new DecryptionException("Error decrypting ID", e);
        }
    }

    public static UserResponseDTO convertToUserResponseDTO(User user) throws EncryptionException {
        log.info("DTOUtil :: convertToUserResponseDTO :: Converting User to UserResponseDTO with encrypted ID.");
        String encryptedId = encryptId(user.getId());

        return new UserResponseDTO(
                encryptedId,
                user.getName(),
                user.getBirthDate(),
                user.getCpf(),
                user.getEmail(),
                user.getAddress(),
                user.getPhone(),
                user.getPictureURL()
        );
    }

    public static User userCreateDTOToUser(UserCreateDTO userCreateDTO) {
        log.info("DTOUtil :: userCreateDTOToUser :: Converting UserCreateDTO to User.");

        // Simple validation for mandatory fields
        if (userCreateDTO.name() == null || userCreateDTO.name().isEmpty()) {
            log.error("DTOUtil :: userCreateDTOToUser :: Name cannot be empty.");
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        User user = new User();
        user.setName(userCreateDTO.name());
        try {
            user.setBirthDate(LocalDate.parse(userCreateDTO.birthDate()));
        } catch (DateTimeParseException e) {
            log.error("DTOUtil :: userCreateDTOToUser :: Invalid date format: {}", userCreateDTO.birthDate(), e);
            throw new InvalidDateFormatException("Invalid date format", e);
        }

        user.setCpf(userCreateDTO.cpf());
        user.setAddress(userCreateDTO.address());
        user.setPhone(userCreateDTO.phone());
        user.setEmail(userCreateDTO.email());
        user.setPictureURL(userCreateDTO.pictureURL());
        user.setRole(userCreateDTO.role());

        return user;
    }
}
