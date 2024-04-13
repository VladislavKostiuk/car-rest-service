package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    public Page<ManufacturerDto> getAllManufacturers() {
//        return manufacturerService.getAllManufacturers();
        return null;
    }

    @GetMapping("/{id}")
    public ManufacturerDto getManufacturer(@PathVariable("id") long id) {
        return manufacturerService.getManufacturerById(id).orElseThrow(() -> new ManufacturerNotFoundException(id));
    }

    @GetMapping("/{id}/models/{model}")
    public ManufacturerDto getManufacturer(@PathVariable("id") long id, @PathVariable("model") String model) {
        return null;
    }

    @PostMapping
    public ManufacturerDto createManufacturer(@RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerService.addManufacturer(manufacturerDto);
    }

    @PutMapping("/{id}")
    public ManufacturerDto updateManufacturer(@PathVariable("id") long id, @RequestBody ManufacturerDto manufacturerDto) {
        return manufacturerService.updateManufacturer(id, manufacturerDto);
    }

    @DeleteMapping("/{id}")
    public void deleteManufacturer(@PathVariable("id") long id) {
        manufacturerService.deleteManufacturerById(id);
    }


}
