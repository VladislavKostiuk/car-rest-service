package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manufacturers/{manufacturerName}/models/{modelName}/{year}")
public class ManufacturerCarController {
    private final CarService carService;
    private final ManufacturerProvider manufacturerProvider;

    @GetMapping
    public CarDto getManufacturerCar(@PathVariable("manufacturerName") String manufacturerName,
                                     @PathVariable("modelName") String modelName,
                                     @PathVariable("year") int year) {
        return manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
    }

    @PostMapping
    public CarDto addCarToManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                       @PathVariable("modelName") String modelName,
                                       @PathVariable("year") int year,
                                       @RequestBody CarDto car) {
        if (car.year() != year || !car.model().name().equals(modelName)
                || !car.model().manufacturer().name().equals(manufacturerName)) {
            throw new IllegalStateException("The request body data doesn't match url");
        }
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        CarDto newManufacturerCar = new CarDto(car.id(), car.objectId(), car.year(), model, car.categories());
        return carService.addCar(newManufacturerCar);
    }

    @PutMapping
    public CarDto updateManufacturerCar(@PathVariable("manufacturerName") String manufacturerName,
                                        @PathVariable("modelName") String modelName,
                                        @PathVariable("year") int year,
                                        @RequestBody CarDto car) {
        CarDto manufacturerCar = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        CarDto updatedCar = new CarDto(manufacturerCar.id(), car.objectId(),
                car.year(), car.model(), car.categories());
        return carService.updateCar(manufacturerCar.id(), updatedCar);
    }

    @DeleteMapping
    public void deleteCarFromManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                          @PathVariable("modelName") String modelName,
                                          @PathVariable("year") int year) {
        CarDto car = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        carService.updateCar(car.id(), new CarDto(car.id(), car.objectId(), car.year(), null, car.categories()));
    }
}
