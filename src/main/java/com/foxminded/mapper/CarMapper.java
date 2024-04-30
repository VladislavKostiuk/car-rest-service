package com.foxminded.mapper;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.model.Car;
import com.foxminded.model.Manufacturer;
import com.foxminded.model.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(source = "model", target = "model", qualifiedByName = "modelDtoToModel")
    Car mapToCar(CarDto carDto);
    @Mapping(source = "model", target = "model", qualifiedByName = "modelToModelDto")
    CarDto mapToCarDto(Car car);

    @Named("modelDtoToModel")
    default Model modelDtoToModel(ModelDto modelDto) {
        if (modelDto == null) {
            return null;
        }

        ManufacturerDto manufacturerDto = modelDto.manufacturer();
        return manufacturerDto == null ? new Model(modelDto.id(), modelDto.name(), null, null) :
                new Model(modelDto.id(), modelDto.name(), new Manufacturer(manufacturerDto.id(),
                        manufacturerDto.name(), null), null);
    }

    @Named("modelToModelDto")
    default ModelDto modelToModelDto(Model model) {
        if (model == null) {
            return null;
        }

        Manufacturer manufacturer = model.getManufacturer();
        return manufacturer == null ? new ModelDto(model.getId(), model.getName(), null, null) :
                new ModelDto(model.getId(), model.getName(), new ManufacturerDto(manufacturer.getId(),
                        manufacturer.getName(), null), null);
    }
}
