package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nastyzl.fooddelivery.dto.VendorDto;
import ru.nastyzl.fooddelivery.model.VendorEntity;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    VendorEntity vendorDtoToVendorEntity(VendorDto dto);

    VendorDto vendorEntityToVendorDto(VendorEntity entity);

}