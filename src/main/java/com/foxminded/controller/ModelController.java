package com.foxminded.controller;

import com.foxminded.dto.ModelDto;
import com.foxminded.dto.pageimpl.ModelPage;
import com.foxminded.payroll.exception.ModelNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    @Operation(summary = "Get certain amount of models, amount of models is determined by limit parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Models received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelPage.class))})
    })
    @GetMapping
    public ResponseEntity<ModelPage> getModels(@Parameter(description = "page number to be displayed")
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                               @Parameter(description = "limit of cars to be displayed on one page")
                                                   @RequestParam(value = "limit", defaultValue = "3") int limit,
                                               @Parameter(description = "name of field that is used to sort models")
                                                   @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField));
        Page<ModelDto> models = modelService.getAllModels(pageable);
        ModelPage modelPage = new ModelPage(models.getContent(), pageable, models.getContent().size());
        return ResponseEntity.ok(modelPage);
    }

    @Operation(summary = "Get model by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ModelDto> getModel(@Parameter(description = "id of model to be searched")
                                                 @PathVariable("id") long id) {
        ModelDto model = modelService.getModelById(id).orElseThrow(() -> new ModelNotFoundException(id));
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Add new model", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class)))
    })
    @PostMapping
    public ResponseEntity<ModelDto> createModel(@Parameter(description = "new model data")
                                                    @Valid @RequestBody ModelDto modelDto) {
        ModelDto model = modelService.addModel(modelDto);
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Find model by id and then update it", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ModelDto.class))),
            @ApiResponse(responseCode = "404", description = "Model not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ModelDto> updateModel(@Parameter(description = "id of model to be searched")
                                                    @PathVariable("id") long id,
                                                @Parameter(description = "model data that is used for update")
                                                    @RequestBody @Valid ModelDto modelDto) {
        ModelDto model = modelService.updateModel(id, modelDto);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Delete model by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Model deleted successfully",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@Parameter(description = "id of model to be deleted")
                                                @PathVariable("id") long id) {
        modelService.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }
}
