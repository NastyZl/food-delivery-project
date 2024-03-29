package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CourierEntity userDtoToCourierEntity(UserDto userDto);

    VendorEntity userDtoToVendorEntity(UserDto userDto);

    CustomerEntity userDtoToCustomerEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity user);
}
