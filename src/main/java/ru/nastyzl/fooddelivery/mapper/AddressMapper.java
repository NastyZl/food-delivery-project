package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.AddressEntity;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressEntity userDtoToAddressEntity(UserDto userDto);

}

