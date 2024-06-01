package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CarDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.service.CarService;
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

@WebMvcTest(controllers = CarController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private CategoryService categoryService;
    private static ObjectMapper objectMapper;
    private CarDto carDto;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void init() {
        carDto = new CarDto(1L, "test", 2024,
                new ModelDto(0, "", null, new ArrayList<>()), new ArrayList<>());
    }

    @Test
    void testGetCars() throws Exception {
        Pageable pageable = PageRequest.of(0,3);
        PageImpl<CarDto> cars = new PageImpl<>(List.of(carDto, carDto, carDto), pageable, 1);
        when(carService.getAllCars(any(), any(), any(), any(), any(), any())).thenReturn(cars);
        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testGetCar_Success() throws Exception {
        when(carService.getCarById(1L)).thenReturn(Optional.of(carDto));
        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("test"))
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    void testGetCar_CarNotFound() throws Exception {
        when(carService.getCarById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateCar_Success() throws Exception {
        when(carService.addCar(any())).thenReturn(carDto);
        mockMvc.perform(post("/api/v1/cars")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("test"))
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    void testUpdateCar_Success() throws Exception {
        when(carService.updateCar(anyLong() ,any())).thenReturn(carDto);
        mockMvc.perform(put("/api/v1/cars/1")
                        .content(objectMapper.writeValueAsString(carDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.objectId").value("test"))
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    void testDeleteCar_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/1"))
                .andExpect(status().isNoContent());
        verify(carService).deleteCarById(anyLong());
    }
}
