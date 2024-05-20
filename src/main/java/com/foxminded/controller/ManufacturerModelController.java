package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.helper.ManufacturerProvider;
import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import com.foxminded.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

    @Operation(summary = "Get model from manufacturer by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Several models found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)
    })
    @GetMapping()
    public ResponseEntity<ModelDto> getManufacturerModel(@Parameter(description = "name of manufacturer to be searched")
                                                             @PathVariable("manufacturerName") String manufacturerName,
                                                         @Parameter(description = "name of model to be searched")
                                                             @PathVariable("modelName") String modelName) {
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Add new model to manufacturer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model added to manufacturer successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "The request body data doesn't match url",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found",
                    content = @Content)
    })
    @PostMapping()
    public ResponseEntity<ModelDto> addModelToManufacturer(@Parameter(description = "name of manufacturer to be searched")
                                                               @PathVariable("manufacturerName") String manufacturerName,
                                                           @Parameter(description = "name of model to be added")
                                                               @PathVariable("modelName") String modelName,
                                                           @Valid @RequestBody ModelDto modelDto) {
        if (!modelDto.name().equals(modelName) || !modelDto.manufacturer().name().equals(manufacturerName)) {
            throw new UrlDoesNotMatchBodyException();
        }
        ManufacturerDto manufacturer = manufacturerProvider.getManufacturerByName(manufacturerName);
        ModelDto newManufacturerModel = new ModelDto(modelDto.id(), modelDto.name(), manufacturer, modelDto.cars());
        ModelDto model = modelService.addModel(newManufacturerModel);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Find model in manufacturer by name and then update it",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "400", description = "Several models found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)
    })
    @PutMapping()
    public ResponseEntity<ModelDto> updateManufacturerModel(@Parameter(description = "name of manufacturer to be searched")
                                                                @PathVariable("manufacturerName") String manufacturerName,
                                                            @Parameter(description = "name of model to be updated")
                                                                @PathVariable("modelName") String modelName,
                                                            @Valid @RequestBody ModelDto modelDto) {
        ModelDto manufacturerModel = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        ModelDto updatedModel = new ModelDto(manufacturerModel.id(), modelDto.name(), modelDto.manufacturer(), modelDto.cars());
        ModelDto model = modelService.updateModel(manufacturerModel.id(), updatedModel);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Delete model from manufacturer by name",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Several models found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteModelFromManufacturer(@Parameter(description = "name of manufacturer to be searched")
                                                                @PathVariable("manufacturerName") String manufacturerName,
                                                            @Parameter(description = "name of model to be deleted")
                                                                @PathVariable("modelName") String modelName) {
        ModelDto model = manufacturerProvider.getManufacturerModelByName(manufacturerName, modelName);
        modelService.updateModel(model.id(), new ModelDto(model.id(), model.name(), null, model.cars()));
        return ResponseEntity.noContent().build();
    }
}
