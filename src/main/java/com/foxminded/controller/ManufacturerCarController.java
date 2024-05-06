package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import com.foxminded.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CarDto> getManufacturerCar(@PathVariable("manufacturerName") String manufacturerName,
                                     @PathVariable("modelName") String modelName,
                                     @PathVariable("year") int year) {
        CarDto car = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        return ResponseEntity.ok(car);
    }

    @PostMapping
    public ResponseEntity<CarDto> addCarToManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                       @PathVariable("modelName") String modelName,
                                       @PathVariable("year") int year,
                                       @RequestBody CarDto carDto) {
        if (carDto.year() != year || !carDto.model().name().equals(modelName)
                || !carDto.model().manufacturer().name().equals(manufacturerName)) {
            throw new UrlDoesNotMatchBodyException();
        }
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        CarDto newManufacturerCar = new CarDto(carDto.id(), carDto.objectId(), carDto.year(), model, carDto.categories());
        CarDto car = carService.addCar(newManufacturerCar);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CarDto> updateManufacturerCar(@PathVariable("manufacturerName") String manufacturerName,
                                        @PathVariable("modelName") String modelName,
                                        @PathVariable("year") int year,
                                        @RequestBody CarDto carDto) {
        CarDto manufacturerCar = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        CarDto updatedCar = new CarDto(manufacturerCar.id(), carDto.objectId(),
                carDto.year(), carDto.model(), carDto.categories());
        CarDto car = carService.updateCar(manufacturerCar.id(), updatedCar);
        return ResponseEntity.ok(car);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCarFromManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                          @PathVariable("modelName") String modelName,
                                          @PathVariable("year") int year) {
        CarDto car = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        carService.updateCar(car.id(), new CarDto(car.id(), car.objectId(), car.year(), null, car.categories()));
        return ResponseEntity.noContent().build();
    }
}
