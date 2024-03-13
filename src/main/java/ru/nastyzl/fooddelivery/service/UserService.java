package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity registerUser(UserDto userDto);

    Optional<? extends UserEntity> getByUsername(String username);

    List<DishEntity> getAllDishes(String username);

}
