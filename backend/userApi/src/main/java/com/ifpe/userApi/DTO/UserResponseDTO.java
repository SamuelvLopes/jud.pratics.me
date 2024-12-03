package com.ifpe.userApi.DTO;

import java.time.LocalDate;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        LocalDate birthDate,
        String address,
        String phone
) {
}
