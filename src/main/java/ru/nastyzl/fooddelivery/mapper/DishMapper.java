package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.VendorDto;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

@Mapper(componentModel = "spring")
public interface DishMapper {

    DishEntity dishDtoToEntity(DishCreateDto dishCreateDto);

    VendorEntity vendorDtoToVendorEntity(VendorDto dto);

    DishCreateDto dishEntityToDto(DishEntity dishEntity);

}
