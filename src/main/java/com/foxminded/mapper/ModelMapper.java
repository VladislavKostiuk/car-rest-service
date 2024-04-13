package com.foxminded.mapper;

import com.foxminded.dto.ModelDto;
import com.foxminded.model.Model;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {
    Model mapToModel(ModelDto modelDto);
    ModelDto mapToModelDto(Model model);
}
