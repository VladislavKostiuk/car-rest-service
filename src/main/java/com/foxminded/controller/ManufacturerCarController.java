package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import com.foxminded.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Get car from manufacturer model by year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class))),
            @ApiResponse(responseCode = "400", description = "Several cars found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<CarDto> getManufacturerCar(@Parameter(description = "name of manufacturer to be searched")
                                                         @PathVariable("manufacturerName") String manufacturerName,
                                                     @Parameter(description = "name of model to be searched")
                                                         @PathVariable("modelName") String modelName,
                                                     @Parameter(description = "year of car to be searched")
                                                         @PathVariable("year") int year) {
        CarDto car = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        return ResponseEntity.ok(car);
    }

    @Operation(summary = "Add new car to manufacturer model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car added to manufacturer model successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class))),
            @ApiResponse(responseCode = "400", description = "The request body data doesn't match url",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Several models found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Manufacturer model not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<CarDto> addCarToManufacturer(@Parameter(description = "name of manufacturer to be searched")
                                                           @PathVariable("manufacturerName") String manufacturerName,
                                                       @Parameter(description = "name of model to be searched")
                                                           @PathVariable("modelName") String modelName,
                                                       @Parameter(description = "year of car to be added")
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

    @Operation(summary = "Find car in manufacturer model by year and then update it", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDto.class))),
            @ApiResponse(responseCode = "400", description = "Several cars found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @PutMapping
    public ResponseEntity<CarDto> updateManufacturerCar(@Parameter(description = "name of manufacturer to be searched")
                                                            @PathVariable("manufacturerName") String manufacturerName,
                                                        @Parameter(description = "name of model to be searched")
                                                            @PathVariable("modelName") String modelName,
                                                        @Parameter(description = "year of car to be updated")
                                                            @PathVariable("year") int year,
                                                        @RequestBody CarDto carDto) {
        CarDto manufacturerCar = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        CarDto updatedCar = new CarDto(manufacturerCar.id(), carDto.objectId(),
                carDto.year(), carDto.model(), carDto.categories());
        CarDto car = carService.updateCar(manufacturerCar.id(), updatedCar);
        return ResponseEntity.ok(car);
    }

    @Operation(summary = "Delete car from manufacturer model by year", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Several cars found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Car not found",
                    content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteCarFromManufacturer(@Parameter(description = "name of manufacturer to be searched")
                                                              @PathVariable("manufacturerName") String manufacturerName,
                                                          @Parameter(description = "name of model to be searched")
                                                              @PathVariable("modelName") String modelName,
                                                          @Parameter(description = "year of car to be deleted")
                                                              @PathVariable("year") int year) {
        CarDto car = manufacturerProvider.getManufacturerCar(manufacturerName, modelName, year);
        carService.updateCar(car.id(), new CarDto(car.id(), car.objectId(), car.year(), null, car.categories()));
        return ResponseEntity.noContent().build();
    }
}
