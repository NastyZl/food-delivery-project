package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.CartNotFoundException;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

public interface CartService {
    void removeDishFromCarts(Long dishId);

    VendorEntity getVendorByCartID(Long id) throws UserNotFoundException;

    CartEntity getCart(String username) throws UserNotFoundException;

    CartEntity addItemToCart(DishShowDto dishShowDto, Integer quantity, String username) throws DishNotFoundException, DifferentVendorsException, UserNotFoundException;

    CartEntity updateCart(DishShowDto dishShowDto, Integer quantity, String username) throws UserNotFoundException;

    CartEntity removeItemFromCart(DishShowDto dishShowDto, String username) throws UserNotFoundException;

    void deleteCartById(Long id) throws CartNotFoundException;
}
