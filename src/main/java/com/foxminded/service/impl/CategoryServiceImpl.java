package com.foxminded.service.impl;

import com.foxminded.dto.CategoryDto;
import com.foxminded.mapper.CategoryMapper;
import com.foxminded.model.Category;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.dal.repository.CategoryRepository;
import com.foxminded.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public Optional<CategoryDto> getCategoryById(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::mapToCategoryDto);
    }

    @Override
    public Optional<CategoryDto> getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        return category.map(categoryMapper::mapToCategoryDto);
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.mapToCategory(categoryDto);
        return categoryMapper.mapToCategoryDto(
                categoryRepository.save(new Category(0L, category.getName()))
        );
    }

    @Override
    public CategoryDto updateCategory(long id, CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category updatedCategory = new Category(id, categoryDto.name());
            return categoryMapper.mapToCategoryDto(
                    categoryRepository.save(updatedCategory)
            );
        } else {
            throw new CategoryNotFoundException(id);
        }
    }

    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::mapToCategoryDto);
    }
}
