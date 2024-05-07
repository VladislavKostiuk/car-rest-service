package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.service.CarService;
import org.junit.jupiter.api.BeforeAll;
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

@WebMvcTest(controllers = ManufacturerCarController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ManufacturerCarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private ManufacturerProvider manufacturerProvider;
    private static ObjectMapper objectMapper;
    private ModelDto modelDto;
    private CarDto carDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        ManufacturerDto manufacturerDto = new ManufacturerDto(1L, "manufacturer", new ArrayList<>());
        modelDto = new ModelDto(1L, "model", manufacturerDto, new ArrayList<>());
        carDto = new CarDto(1L, "car", 2020, modelDto, new ArrayList<>());
    }

    @Test
    void testGetManufacturerCar_Success() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenReturn(carDto);
        mockMvc.perform(get("/api/v1/manufacturers/manufacturer/models/model/2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("car"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    void testGetManufacturerCar_CarNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenThrow(new CarNotFoundException());
        mockMvc.perform(get("/api/v1/manufacturers/manufacturer/models/model/2020"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCar_Success() throws Exception {
        when(manufacturerProvider.getManufacturerModelByName("manufacturer",
                "model")).thenReturn(modelDto);
        when(carService.addCar(any())).thenReturn(carDto);
        mockMvc.perform(post("/api/v1/manufacturers/manufacturer/models/model/2020")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("car"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    void testCreateCar_UrlDoesNotMatchBody() throws Exception {
        mockMvc.perform(post("/api/v1/manufacturers/manufacturer/models/model/2222")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCar_Success() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenReturn(carDto);
        when(carService.updateCar(anyLong() ,any())).thenReturn(carDto);
        mockMvc.perform(put("/api/v1/manufacturers/manufacturer/models/model/2020")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("car"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    void testUpdateCar_CarNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenThrow(new CarNotFoundException());
        mockMvc.perform(put("/api/v1/manufacturers/manufacturer/models/model/2020")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCarFromManufacturer_Success() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenReturn(carDto);
        mockMvc.perform(delete("/api/v1/manufacturers/manufacturer/models/model/2020"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteModelFromManufacturer_ModelNotFound() throws Exception {
        when(manufacturerProvider.getManufacturerCar("manufacturer",
                "model", 2020)).thenThrow(new CarNotFoundException());
        mockMvc.perform(delete("/api/v1/manufacturers/manufacturer/models/model/2020"))
                .andExpect(status().isNotFound());
    }
}
