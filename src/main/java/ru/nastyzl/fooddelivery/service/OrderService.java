package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.exception.CartNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.exception.OrderNotFoundException;
import ru.nastyzl.fooddelivery.model.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderEntity save(OrderDto orderDto) throws UserNotFoundException, CartNotFoundException;

    List<OrderEntity> findAllForCustomer(String username) throws UserNotFoundException;

    List<OrderEntity> findAllForVendor(String username) throws UserNotFoundException;
    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findALlOrders();

    OrderEntity acceptOrder(Long id);

    void cancelOrder(Long id);

    void updateStatusOrder(OrderStatus status, Long orderId) throws OrderNotFoundException;
}
