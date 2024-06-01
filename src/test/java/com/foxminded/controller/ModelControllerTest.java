package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.service.ModelService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ModelController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelService modelService;
    private static ObjectMapper objectMapper;
    private ModelDto modelDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        modelDto = new ModelDto(1L, "test",
                new ManufacturerDto(0L, "", new ArrayList<>()), new ArrayList<>());
    }

    @Test
    void testGetModels() throws Exception {
        Pageable pageable = PageRequest.of(0,3);
        PageImpl<ModelDto> models = new PageImpl<>(List.of(modelDto, modelDto, modelDto), pageable, 1);
        when(modelService.getAllModels(any())).thenReturn(models);
        mockMvc.perform(get("/api/v1/models"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testGetModel_Success() throws Exception {
        when(modelService.getModelById(1L)).thenReturn(Optional.of(modelDto));
        mockMvc.perform(get("/api/v1/models/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testGetModel_ModelNotFound() throws Exception {
        when(modelService.getModelById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/models/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateModel_Success() throws Exception {
        when(modelService.addModel(any())).thenReturn(modelDto);
        mockMvc.perform(post("/api/v1/models")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testUpdateModel_Success() throws Exception {
        when(modelService.updateModel(anyLong() ,any())).thenReturn(modelDto);
        mockMvc.perform(put("/api/v1/models/1")
                        .content(objectMapper.writeValueAsString(modelDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testDeleteCar_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/models/1"))
                .andExpect(status().isNoContent());
        verify(modelService).deleteModelById(anyLong());
    }
}
