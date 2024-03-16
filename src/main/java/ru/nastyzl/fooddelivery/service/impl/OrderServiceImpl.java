package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.enums.PaymentType;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.model.*;
import ru.nastyzl.fooddelivery.repository.OrderItemRepository;
import ru.nastyzl.fooddelivery.repository.OrderRepository;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserService userService, OrderItemRepository orderItemRepository, CartService cartService, OrderRepository orderRepository) {
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderEntity save(CartEntity cart) throws CustomerNotFoundException {
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(cart.getCustomer());
        order.setTotalAmount(cart.getTotalPrise());
        order.setOrderStatus(OrderStatus.ACCEPTED);

        order.setPaymentType(PaymentType.CASH);
        order.setCourier((CourierEntity) userService.getByUsername("courier-1").get());
        order.setCustomerAddress("test");
        order.setVendor((VendorEntity) userService.getByUsername("повар").get());

        List<OrderItemEntity> orderItems = order.getOrderItems();
        for(CartItemEntity item: cart.getCartItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setDish(item.getDish());
            orderItem.setQuantity(item.getQuantity());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        cartService.deleteCartById(cart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> findAll(String username) {
        return null;
    }

    @Override
    public List<OrderEntity> findALlOrders() {
        return null;
    }

    @Override
    public OrderEntity acceptOrder(Long id) {
        return null;
    }

    @Override
    public void cancelOrder(Long id) {

    }
}
