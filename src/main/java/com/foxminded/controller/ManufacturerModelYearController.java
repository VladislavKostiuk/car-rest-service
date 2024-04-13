package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.CarService;
import com.foxminded.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manufacturers/{manufacturerName}/models/{modelName}/{year}")
public class ManufacturerModelYearController {
    private final ManufacturerService manufacturerService;
    private final CarService carService;

    @ModelAttribute("manufacturer")
    public ManufacturerDto getManufacturer(@PathVariable("manufacturerName") String manufacturerName) {
        return manufacturerService.getManufacturerByName(manufacturerName).orElseThrow(
                () -> new ManufacturerNotFoundException(manufacturerName)
        );
    }

    @ModelAttribute("manufacturerModel")
    public ModelDto getManufacturerModel(@ModelAttribute("manufacturer") ManufacturerDto manufacturer,
                                         @PathVariable("modelName") String modelName) {
        List<ModelDto> models = manufacturer.models()
                .stream()
                .filter(model -> model.name().equals(modelName))
                .toList();

        if (models.isEmpty()) {
            throw new ModelNotFoundException(modelName);
        }

        return models.get(0);
    }

    @ModelAttribute("manufacturerCar")
    public CarDto getManufacturerCar(@ModelAttribute("manufacturerModel") ModelDto model,
                                              @PathVariable("year") int year) {
        List<CarDto> cars = model.cars()
                .stream()
                .filter(car -> car.year() == year)
                .toList();

        if (cars.isEmpty()) {
            throw new CarNotFoundException();
        }
        return cars.get(0);
    }

    @GetMapping
    public CarDto getManufacturerCar(@ModelAttribute("manufacturerCar") CarDto manufacturerCar) {
        return manufacturerCar;
    }

    @PostMapping
    public CarDto addCarToManufacturer(@ModelAttribute("manufacturerModel") ModelDto model,
                                       @PathVariable("modelName") String modelName,
                                       @PathVariable("year") int year) {
        CarDto car = carService.getCarByModelNameAndYear(modelName, year)
                .orElseThrow(CarNotFoundException::new);

        if(!model.cars().contains(car)) {
            model.cars().add(car);
        }

        return car;
    }

    @PutMapping
    public CarDto updateManufacturerCar(@ModelAttribute("manufacturerCar") CarDto manufacturerCar,
                                        @RequestBody CarDto car) {
        CarDto updatedCar = new CarDto(manufacturerCar.id(), car.objectId(),
                car.year(), car.model(), car.categories());
        return carService.updateCar(manufacturerCar.id(), updatedCar);
    }

    @DeleteMapping
    public void deleteCarFromManufacturer(@ModelAttribute("manufacturer") ManufacturerDto manufacturer,
                                          @ModelAttribute("manufacturerModel") ModelDto model,
                                          @ModelAttribute("manufacturerCar") CarDto manufacturerCar) {
        model.cars().remove(manufacturerCar);
        manufacturerService.updateManufacturer(manufacturerCar.id(), manufacturer);
    }
}
