package com.foxminded.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModelDto(
        @Min(value = 0L, message = "id should be grater than 0")
        long id,
        @NotBlank(message = "name is mandatory")
        String name,
        @NotNull(message = "manufacturer should not be null")
        ManufacturerDto manufacturer,
        @NotNull(message = "cars should not be null")
        List<CarDto> cars
) {}
