package com.foxminded.mapper;


import com.foxminded.dto.ManufacturerDto;
import com.foxminded.dto.ModelDto;
import com.foxminded.model.Manufacturer;
import com.foxminded.model.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends CarListMapper {
    @Mapping(source = "models", target = "models", qualifiedByName = "modelsDtoToModels")
    Manufacturer mapToManufacturer(ManufacturerDto manufacturerDto);
    @Mapping(source = "models", target = "models", qualifiedByName = "modelsToModelsDto")
    ManufacturerDto mapToManufacturerDto(Manufacturer manufacturer);

    @Named("modelsDtoToModels")
    default List<Model> modelsDtoToModels(List<ModelDto> modelsDto) {
        if (modelsDto == null) {
            return new ArrayList<>();
        }

        List<Model> models = new ArrayList<>();
        for (var modelDto : modelsDto) {
            ManufacturerDto manufacturerDto = modelDto.manufacturer();
            Model model;
            if (manufacturerDto != null) {
                model = new Model(modelDto.id(), modelDto.name(),
                        new Manufacturer(manufacturerDto.id(), manufacturerDto.name(), null),
                        carsDtoToCars(modelDto.cars()));
            } else {
                model = new Model(modelDto.id(), modelDto.name(),
                        null, carsDtoToCars(modelDto.cars()));
            }
            models.add(model);
        }
        return models;
    }

    @Named("modelsToModelsDto")
    default List<ModelDto> modelsToModelsDto(List<Model> models) {
        if (models == null) {
            return new ArrayList<>();
        }

        List<ModelDto> modelsDto = new ArrayList<>();
        for (var model : models) {
            Manufacturer manufacturer = model.getManufacturer();
            ModelDto modelDto;
            if (manufacturer != null) {
                modelDto = new ModelDto(model.getId(), model.getName(),
                        new ManufacturerDto(manufacturer.getId(), manufacturer.getName(), null),
                        carsToCarsDto(model.getCars()));
            } else {
                modelDto = new ModelDto(model.getId(), model.getName(),
                        null, carsToCarsDto(model.getCars()));
            }
            modelsDto.add(modelDto);
        }
        return modelsDto;
    }
}
