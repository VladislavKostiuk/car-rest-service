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

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public Optional<CategoryDto> getCategoryById(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::mapToCategoryDto);
    }

    @Transactional
    @Override
    public Optional<CategoryDto> getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        return category.map(categoryMapper::mapToCategoryDto);
    }

    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.mapToCategory(categoryDto);
        return categoryMapper.mapToCategoryDto(
                categoryRepository.save(new Category(0L, category.getName(), category.getCars()))
        );
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(long id, CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category updatedCategory = new Category(id, categoryDto.name(), new ArrayList<>());
            return categoryMapper.mapToCategoryDto(
                    categoryRepository.save(updatedCategory)
            );
        } else {
            throw new CategoryNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::mapToCategoryDto);
    }
}
