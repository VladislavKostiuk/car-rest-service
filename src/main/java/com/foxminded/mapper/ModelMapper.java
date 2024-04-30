package com.foxminded.mapper;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.model.Car;
import com.foxminded.model.Category;
import com.foxminded.model.Manufacturer;
import com.foxminded.model.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper extends CarListMapper{
    @Mapping(source = "manufacturer", target = "manufacturer", qualifiedByName = "manufacturerDtoToManufacturer")
    @Mapping(source = "cars", target = "cars", qualifiedByName = "carsDtoToCars")
    Model mapToModel(ModelDto modelDto);
    @Mapping(source = "manufacturer", target = "manufacturer", qualifiedByName = "manufacturerToManufacturerDto")
    @Mapping(source = "cars", target = "cars", qualifiedByName = "carsToCarsDto")
    ModelDto mapToModelDto(Model model);

    @Named("manufacturerDtoToManufacturer")
    default Manufacturer manufacturerDtoToManufacturer(ManufacturerDto manufacturerDto) {
        return manufacturerDto == null ? null : new Manufacturer(manufacturerDto.id(),
                manufacturerDto.name(), null);
    }

    @Named("manufacturerToManufacturerDto")
    default ManufacturerDto manufacturerToManufacturerDto(Manufacturer manufacturer) {
        return manufacturer == null ? null :  new ManufacturerDto(manufacturer.getId(),
                manufacturer.getName(), null);
    }
}
