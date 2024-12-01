package com.ifpe.userApi.DTO;

import java.time.LocalDate;

public record UserResponseDTO(
        String id,
        String name,
        LocalDate birthDate,
        String cpf,
        String email,
        String address,
        String phone,
        String pictureURL
) {
}
