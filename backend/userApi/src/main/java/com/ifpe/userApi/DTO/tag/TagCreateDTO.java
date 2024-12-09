package com.ifpe.userApi.DTO.tag;

import jakarta.validation.constraints.NotBlank;

public record TagCreateDTO(@NotBlank(message = "tag name cant be null or blank") String name) {
}
