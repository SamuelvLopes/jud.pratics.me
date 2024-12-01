package com.ifpe.userApi.DTO;

import com.ifpe.userApi.util.enums.Role;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;

public record UserCreateDTO(
        @NotBlank(message = "Name cannot be blank") String name,
        @NotNull(message = "BirthDate cannot be null") @Past(message = "BirthDate must be in the past") String birthDate,
        @NotBlank(message = "CPF cannot be blank") @Pattern(regexp = "^[0-9]{11}$", message = "CPF must contain 11 digits") String cpf,
        String address,
        @NotBlank(message = "Phone cannot be blank") @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number") String phone,
        @NotBlank @Email(message = "Invalid email format") String email,
        @NotNull(message = "pictureURL cannot be null") @Lob String pictureURL,
        @NotNull(message = "User role cannot be null") Role role,
        @NotNull(message = "User status cannot be null") Boolean isAccountActive
) {
}
