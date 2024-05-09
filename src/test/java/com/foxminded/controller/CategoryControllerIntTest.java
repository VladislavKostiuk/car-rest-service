package com.foxminded.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CategoryControllerIntTest {
    private final MockMvc mockMvc;

    @Autowired
    public CategoryControllerIntTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testGetAllCategories_Success() throws Exception {
        mockMvc.perform(get("/api/v1/categories?limit=5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));
    }
}
