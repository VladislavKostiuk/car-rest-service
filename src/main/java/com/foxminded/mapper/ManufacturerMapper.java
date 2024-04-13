package com.foxminded.mapper;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.model.Manufacturer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {
    Manufacturer mapToManufacturer(ManufacturerDto manufacturerDto);
    ManufacturerDto mapToManufacturerDto(Manufacturer manufacturer);
}
