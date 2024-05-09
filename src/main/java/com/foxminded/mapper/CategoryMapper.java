package com.foxminded.mapper;

import com.foxminded.dto.CategoryDto;
import com.foxminded.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(CategoryDto categoryDto);
    CategoryDto mapToCategoryDto(Category category);
}
