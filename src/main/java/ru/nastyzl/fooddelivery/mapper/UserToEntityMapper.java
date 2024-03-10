package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserToEntityMapper {
    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity userEntity);
}
