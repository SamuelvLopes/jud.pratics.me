package com.ifpe.userApi.DTO;

import java.time.LocalDate;

public record UserUpdateRequestDTO(
        //id aqui eh na verdade o uniqueToken, o id real fica preservado
        String id,
        String name,
        LocalDate birthDate,
        String address,
        String phone) {

}
