package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.CartItemEntity;

import java.util.Set;

public interface CartService {
    CartEntity getCart(String username);

    CartEntity addItemToCart(DishDto dishDto, Integer quantity, String username) throws DishNotFoundException, DifferentVendorsException;

    CartEntity updateCart(DishDto dishDto, Integer quantity, String username) throws CustomerNotFoundException;

    CartEntity removeItemFromCart(DishDto dishDto, String username) throws CustomerNotFoundException;
    void deleteCartById(Long id) throws CustomerNotFoundException;
}
