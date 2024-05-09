package com.foxminded.service;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CarService {
    Optional<CarDto> getCarById(long id);
    CarDto addCar(CarDto carDto);
    CarDto updateCar(long id, CarDto carDto);
    void deleteCarById(long id);
    Page<CarDto> getAllCars(Pageable pageable, String manufacturer, String model,
                            Integer minYear, Integer maxYear, CategoryDto categoryDto);
}
