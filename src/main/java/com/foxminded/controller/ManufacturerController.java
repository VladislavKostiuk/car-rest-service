package com.foxminded.controller;

import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.pageimpl.ManufacturerPage;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.service.ManufacturerService;
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
@RequestMapping("api/v1/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Operation(summary = "Get certain amount of manufacturers, amount of manufacturers is determined by limit parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manufacturers received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerPage.class))})
    })
    @GetMapping
    public ResponseEntity<ManufacturerPage> getManufacturers(@Parameter(description = "page number to be displayed")
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @Parameter(description = "limit of manufacturers to be displayed on one page")
                                                                @RequestParam(value = "limit", defaultValue = "3") int limit,
                                                             @Parameter(description = "name of field that is used to sort manufacturers")
                                                                @RequestParam(value = "sort", defaultValue = "id") String sortField) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sortField));
        Page<ManufacturerDto> manufacturers = manufacturerService.getAllManufacturers(pageable);
        ManufacturerPage manufacturerPage = new ManufacturerPage(manufacturers.getContent(),
                pageable, manufacturers.getContent().size());
        return ResponseEntity.ok(manufacturerPage);
    }

    @Operation(summary = "Get manufacturer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manufacturer found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerDto.class))),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDto> getManufacturer(@Parameter(description = "id of manufacturer to be searched")
                                                               @PathVariable("id") long id) {
        ManufacturerDto manufacturer = manufacturerService.getManufacturerById(id).orElseThrow(() -> new ManufacturerNotFoundException(id));
        return ResponseEntity.ok(manufacturer);
    }

    @Operation(summary = "Add new manufacturer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manufacturer added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerDto.class)))
    })
    @PostMapping
    public ResponseEntity<ManufacturerDto> createManufacturer(@Parameter(description = "new manufacturer data")
                                                                  @Valid @RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto manufacturer = manufacturerService.addManufacturer(manufacturerDto);
        return new ResponseEntity<>(manufacturer, HttpStatus.CREATED);
    }

    @Operation(summary = "Find manufacturer by id and then update it", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manufacturer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManufacturerDto.class))),
            @ApiResponse(responseCode = "404", description = "Manufacturer not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDto> updateManufacturer(@Parameter(description = "id of manufacturer to be searched")
                                                                  @PathVariable("id") long id,
                                                              @Parameter(description = "manufacturer data that is used for update")
                                                              @Valid @RequestBody ManufacturerDto manufacturerDto) {
        ManufacturerDto manufacturer = manufacturerService.updateManufacturer(id, manufacturerDto);
        return ResponseEntity.ok(manufacturer);
    }

    @Operation(summary = "Delete manufacturer by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manufacturer deleted successfully",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@Parameter(description = "id of manufacturer to be deleted")
                                                       @PathVariable("id") long id) {
        manufacturerService.deleteManufacturerById(id);
        return ResponseEntity.noContent().build();
    }


}
