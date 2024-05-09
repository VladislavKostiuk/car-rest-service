package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import com.foxminded.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manufacturers/{manufacturerName}/models/{modelName}")
public class ManufacturerModelController {
    private final ModelService modelService;
    private final ManufacturerProvider manufacturerProvider;

    @GetMapping()
    public ResponseEntity<ModelDto> getManufacturerModel(@PathVariable("manufacturerName") String manufacturerName,
                                                        @PathVariable("modelName") String modelName) {
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        return ResponseEntity.ok(model);
    }

    @PostMapping()
    public ResponseEntity<ModelDto> addModelToManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                           @PathVariable("modelName") String modelName,
                                           @RequestBody ModelDto modelDto) {
        if (!modelDto.name().equals(modelName) || !modelDto.manufacturer().name().equals(manufacturerName)) {
            throw new UrlDoesNotMatchBodyException();
        }
        ManufacturerDto manufacturer = manufacturerProvider.getManufacturerByName(manufacturerName);
        ModelDto newManufacturerModel = new ModelDto(modelDto.id(), modelDto.name(), manufacturer, modelDto.cars());
        ModelDto model = modelService.addModel(newManufacturerModel);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<ModelDto> updateManufacturerModel(@PathVariable("manufacturerName") String manufacturerName,
                                            @PathVariable("modelName") String modelName,
                                        @RequestBody ModelDto modelDto) {
        ModelDto manufacturerModel = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        ModelDto updatedModel = new ModelDto(manufacturerModel.id(), modelDto.name(), modelDto.manufacturer(), modelDto.cars());
        ModelDto model = modelService.updateModel(manufacturerModel.id(), updatedModel);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteModelFromManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                            @PathVariable("modelName") String modelName) {
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        modelService.updateModel(model.id(), new ModelDto(model.id(), model.name(), null, model.cars()));
        return ResponseEntity.noContent().build();
    }
}
