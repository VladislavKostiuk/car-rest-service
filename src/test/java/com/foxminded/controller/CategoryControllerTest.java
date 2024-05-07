package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CategoryDto;
import com.foxminded.service.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    private static ObjectMapper objectMapper;
    private CategoryDto categoryDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        categoryDto = new CategoryDto(1L, "test");
    }

    @Test
    void testGetAllCategories() throws Exception {
        Pageable pageable = PageRequest.of(0,3);
        PageImpl<CategoryDto> categories = new PageImpl<>(List.of(categoryDto, categoryDto, categoryDto), pageable, 1);
        when(categoryService.getAllCategories(any())).thenReturn(categories);
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testGetCategory_Success() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(categoryDto));
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testGetCategory_CategoryNotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        when(categoryService.addCategory(any())).thenReturn(categoryDto);
        mockMvc.perform(post("/api/v1/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        when(categoryService.updateCategory(anyLong() ,any(CategoryDto.class))).thenReturn(categoryDto);
        mockMvc.perform(put("/api/v1/categories/1")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isNoContent());
    }
}
