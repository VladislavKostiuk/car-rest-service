package com.foxminded.mapper;

import com.foxminded.dto.CarDto;
import com.foxminded.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    Car mapToCar(CarDto carDto);
    CarDto mapToCarDto(Car car);
}
