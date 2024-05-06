package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.service.ManufacturerService;
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
@RequestMapping("api/v1/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<Page<ManufacturerDto>> getAllManufacturers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "limit", defaultValue = "3") int limit,
                                                                    @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Page<ManufacturerDto> manufacturers = manufacturerService.getAllManufacturers(
                PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField)));
        return ResponseEntity.ok(manufacturers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDto> getManufacturer(@PathVariable("id") long id) {
        ManufacturerDto manufacturer = manufacturerService.getManufacturerById(id).orElseThrow(() -> new ManufacturerNotFoundException(id));
        return ResponseEntity.ok(manufacturer);
    }

    @PostMapping
    public ResponseEntity<ManufacturerDto> createManufacturer(@RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto manufacturer = manufacturerService.addManufacturer(manufacturerDto);
        return new ResponseEntity<>(manufacturer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDto> updateManufacturer(@PathVariable("id") long id, @RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto manufacturer = manufacturerService.updateManufacturer(id, manufacturerDto);
        return ResponseEntity.ok(manufacturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable("id") long id) {
        manufacturerService.deleteManufacturerById(id);
        return ResponseEntity.noContent().build();
    }


}
