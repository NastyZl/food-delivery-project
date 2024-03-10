package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity registerUser(UserDto userDto);

    Optional<UserEntity> getByUsername(String username);

}
