package com.foxminded.service.impl;

import com.foxminded.dto.CarDto;
import com.foxminded.mapper.CarMapper;
import com.foxminded.mapper.CategoryMapper;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.model.Car;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.repository.CarRepository;
import com.foxminded.service.CarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Optional<CarDto> getCarById(long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(carMapper::mapToCarDto);
    }

    @Override
    public Optional<CarDto> getCarByModelNameAndYear(String modelName, int year) {
        Optional<Car> car = carRepository.findByModel_NameAndYear(modelName, year);
        return car.map(carMapper::mapToCarDto);
    }

    @Override
    public CarDto addCar(CarDto carDto) {
        Car car = carMapper.mapToCar(carDto);
        return carMapper.mapToCarDto(carRepository.save(
                new Car(0L, car.getObjectId(), car.getYear(), car.getModel(), car.getCategories())
                ));
    }

    @Override
    public CarDto updateCar(long id, CarDto carDto) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            Car updatedCar = new Car(id, carDto.objectId(),
                    carDto.year(), modelMapper.mapToModel(carDto.model()),
                    carDto.categories().stream().map(categoryMapper::mapToCategory).toList());
            return carMapper.mapToCarDto(carRepository.save(updatedCar));
        } else {
            throw new CarNotFoundException(id);
        }
    }

    @Override
    public void deleteCarById(long id) {
        carRepository.deleteById(id);
    }

    @Override
    public Page<CarDto> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable).map(carMapper::mapToCarDto);
    }
}
