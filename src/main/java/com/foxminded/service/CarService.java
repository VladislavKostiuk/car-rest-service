package com.foxminded.service;

import com.foxminded.dto.CarDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CarService {
    Optional<CarDto> getCarById(long id);
    Optional<CarDto> getCarByModelNameAndYear(String modelName, int year);
    CarDto addCar(CarDto carDto);
    CarDto updateCar(long id, CarDto carDto);
    void deleteCarById(long id);
    Page<CarDto> getAllCars(Pageable pageable);
}
