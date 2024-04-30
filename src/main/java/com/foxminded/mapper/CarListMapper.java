package com.foxminded.mapper;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.model.Car;
import com.foxminded.model.Manufacturer;
import com.foxminded.model.Model;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

public interface CarListMapper extends CategoryListMapper {
    @Named("carsDtoToCars")
    default List<Car> carsDtoToCars(List<CarDto> carsDto) {
        if (carsDto == null) {
            return new ArrayList<>();
        }

        List<Car> cars = new ArrayList<>();
        for (var carDto : carsDto) {
            ModelDto modelDto = carDto.model();
            Model model = null;

            if (modelDto != null) {
                ManufacturerDto manufacturerDto = modelDto.manufacturer();
                if (manufacturerDto != null) {
                    model = new Model(modelDto.id(), modelDto.name(),
                            new Manufacturer(manufacturerDto.id(), manufacturerDto.name(), null), null);
                } else {
                    model = new Model(modelDto.id(), modelDto.name(), null, null);
                }
            }

            Car car = new Car(carDto.id(), carDto.objectId(), carDto.year(),
                    model, categoriesDtoToCategories(carDto.categories()));
            cars.add(car);
        }
        return cars;
    }

    @Named("carsToCarsDto")
    default List<CarDto> carsToCarsDto(List<Car> cars) {
        if (cars == null) {
            return new ArrayList<>();
        }

        List<CarDto> carsDto = new ArrayList<>();
        for (var car : cars) {
            Model model = car.getModel();
            ModelDto modelDto = null;
            if (model != null) {
                Manufacturer manufacturer = model.getManufacturer();
                if (manufacturer != null) {
                    modelDto = new ModelDto(model.getId(), model.getName(),
                            new ManufacturerDto(manufacturer.getId(), manufacturer.getName(), null), null);
                } else {
                    modelDto = new ModelDto(model.getId(), model.getName(), null, null);
                }
            }
            CarDto carDto = new CarDto(car.getId(), car.getObjectId(), car.getYear(),
                    modelDto, categoriesToCategoriesDto(car.getCategories()));
            carsDto.add(carDto);
        }
        return carsDto;
    }
}
