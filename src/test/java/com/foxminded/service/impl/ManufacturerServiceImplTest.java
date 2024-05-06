package com.foxminded.service.impl;

import com.foxminded.dal.repository.ManufacturerRepository;
import com.foxminded.dto.CategoryDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.mapper.ManufacturerMapper;
import com.foxminded.mapper.ManufacturerMapperImpl;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.mapper.ModelMapperImpl;
import com.foxminded.model.Manufacturer;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.service.ManufacturerService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ManufacturerMapperImpl.class, ModelMapperImpl.class})
class ManufacturerServiceImplTest {
    private ManufacturerService manufacturerService;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerMapper manufacturerMapper;
    @Autowired
    private ModelMapper modelMapper;
    private ManufacturerDto manufacturerDto;

    @BeforeEach
    void setup() {
        manufacturerService = new ManufacturerServiceImpl(manufacturerRepository, manufacturerMapper, modelMapper);
        manufacturerDto = new ManufacturerDto(1L, "test", new ArrayList<>());
    }

    @Test
    void testGetManufacturerById_Success() {
        Manufacturer manufacturer = manufacturerMapper.mapToManufacturer(manufacturerDto);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        Optional<ManufacturerDto> actualManufacturerDto = manufacturerService.getManufacturerById(1L);
        assertEquals(Optional.of(manufacturerDto), actualManufacturerDto);
        verify(manufacturerRepository).findById(1L);
    }

    @Test
    void testAddManufacturer_Success() {
        Manufacturer manufacturer = manufacturerMapper.mapToManufacturer(manufacturerDto);
        when(manufacturerRepository.save(any())).thenReturn(manufacturer);
        ManufacturerDto actualManufacturerDto = manufacturerService.addManufacturer(manufacturerDto);
        assertEquals(manufacturerDto, actualManufacturerDto);
        verify(manufacturerRepository).save(any());
    }

    @Test
    void testUpdateManufacturer_Success() {
        ManufacturerDto updatedManufacturerDto = new ManufacturerDto(1L, "updated", new ArrayList<>());
        Manufacturer updatedManufacturer = manufacturerMapper.mapToManufacturer(updatedManufacturerDto);
        Manufacturer manufacturer = manufacturerMapper.mapToManufacturer(manufacturerDto);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(manufacturerRepository.save(any())).thenReturn(updatedManufacturer);
        ManufacturerDto actualManufacturerDto = manufacturerService.updateManufacturer(1L, updatedManufacturerDto);
        assertEquals(updatedManufacturerDto, actualManufacturerDto);
        verify(manufacturerRepository).findById(1L);
        verify(manufacturerRepository).save(any());
    }

    @Test
    void testUpdateManufacturer_ManufacturerNotFound() {
        when(manufacturerRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.updateManufacturer(100L, manufacturerDto));
    }

    @Test
    void testDeleteManufacturerById_Success() {
        manufacturerService.deleteManufacturerById(1L);
        verify(manufacturerRepository).deleteById(1L);
    }

    @Test
    void testGetAllManufacturers_Success() {
        Pageable pageable = PageRequest.of(1,1);
        Manufacturer manufacturer = manufacturerMapper.mapToManufacturer(manufacturerDto);
        when(manufacturerRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(manufacturer), pageable, 1));
        Page<ManufacturerDto> expectedManufacturerDtos = new PageImpl<>(List.of(manufacturerDto), pageable, 1);
        Page<ManufacturerDto> actualManufacturerDtos = manufacturerService.getAllManufacturers(pageable);
        assertEquals(expectedManufacturerDtos, actualManufacturerDtos);
        verify(manufacturerRepository).findAll(pageable);
    }
}