package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.VendorDto;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

@Mapper(componentModel = "spring")
public interface DishMapper {
    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    DishEntity dishDtoToEntity(DishCreateDto dishCreateDto);

    VendorEntity vendorDtoToVendorEntity(VendorDto dto);

    VendorDto vendorEntityToVendorDto(VendorEntity entity);

    DishCreateDto dishEntityToDto(DishEntity dishEntity);


}
