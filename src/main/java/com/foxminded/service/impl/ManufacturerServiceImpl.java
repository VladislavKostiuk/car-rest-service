package com.foxminded.service.impl;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.mapper.ManufacturerMapper;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.model.Manufacturer;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.dal.repository.ManufacturerRepository;
import com.foxminded.service.ManufacturerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;
    private final ModelMapper modelMapper;

    @Override
    public Optional<ManufacturerDto> getManufacturerById(long id) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);
        return manufacturer.map(manufacturerMapper::mapToManufacturerDto);
    }

    @Override
    public Optional<ManufacturerDto> getManufacturerByName(String name) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findByName(name);
        return manufacturer.map(manufacturerMapper::mapToManufacturerDto);
    }

    @Override
    public ManufacturerDto addManufacturer(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = manufacturerMapper.mapToManufacturer(manufacturerDto);
        return manufacturerMapper.mapToManufacturerDto(
                manufacturerRepository.save(new Manufacturer(0L, manufacturer.getName(), manufacturer.getModels()))
        );
    }

    @Override
    public ManufacturerDto updateManufacturer(long id, ManufacturerDto manufacturerDto) {
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);
        if (manufacturer.isPresent()) {
            Manufacturer updatedManufacturer = new Manufacturer(id, manufacturerDto.name(),
                    manufacturerDto.models().stream().map(modelMapper::mapToModel).toList());
            return manufacturerMapper.mapToManufacturerDto(
                    manufacturerRepository.save(updatedManufacturer)
            );
        } else {
            throw new ManufacturerNotFoundException(id);
        }
    }

    @Override
    public void deleteManufacturerById(long id) {
        manufacturerRepository.deleteById(id);
    }

    @Override
    public Page<ManufacturerDto> getAllManufacturers(Pageable pageable) {
        return manufacturerRepository.findAll(pageable).map(manufacturerMapper::mapToManufacturerDto);
    }
}
