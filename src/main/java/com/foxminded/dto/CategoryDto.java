package com.foxminded.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @Min(value = 0L, message = "id should be grater than 0")
        long id,
        @NotBlank(message = "name is mandatory")
        String name
)
{}
