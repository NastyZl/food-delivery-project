package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

import java.util.List;
import java.util.Optional;

public interface UserService{
    VendorEntity getVendorByUsername(String username) throws CustomerNotFoundException;

    CustomerEntity getCustomerByUsername(String username) throws CustomerNotFoundException;

    UserEntity registerUser(UserDto userDto);

    Optional<? extends UserEntity> getByUsername(String username);

    List<DishEntity> getAllDishes(String username);

}
