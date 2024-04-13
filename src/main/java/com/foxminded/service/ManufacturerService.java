package com.foxminded.service;

import com.foxminded.dto.CategoryDto;
import com.foxminded.dto.ManufacturerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {
    Optional<ManufacturerDto> getManufacturerById(long id);
    Optional<ManufacturerDto> getManufacturerByName(String name);
    ManufacturerDto addManufacturer(ManufacturerDto manufacturerDto);
    ManufacturerDto updateManufacturer(long id, ManufacturerDto manufacturerDto);
    void deleteManufacturerById(long id);
    Page<ManufacturerDto> getAllManufacturers(Pageable pageable);
}
