package com.foxminded.service.impl;

import com.foxminded.dal.repository.CarRepository;
import com.foxminded.dto.CarDto;
import com.foxminded.mapper.CarMapper;
import com.foxminded.mapper.CarMapperImpl;
import com.foxminded.mapper.CategoryMapper;
import com.foxminded.mapper.CategoryMapperImpl;
import com.foxminded.mapper.ModelMapperImpl;
import com.foxminded.model.Car;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CarMapperImpl.class, ModelMapperImpl.class, CategoryMapperImpl.class
})
class CarServiceImplTest {
    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private ModelMapperImpl modelMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    private CarDto carDto;
    @BeforeEach
    void setup() {
        carService = new CarServiceImpl(carRepository, carMapper, modelMapper, categoryMapper);
        carDto = new CarDto(1L, "test", 2024, null, new ArrayList<>());
    }

    @Test
    void testGetCarById_Success() {
        Car car = carMapper.mapToCar(carDto);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Optional<CarDto> actualCarDto = carService.getCarById(1L);
        assertEquals(Optional.of(carDto), actualCarDto);
        verify(carRepository).findById(1L);
    }

    @Test
    void testAddCar_Success() {
        Car car = carMapper.mapToCar(carDto);
        when(carRepository.save(any())).thenReturn(car);
        CarDto actualCarDto = carService.addCar(carDto);
        assertEquals(carDto, actualCarDto);
        verify(carRepository).save(any());
    }

    @Test
    void testUpdateCar_Success() {
        CarDto updatedCarDto = new CarDto(1L, "updated", 2024, null, new ArrayList<>());
        Car updatedCar = carMapper.mapToCar(updatedCarDto);
        Car car = carMapper.mapToCar(carDto);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any())).thenReturn(updatedCar);
        CarDto actualCarDto = carService.updateCar(1L, updatedCarDto);
        assertEquals(updatedCarDto, actualCarDto);
        verify(carRepository).findById(1L);
        verify(carRepository).save(any());
    }

    @Test
    void testUpdateCar_CarNotFound() {
        when(carRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.updateCar(100L, carDto));
    }

    @Test
    void testDeleteCarById_Success() {
        carService.deleteCarById(1L);
        verify(carRepository).deleteById(1L);
    }

    @Test
    void testGetAllCars_Success() {
        Pageable pageable = PageRequest.of(1,1);
        Car car = carMapper.mapToCar(carDto);
        when(carRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(car), pageable, 1));
        Page<CarDto> expectedCarDtos = new PageImpl<>(List.of(carDto), pageable, 1);
        Page<CarDto> actualCarDtos = carService.getAllCars(pageable, null,
                null, null, null, null);
        assertEquals(expectedCarDtos, actualCarDtos);
        verify(carRepository).findAll(pageable);
    }

}