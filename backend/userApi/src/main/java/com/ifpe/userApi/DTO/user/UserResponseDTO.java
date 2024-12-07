package com.ifpe.userApi.DTO.user;

import java.time.LocalDate;

public record UserResponseDTO(
        //id aqui eh na verdade o uniqueToken, o id real fica preservado
        String id,
        String name,
        String email,
        LocalDate birthDate,
        String address,
        String phone
) {
}
