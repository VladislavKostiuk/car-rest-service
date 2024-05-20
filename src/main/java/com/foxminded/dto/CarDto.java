package com.foxminded.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CarDto(
        @Min(value = 0L, message = "id should be grater than 0")
        long id,
        @NotBlank(message = "objectId is mandatory")
        String objectId,
        @Min(value = 0L, message = "year should be grater than 0")
        int year,
        @NotNull(message = "model should not be null")
        ModelDto model,
        @NotNull(message = "categories should not be null")
        List<CategoryDto> categories

) {}
