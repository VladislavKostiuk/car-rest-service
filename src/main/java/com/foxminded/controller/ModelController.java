package com.foxminded.controller;

import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ModelService;
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
@RequestMapping("api/v1/models")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @GetMapping
    public ResponseEntity<Page<ModelDto>> getAllModels(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "limit", defaultValue = "3") int limit,
                                                      @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Page<ModelDto> models = modelService.getAllModels(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField)));
        return ResponseEntity.ok(models);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelDto> getModel(@PathVariable("id") long id) {
        ModelDto model = modelService.getModelById(id).orElseThrow(() -> new ModelNotFoundException(id));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<ModelDto> createModel(@RequestBody ModelDto modelDto) {
        ModelDto model = modelService.addModel(modelDto);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelDto> updateModel(@PathVariable("id") long id, @RequestBody ModelDto modelDto) {
        ModelDto model = modelService.updateModel(id, modelDto);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable("id") long id) {
        modelService.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }
}
