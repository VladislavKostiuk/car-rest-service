package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import com.foxminded.service.ManufacturerService;
import com.foxminded.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manufacturers/{manufacturerName}/models/{modelName}")
public class ManufacturerModelController {
    private final ManufacturerService manufacturerService;
    private final ModelService modelService;

    @ModelAttribute("manufacturer")
    public ManufacturerDto getManufacturer(@PathVariable("manufacturerName") String manufacturerName) {
        return manufacturerService.getManufacturerByName(manufacturerName).orElseThrow(
                () -> new ManufacturerNotFoundException(manufacturerName)
        );
    }

    @ModelAttribute("manufacturerModel")
    public ModelDto getManufacturerModel(@ModelAttribute("manufacturer") ManufacturerDto manufacturer,
                                                @PathVariable("modelName") String modelName) {
        List<ModelDto> models = manufacturer.models()
                .stream()
                .filter(model -> model.name().equals(modelName))
                .toList();

        if (models.isEmpty()) {
            throw new ModelNotFoundException(modelName);
        }

        return models.get(0);
    }

    @GetMapping()
    public ModelDto getManufacturerModel(@ModelAttribute("manufacturerModel") ModelDto model) {
        return model;
    }

    @PostMapping()
    public ModelDto addModelToManufacturer(@ModelAttribute("manufacturer") ManufacturerDto manufacturer,
                                           @PathVariable("modelName") String modelName) {
        ModelDto model = modelService.getModelByName(modelName).orElseThrow(
                () -> new ModelNotFoundException(modelName)
        );

        if (!manufacturer.models().contains(model)) {
            manufacturer.models().add(model);
        }

        return model;
    }

    @PutMapping()
    public ModelDto updateManufacturerModel(@ModelAttribute("manufacturerModel") ModelDto manufacturerModel,
                                        @RequestBody ModelDto model) {
        ModelDto updatedModel = new ModelDto(manufacturerModel.id(), model.name(), model.manufacturer(), model.cars());
        return modelService.updateModel(manufacturerModel.id(), updatedModel);
    }

    @DeleteMapping()
    public void deleteModelFromManufacturer(@ModelAttribute("manufacturer") ManufacturerDto manufacturer,
                                            @ModelAttribute("manufacturerModel") ModelDto model) {
        manufacturer.models().remove(model);
        manufacturerService.updateManufacturer(manufacturer.id() ,manufacturer);
    }
}
