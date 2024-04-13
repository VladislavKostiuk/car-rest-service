package com.foxminded.dto;

import java.util.List;

public record ModelDto(
        long id,
        String name,
        ManufacturerDto manufacturer,
        List<CarDto> cars
) {}
