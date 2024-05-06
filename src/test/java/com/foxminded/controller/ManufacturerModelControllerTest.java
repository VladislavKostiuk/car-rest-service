package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ManufacturerModelController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ManufacturerModelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModelService modelService;
    @MockBean
    private ManufacturerProvider manufacturerProvider;
    private ObjectMapper objectMapper;
    private ModelDto modelDto;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        modelDto = new ModelDto(1L, "model",
                new ManufacturerDto(1L, "manufacturer", new ArrayList<>()),
                new ArrayList<>());
    }

    @Test
    void testGetManufacturerModel_Success() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenReturn(modelDto);
        mockMvc.perform(get("/api/v1/manufacturers/manufacturer/models/model"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("model"));
    }

    @Test
    void testGetManufacturerModel_ModelNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenThrow(new ModelNotFoundException("model name"));
        mockMvc.perform(get("/api/v1/manufacturers/manufacturer/models/model"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateModel_Success() throws Exception {
        when(modelService.addModel(any())).thenReturn(modelDto);
        mockMvc.perform(post("/api/v1/manufacturers/manufacturer/models/model")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("model"));
    }

    @Test
    void testCreateModel_UrlDoesNotMatchBody() throws Exception {
        mockMvc.perform(post("/api/v1/manufacturers/manufacturer/models/some-model")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateModel_Success() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName(any(), any())).thenReturn(modelDto);
        when(modelService.updateModel(anyLong() ,any())).thenReturn(modelDto);
        mockMvc.perform(put("/api/v1/manufacturers/manufacturer/models/model")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("model"));
    }

    @Test
    void testUpdateModel_ModelNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenThrow(new ModelNotFoundException("model name"));
        mockMvc.perform(put("/api/v1/manufacturers/manufacturer/models/model")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteModelFromManufacturer_Success() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenReturn(modelDto);
        mockMvc.perform(delete("/api/v1/manufacturers/manufacturer/models/model"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteModelFromManufacturer_ModelNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenThrow(new ModelNotFoundException("model name"));
        mockMvc.perform(delete("/api/v1/manufacturers/manufacturer/models/model"))
                .andExpect(status().isNotFound());
    }
}