package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.service.ManufacturerService;
import com.foxminded.service.ModelService;
import lombok.RequiredArgsConstructor;
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
    public ModelDto getManufacturerModel(@PathVariable("manufacturerName") String manufacturerName,
                                         @PathVariable("modelName") String modelName) {
        return manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
    }

    @PostMapping()
    public ModelDto addModelToManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                           @PathVariable("modelName") String modelName,
                                           @RequestBody ModelDto model) {
        if (!model.name().equals(modelName) || !model.manufacturer().name().equals(manufacturerName)) {
            throw new IllegalStateException("The request body data doesn't match url");
        }
        ManufacturerDto manufacturer = manufacturerProvider.getManufacturerByName(manufacturerName);
        ModelDto newManufacturerModel = new ModelDto(model.id(), model.name(), manufacturer, model.cars());
        return modelService.addModel(newManufacturerModel);
    }

    @PutMapping()
    public ModelDto updateManufacturerModel(@PathVariable("manufacturerName") String manufacturerName,
                                            @PathVariable("modelName") String modelName,
                                        @RequestBody ModelDto model) {
        ModelDto manufacturerModel = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        ModelDto updatedModel = new ModelDto(manufacturerModel.id(), model.name(), model.manufacturer(), model.cars());
        return modelService.updateModel(manufacturerModel.id(), updatedModel);
    }

    @DeleteMapping()
    public void deleteModelFromManufacturer(@PathVariable("manufacturerName") String manufacturerName,
                                            @PathVariable("modelName") String modelName) {
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        modelService.updateModel(model.id(), new ModelDto(model.id(), model.name(), null, model.cars()));
    }
}
