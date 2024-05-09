package com.foxminded.service.impl;

import com.foxminded.dal.specification.CarSpecification;
import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import com.foxminded.mapper.CarMapper;
import com.foxminded.mapper.CategoryMapper;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.model.Car;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.dal.repository.CarRepository;
import com.foxminded.service.CarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public Optional<CarDto> getCarById(long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(carMapper::mapToCarDto);
    }

    @Transactional
    @Override
    public CarDto addCar(CarDto carDto) {
        Car car = carMapper.mapToCar(carDto);
        return carMapper.mapToCarDto(carRepository.save(
                new Car(0L, car.getObjectId(), car.getYear(),
                        car.getModel(), car.getCategories())));
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteCarById(long id) {
        carRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Page<CarDto> getAllCars(Pageable pageable, String manufacturer, String model,
                                   Integer minYear, Integer maxYear, CategoryDto categoryDto) {
        List<Specification<Car>> allSpecifications = new ArrayList<>();
        if (manufacturer != null) {
            allSpecifications.add(CarSpecification.haveManufacturer(manufacturer));
        }
        if (model != null) {
            allSpecifications.add(CarSpecification.haveModel(model));
        }
        if (minYear != null) {
            allSpecifications.add(CarSpecification.haveYearGreaterThan(minYear));
        }
        if (maxYear != null) {
            allSpecifications.add(CarSpecification.haveYearLessThan(maxYear));
        }
        if (categoryDto != null) {
            allSpecifications.add(CarSpecification.containCategory(categoryMapper.mapToCategory(categoryDto)));
        }

        if (allSpecifications.isEmpty()) {
            return carRepository.findAll(pageable).map(carMapper::mapToCarDto);
        } else {
            Specification<Car> carSpec = Specification.allOf(allSpecifications);
            List<CarDto> cars = carRepository.findAll(carSpec)
                    .stream()
                    .map(carMapper::mapToCarDto)
                    .toList();
            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), cars.size());
            return new PageImpl<>(cars.subList(start, end), pageable, cars.size());
        }
    }
}
