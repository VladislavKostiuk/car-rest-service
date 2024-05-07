package com.foxminded.service.impl;

import com.foxminded.dal.repository.ModelRepository;
import com.foxminded.dto.ModelDto;
import com.foxminded.mapper.CarMapper;
import com.foxminded.mapper.CarMapperImpl;
import com.foxminded.mapper.ManufacturerMapper;
import com.foxminded.mapper.ManufacturerMapperImpl;
import com.foxminded.mapper.ModelMapper;
import com.foxminded.mapper.ModelMapperImpl;
import com.foxminded.model.Model;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ModelService;
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
        ModelMapperImpl.class, ManufacturerMapperImpl.class, CarMapperImpl.class
})
class ModelServiceImplTest {
    private ModelService modelService;

    @Mock
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private CarMapper carMapper;
    private ModelDto modelDto;

    @BeforeEach
    void setup() {
        modelService = new ModelServiceImpl(modelRepository, modelMapper, manufacturerMapper, carMapper);
        modelDto = new ModelDto(1L, "test", null, new ArrayList<>());
    }

    @Test
    void testGetModelById_Success() {
        Model model = modelMapper.mapToModel(modelDto);
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        Optional<ModelDto> actualModelDto = modelService.getModelById(1L);
        assertEquals(Optional.of(modelDto), actualModelDto);
        verify(modelRepository).findById(1L);
    }

    @Test
    void testAddModel_Success() {
        Model model = modelMapper.mapToModel(modelDto);
        when(modelRepository.save(any())).thenReturn(model);
        ModelDto actualModelDto = modelService.addModel(modelDto);
        assertEquals(modelDto, actualModelDto);
        verify(modelRepository).save(any());
    }

    @Test
    void testUpdateModel_Success() {
        ModelDto updatedModelDto = new ModelDto(1L, "updated", null, new ArrayList<>());
        Model updatedModel = modelMapper.mapToModel(updatedModelDto);
        Model model = modelMapper.mapToModel(modelDto);
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(modelRepository.save(any())).thenReturn(updatedModel);
        ModelDto actualModelDto = modelService.updateModel(1L, updatedModelDto);
        assertEquals(updatedModelDto, actualModelDto);
        verify(modelRepository).findById(1L);
        verify(modelRepository).save(any());
    }

    @Test
    void testUpdateModel_ModelNotFound() {
        when(modelRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ModelNotFoundException.class, () -> modelService.updateModel(100L, modelDto));
    }

    @Test
    void testDeleteModelById_Success() {
        modelService.deleteModelById(1L);
        verify(modelRepository).deleteById(1L);
    }

    @Test
    void testGetAllModels_Success() {
        Pageable pageable = PageRequest.of(1,1);
        Model model = modelMapper.mapToModel(modelDto);
        when(modelRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(model), pageable, 1));
        Page<ModelDto> expectedModelDtos = new PageImpl<>(List.of(modelDto), pageable, 1);
        Page<ModelDto> actualModelDtos = modelService.getAllModels(pageable);
        assertEquals(expectedModelDtos, actualModelDtos);
        verify(modelRepository).findAll(pageable);
    }
}
