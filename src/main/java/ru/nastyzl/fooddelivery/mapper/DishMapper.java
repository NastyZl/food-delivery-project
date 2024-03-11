package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.DishEntity;

@Mapper(componentModel = "spring")
public interface DishMapper {
    DishEntity dishDtoToEntity(DishDto dishDto);

    DishDto dishEntityToDto(DishEntity dishEntity);
}
