package com.foxminded.dto;

import java.util.List;

public record ManufacturerDto(
        long id,
        String name,
        List<ModelDto> models
) {}
