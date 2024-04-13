package com.foxminded.service;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<CategoryDto> getCategoryById(long id);
    CategoryDto addCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(long id, CategoryDto categoryDto);
    void deleteCategoryById(long id);
    Page<CategoryDto> getAllCategories(Pageable pageable);
}
