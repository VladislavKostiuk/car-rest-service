package com.foxminded.controller;

import com.foxminded.dto.CategoryDto;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Page<CategoryDto> getAllCategories(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "limit", defaultValue = "3") int limit,
                                              @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        return categoryService.getAllCategories(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField)));
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable("id") long id) {
        return categoryService.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable("id") long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
    }
}
