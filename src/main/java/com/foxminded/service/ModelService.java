package com.foxminded.service;

import com.foxminded.dto.ModelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ModelService {
    Optional<ModelDto> getModelById(long id);
    Optional<ModelDto> getModelByName(String name);
    ModelDto addModel(ModelDto modelDto);
    ModelDto updateModel(long id, ModelDto modelDto);
    void deleteModelById(long id);
    Page<ModelDto> getAllModels(Pageable pageable);
}
