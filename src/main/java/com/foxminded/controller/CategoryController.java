package com.foxminded.controller;

import com.foxminded.dto.CategoryDto;
import com.foxminded.dto.pageimpl.CategoryPage;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Get certain amount of categories, amount of categories is determined by limit parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryPage.class))})
    })
    @GetMapping
    public ResponseEntity<CategoryPage> getCategories(@Parameter(description = "page number to be displayed")
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @Parameter(description = "limit of categories to be displayed on one page")
                                                          @RequestParam(value = "limit", defaultValue = "3") int limit,
                                                      @Parameter(description = "name of field that is used to sort categories")
                                                          @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField));
        Page<CategoryDto> categories = categoryService.getAllCategories(pageable);
        CategoryPage categoryPage = new CategoryPage(categories.getContent(), pageable, categories.getContent().size());
        return ResponseEntity.ok(categoryPage);
    }

    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@Parameter(description = "id of category to be searched")
                                                       @PathVariable("id") long id) {
        CategoryDto category = categoryService.getCategoryById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Add new category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)))
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Parameter(description = "new category data")
                                                          @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @Operation(summary = "Find category by id and then update it", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Parameter(description = "id of category to be searched")
                                                          @PathVariable("id") long id,
                                                      @Parameter(description = "category data that is used for update")
                                                          @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Delete category by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "id of category to be deleted")
                                                   @PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
