package com.foxminded.controller;

import com.foxminded.dto.CategoryDto;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAllCategories(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "limit", defaultValue = "3") int limit,
                                                             @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Page<CategoryDto> categories = categoryService.getAllCategories(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField)));
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") long id) {
        CategoryDto category = categoryService.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") long id, @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
