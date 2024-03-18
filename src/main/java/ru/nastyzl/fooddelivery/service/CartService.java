package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;

public interface CartService {
    CartEntity getCart(String username) throws CustomerNotFoundException;

    CartEntity addItemToCart(DishShowDto dishShowDto, Integer quantity, String username) throws DishNotFoundException, DifferentVendorsException, CustomerNotFoundException;

    CartEntity updateCart(DishShowDto dishShowDto, Integer quantity, String username) throws CustomerNotFoundException;

    CartEntity removeItemFromCart(DishShowDto dishShowDto, String username) throws CustomerNotFoundException;

    void deleteCartById(Long id) throws CustomerNotFoundException;
}
