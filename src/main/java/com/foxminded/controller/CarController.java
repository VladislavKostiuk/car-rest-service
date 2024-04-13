package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public Page<CarDto> getAllCars(@RequestParam(value = "manufacturer", required = false) String manufacturer,
                                   @RequestParam(value = "model", required = false) String model,
                                   @RequestParam(value = "minYear", required = false) int minYear,
                                   @RequestParam(value = "maxYear", required = false) int maxYear,
                                   @RequestParam(value = "category", required = false) String category) {
//        List<CarDto> allCars = carService.getAllCars();
//        allCars = filterCarsByManufacturer(allCars, manufacturer);
//        allCars = filterCarsByModel(allCars, model);
//        allCars = filterCarsByMinYear(allCars, minYear);
//        allCars = filterCarsByMaxYear(allCars, maxYear);
//        allCars = filterCarsByCategory(allCars, category);
//
//        return allCars;
        return null;
    }

    @GetMapping("/{id}")
    public CarDto getCar(@PathVariable("id") long id) {
        return carService.getCarById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @PostMapping
    public CarDto createCar(@RequestBody CarDto carDto) {
        return carService.addCar(carDto);
    }

    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable("id") long id, @RequestBody CarDto carDto) {
        return carService.updateCar(id, carDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") long id) {
        carService.deleteCarById(id);
    }

    private List<CarDto> filterCarsByManufacturer(List<CarDto> cars, String manufacturer) {
        return manufacturer == null ? cars : cars.stream()
                .filter(car -> car.model().manufacturer().name().equals(manufacturer))
                .toList();
    }

    private List<CarDto> filterCarsByModel(List<CarDto> cars, String model) {
        return model == null ? cars : cars.stream()
                .filter(car -> car.model().name().equals(model))
                .toList();
    }

    private List<CarDto> filterCarsByMinYear(List<CarDto> cars, int minYear) {
        return minYear == 0 ? cars : cars.stream()
                .filter(car -> car.year() >= minYear)
                .toList();
    }

    private List<CarDto> filterCarsByMaxYear(List<CarDto> cars, int maxYear) {
        return maxYear == 0 ? cars : cars.stream()
                .filter(car -> car.year() <= maxYear)
                .toList();
    }

    private List<CarDto> filterCarsByCategory(List<CarDto> cars, String categoryName) {
        if (categoryName != null) {
            cars.forEach(car -> car.categories().removeIf(category -> category.name().equals(categoryName)));
        }
        return cars;
    }
}
