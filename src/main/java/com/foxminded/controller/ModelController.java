package com.foxminded.controller;

import com.foxminded.dto.CarDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @GetMapping
    public Page<ModelDto> getAllModels(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "3") int limit,
                                       @RequestParam(value = "sort", defaultValue = "id") String sortField) {

        return modelService.getAllModels(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField)));
    }

    @GetMapping("/{id}")
    public ModelDto getModel(@PathVariable("id") long id) {
        return modelService.getModelById(id).orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping
    public ModelDto createModel(@RequestBody ModelDto modelDto) {
        return modelService.addModel(modelDto);
    }

    @PutMapping("/{id}")
    public ModelDto updateModel(@PathVariable("id") long id, @RequestBody ModelDto modelDto) {
        return modelService.updateModel(id, modelDto);
    }

    @DeleteMapping("/{id}")
    public void deleteModel(@PathVariable("id") long id) {
        modelService.deleteModelById(id);
    }
}
