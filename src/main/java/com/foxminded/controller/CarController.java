package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CarService;
import com.foxminded.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final CategoryService categoryService;

    @GetMapping
    public Page<CarDto> getAllCars(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "3") int limit,
                                   @RequestParam(value = "sort", defaultValue = "id") String sortField,
                                   @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                   @RequestParam(value = "model", required = false) String model,
                                   @RequestParam(value = "minYear", required = false) Integer minYear,
                                   @RequestParam(value = "maxYear", required = false) Integer maxYear,
                                   @RequestParam(value = "category", required = false) String categoryName) {
        CategoryDto category = null;
        if (categoryName != null) {
            category = categoryService.getCategoryByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryName));
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField));
        return carService.getAllCars(pageable, manufacturer, model, minYear, maxYear, category);
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
}
