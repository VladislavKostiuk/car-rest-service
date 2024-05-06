package com.foxminded.service.impl;
import com.foxminded.dal.repository.CategoryRepository;
import com.foxminded.dto.CategoryDto;
import com.foxminded.mapper.CategoryMapper;
import com.foxminded.mapper.CategoryMapperImpl;
import com.foxminded.model.Category;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CategoryService;
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
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CategoryMapperImpl.class})
class CategoryServiceImplTest {
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    private CategoryDto categoryDto;

    @BeforeEach
    void setup() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
        categoryDto = new CategoryDto(1L, "test");
    }

    @Test
    void testGetCategoryById_Success() {
        Category category = categoryMapper.mapToCategory(categoryDto);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Optional<CategoryDto> actualCategoryDto = categoryService.getCategoryById(1L);
        assertEquals(Optional.of(categoryDto), actualCategoryDto);
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testAddCategory_Success() {
        Category category = categoryMapper.mapToCategory(categoryDto);
        when(categoryRepository.save(any())).thenReturn(category);
        CategoryDto actualCategoryDto = categoryService.addCategory(categoryDto);
        assertEquals(categoryDto, actualCategoryDto);
        verify(categoryRepository).save(any());
    }

    @Test
    void testUpdateCategory_Success() {
        CategoryDto updatedCategoryDto = new CategoryDto(1L, "updated");
        Category updatedCategory = categoryMapper.mapToCategory(updatedCategoryDto);
        Category category = categoryMapper.mapToCategory(categoryDto);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(updatedCategory);
        CategoryDto actualCategoryDto = categoryService.updateCategory(1L, updatedCategoryDto);
        assertEquals(updatedCategoryDto, actualCategoryDto);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any());
    }

    @Test
    void testUpdateCategory_CategoryNotFound() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(100L, categoryDto));
    }

    @Test
    void testDeleteCategoryById_Success() {
        categoryService.deleteCategoryById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testGetAllCategories_Success() {
        Pageable pageable = PageRequest.of(1,1);
        Category category = categoryMapper.mapToCategory(categoryDto);
        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(category), pageable, 1));
        Page<CategoryDto> expectedCategoryDtos = new PageImpl<>(List.of(categoryDto), pageable, 1);
        Page<CategoryDto> actualCategoryDtos = categoryService.getAllCategories(pageable);
        assertEquals(expectedCategoryDtos, actualCategoryDtos);
        verify(categoryRepository).findAll(pageable);
    }
}