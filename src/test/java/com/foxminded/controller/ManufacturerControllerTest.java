package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.service.ManufacturerService;
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

@WebMvcTest(controllers = ManufacturerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManufacturerService manufacturerService;
    private static ObjectMapper objectMapper;
    private ManufacturerDto manufacturerDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        manufacturerDto = new ManufacturerDto(1L, "test", new ArrayList<>());
    }

    @Test
    void testGetManufacturers() throws Exception {
        Pageable pageable = PageRequest.of(0,3);
        PageImpl<ManufacturerDto> manufacturers = new PageImpl<>(List.of(manufacturerDto, manufacturerDto, manufacturerDto), pageable, 1);
        when(manufacturerService.getAllManufacturers(any())).thenReturn(manufacturers);
        mockMvc.perform(get("/api/v1/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testGetManufacturer_Success() throws Exception {
        when(manufacturerService.getManufacturerById(1L)).thenReturn(Optional.of(manufacturerDto));
        mockMvc.perform(get("/api/v1/manufacturers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testGetManufacturer_ManufacturerNotFound() throws Exception {
        when(manufacturerService.getManufacturerById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/manufacturers/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateManufacturer_Success() throws Exception {
        when(manufacturerService.addManufacturer(any())).thenReturn(manufacturerDto);
        mockMvc.perform(post("/api/v1/manufacturers")
                        .content(objectMapper.writeValueAsString(manufacturerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testUpdateManufacturer_Success() throws Exception {
        when(manufacturerService.updateManufacturer(anyLong() ,any())).thenReturn(manufacturerDto);
        mockMvc.perform(put("/api/v1/manufacturers/1")
                        .content(objectMapper.writeValueAsString(manufacturerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    void testDeleteCar_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/1"))
                .andExpect(status().isNoContent());
        verify(manufacturerService).deleteManufacturerById(anyLong());
    }
}
