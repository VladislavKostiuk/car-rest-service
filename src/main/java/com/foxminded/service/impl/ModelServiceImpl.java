package com.foxminded.service.impl;

import com.foxminded.dto.ModelDto;
import com.foxminded.mapper.CarMapper;
import com.foxminded.mapper.ManufacturerMapper;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.model.Model;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.dal.repository.ModelRepository;
import com.foxminded.service.ModelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;
    private final ManufacturerMapper manufacturerMapper;
    private final CarMapper carMapper;

    @Transactional
    @Override
    public Optional<ModelDto> getModelById(long id) {
        Optional<Model> model = modelRepository.findById(id);
        return model.map(modelMapper::mapToModelDto);
    }

    @Transactional
    @Override
    public Optional<ModelDto> getModelByName(String name) {
        Optional<Model> model = modelRepository.findByName(name);
        return model.map(modelMapper::mapToModelDto);
    }

    @Transactional
    @Override
    public ModelDto addModel(ModelDto modelDto) {
        Model model = modelMapper.mapToModel(modelDto);
        return modelMapper.mapToModelDto(modelRepository.save(
                new Model(0L, model.getName(), model.getManufacturer(), model.getCars()))
        );
    }

    @Transactional
    @Override
    public ModelDto updateModel(long id, ModelDto modelDto) {
        Optional<Model> model = modelRepository.findById(id);
        if (model.isPresent()) {
            Model updatedModel = new Model(id, modelDto.name(),
                    manufacturerMapper.mapToManufacturer(modelDto.manufacturer()),
                    modelDto.cars().stream().map(carMapper::mapToCar).toList());
            return modelMapper.mapToModelDto(modelRepository.save(updatedModel));
        } else {
            throw new ModelNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public void deleteModelById(long id) {
        modelRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Page<ModelDto> getAllModels(Pageable pageable) {
        return modelRepository.findAll(pageable).map(modelMapper::mapToModelDto);
    }
}
