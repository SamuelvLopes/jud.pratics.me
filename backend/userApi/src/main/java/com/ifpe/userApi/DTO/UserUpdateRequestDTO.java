package com.ifpe.userApi.DTO;

import java.time.LocalDate;

public record UserUpdateRequestDTO(
        String id,
        String name,
        LocalDate birthDate,
        String address,
        String phone) {

}
