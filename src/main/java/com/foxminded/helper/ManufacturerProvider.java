package com.foxminded.helper;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ManufacturerProvider {
    private final ManufacturerService manufacturerService;

    public ManufacturerDto getManufacturerByName(String manufacturerName) {
        return manufacturerService.getManufacturerByName(manufacturerName).orElseThrow(
                () -> new ManufacturerNotFoundException(manufacturerName)
        );
    }

    public ModelDto getManufacturerModelByName(String manufacturerName, String modelName) {
        ManufacturerDto manufacturer = getManufacturerByName(manufacturerName);
        List<ModelDto> models = manufacturer.models()
                .stream()
                .filter(model -> model.name().equals(modelName))
                .toList();

        if (models.isEmpty()) {
            throw new ModelNotFoundException(modelName);
        } else if (models.size() > 1) {
            throw new IllegalStateException("Two or more models were found with the same name");
        }

        return models.get(0);
    }

    public CarDto getManufacturerCar(String manufacturerName, String modelName, int year) {
        ModelDto model = getManufacturerModelByName(manufacturerName, modelName);
        List<CarDto> cars = model.cars()
                .stream()
                .filter(car -> car.year() == year)
                .toList();

        if (cars.isEmpty()) {
            throw new CarNotFoundException();
        } else if (cars.size() > 1) {
            throw new IllegalStateException("Two or more cars were found with the same model and year");
        }

        return cars.get(0);
    }
}
