package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity save(CartEntity cart) throws CustomerNotFoundException;

    List<OrderEntity> findAll(String username);

    List<OrderEntity> findALlOrders();

    OrderEntity acceptOrder(Long id);

    void cancelOrder(Long id);
}
