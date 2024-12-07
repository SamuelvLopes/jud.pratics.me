package com.ifpe.userApi.DTO.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatCreateDTO(
        @NotBlank(message = "user ids cant be null or blank") String firstId,
        @NotBlank(message = "user ids cant be null or blank") String secondId
) {
}
