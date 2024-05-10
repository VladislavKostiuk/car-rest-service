package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import com.foxminded.dto.pageimpl.CarPage;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CarService;
import com.foxminded.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Get certain amount of cars, amount of cars is determined by limit parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cars received",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarPage.class))})
    })
    @GetMapping
    public ResponseEntity<CarPage> getCars(@Parameter(description = "page number to be displayed")
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                           @Parameter(description = "limit of cars to be displayed on one page")
                                               @RequestParam(value = "limit", defaultValue = "3") int limit,
                                           @Parameter(description = "name of field that is used to sort cars")
                                               @RequestParam(value = "sort", defaultValue = "id") String sortField,
                                           @Parameter(description = "manufacturer name that is used to filter cars")
                                               @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                           @Parameter(description = "model name that is used to filter cars")
                                               @RequestParam(value = "model", required = false) String model,
                                           @Parameter(description = "min year that is used to filter cars")
                                               @RequestParam(value = "minYear", required = false) Integer minYear,
                                           @Parameter(description = "max year that is used to filter cars")
                                               @RequestParam(value = "maxYear", required = false) Integer maxYear,
                                           @Parameter(description = "category name that is used to filter cars")
                                               @RequestParam(value = "category", required = false) String categoryName) {
        CategoryDto category = null;
        if (categoryName != null) {
            category = categoryService.getCategoryByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryName));
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField));
        Page<CarDto> cars = carService.getAllCars(pageable, manufacturer, model, minYear, maxYear, category);
        CarPage carPage = new CarPage(cars.getContent(), pageable, cars.getContent().size());
        return ResponseEntity.ok(carPage);
    }

    @Operation(summary = "Get car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@Parameter(description = "id of car to be searched")
                                             @PathVariable("id") long id) {
        CarDto car = carService.getCarById(id).orElseThrow(() -> new CarNotFoundException(id));
        return ResponseEntity.ok(car);
    }

    @Operation(summary = "Add new car", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class)))
    })
    @PostMapping
    public ResponseEntity<CarDto> createCar(@Parameter(description = "new car data")
                                                @RequestBody CarDto carDto) {
        CarDto car = carService.addCar(carDto);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @Operation(summary = "Find car by id and then update it", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class))),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@Parameter(description = "id of car to be searched")
                                                @PathVariable("id") long id,
                                            @Parameter(description = "car data that is used for update") @RequestBody CarDto carDto) {
        CarDto car = carService.updateCar(id, carDto);
        return ResponseEntity.ok(car);
    }

    @Operation(summary = "Delete car by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@Parameter(description = "id of car to be deleted")
                                              @PathVariable("id") long id) {
        carService.deleteCarById(id);
        return ResponseEntity.noContent().build();
    }
}
