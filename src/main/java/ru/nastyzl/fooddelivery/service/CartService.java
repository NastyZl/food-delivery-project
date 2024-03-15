package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.CartEntity;

public interface CartService {
    CartEntity getCart(String username);

    CartEntity addItemToCart(DishDto dishDto, Integer quantity, String username);
}
