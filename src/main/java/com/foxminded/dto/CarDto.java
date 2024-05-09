package com.foxminded.dto;

import java.util.List;

public record CarDto(
        long id,
        String objectId,
        int year,
        ModelDto model,
        List<CategoryDto> categories

) {}
