package com.foxminded.helper;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.payroll.exception.SeveralCarsFoundException;
import com.foxminded.payroll.exception.SeveralModelsFoundException;
import com.foxminded.service.ManufacturerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ManufacturerProviderTest {
    @InjectMocks
    private ManufacturerProvider manufacturerProvider;
    @Mock
    private ManufacturerService manufacturerService;
    private ManufacturerDto manufacturerDto;
    private ModelDto modelDto;
    private CarDto carDto;

    @BeforeEach
    void setUp() {
        carDto = new CarDto(1L, "car", 2020, modelDto, new ArrayList<>());
        modelDto = new ModelDto(1L, "model", manufacturerDto, List.of(carDto));
        manufacturerDto = new ManufacturerDto(1L, "manufacter", List.of(modelDto));
    }

    @Test
    void testGetManufacturerByName_Success() {
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        ManufacturerDto actualManufacturerDto = manufacturerProvider.getManufacturerByName("manufacturer");
        assertEquals(manufacturerDto, actualManufacturerDto);
    }

    @Test
    void testGetManufacturerByName_ManufacturerNotFound() {
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.empty());
        assertThrows(ManufacturerNotFoundException.class, () -> manufacturerProvider.getManufacturerByName("manufacturer"));
    }

    @Test
    void testGetManufacturerModelByName_Success() {
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        ModelDto actualModelDto = manufacturerProvider.getManufacturerModelByName("manufacturer", "model");
        assertEquals(modelDto, actualModelDto);
    }

    @Test
    void testGetManufacturerModelByName_ModelNotFound() {
        manufacturerDto = new ManufacturerDto(1L, "manufacturer", new ArrayList<>());
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        assertThrows(ModelNotFoundException.class, () ->
                manufacturerProvider.getManufacturerModelByName("manufacturer", "model"));
    }

    @Test
    void testGetManufacturerModelByName_SeveralModelsFound() {
        manufacturerDto = new ManufacturerDto(1L, "manufacturer", List.of(modelDto, modelDto));
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        assertThrows(SeveralModelsFoundException.class, () ->
                manufacturerProvider.getManufacturerModelByName("manufacturer", "model"));
    }

    @Test
    void testGetManufacturerCar_Success() {
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        CarDto actualCarDto = manufacturerProvider.getManufacturerCar("manufacturer", "model", 2020);
        assertEquals(carDto, actualCarDto);
    }

    @Test
    void testGetManufacturerCar_CarNotFound() {
        modelDto = new ModelDto(1L, "model", manufacturerDto, new ArrayList<>());
        manufacturerDto = new ManufacturerDto(1L, "manufacturer", List.of(modelDto));
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        assertThrows(CarNotFoundException.class, () ->
                manufacturerProvider.getManufacturerCar("manufacturer", "model", 2020));
    }

    @Test
    void testGetManufacturerCar_SeveralCarsFound() {
        modelDto = new ModelDto(1L, "model", manufacturerDto, List.of(carDto, carDto));
        manufacturerDto = new ManufacturerDto(1L, "manufacturer", List.of(modelDto));
        when(manufacturerService.getManufacturerByName(any())).thenReturn(Optional.of(manufacturerDto));
        assertThrows(SeveralCarsFoundException.class, () ->
                manufacturerProvider.getManufacturerCar("manufacturer", "model", 2020));
    }
}