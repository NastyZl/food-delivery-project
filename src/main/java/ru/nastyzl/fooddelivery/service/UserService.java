package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    CourierEntity chooseCourier() throws UserNotFoundException;
    VendorEntity getVendorByUsername(String username) throws UserNotFoundException;

    CustomerEntity getCustomerByUsername(String username) throws UserNotFoundException;

    Optional<CourierEntity> getCourierByUsername(String username);

    UserEntity registerUser(UserDto userDto);

    boolean activateCourier(String username, Long chatId);

    Optional<CourierEntity> findByChatId(Long id);

    Optional<? extends UserEntity> getByUsername(String username);

    List<DishEntity> getAllDishes(String username);

}
