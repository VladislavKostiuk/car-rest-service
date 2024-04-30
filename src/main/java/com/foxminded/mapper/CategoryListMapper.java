package com.foxminded.mapper;

import com.foxminded.dto.CategoryDto;
import com.foxminded.model.Category;

import java.util.ArrayList;
import java.util.List;

public interface CategoryListMapper {
    default List<Category> categoriesDtoToCategories(List<CategoryDto> categoriesDto) {
        if (categoriesDto == null) {
            return null;
        }

        List<Category> categories = new ArrayList<>();
        for (var categoryDto : categoriesDto) {
            categories.add(new Category(categoryDto.id(), categoryDto.name()));
        }
        return categories;
    }

    default List<CategoryDto> categoriesToCategoriesDto(List<Category> categories) {
        if (categories == null) {
            return null;
        }

        List<CategoryDto> categoriesDto = new ArrayList<>();
        for (var category : categories) {
            categoriesDto.add(new CategoryDto(category.getId(), category.getName()));
        }
        return categoriesDto;
    }
}
