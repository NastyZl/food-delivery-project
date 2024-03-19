package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.bot.TelegramBotService;
import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.exception.OrderNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CartItemEntity;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;
import ru.nastyzl.fooddelivery.repository.OrderItemRepository;
import ru.nastyzl.fooddelivery.repository.OrderRepository;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final TelegramBotService telegramBotService;

    public OrderServiceImpl(UserService userService, OrderItemRepository orderItemRepository, CartService cartService, OrderRepository orderRepository, TelegramBotService telegramBotService) {
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.telegramBotService = telegramBotService;
    }

    @Override
    @Transactional
    public OrderEntity save(OrderDto orderDto) throws UserNotFoundException {
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(orderDto.getCart().getCustomer());
        order.setTotalAmount(orderDto.getCart().getTotalPrise());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setPaymentType(orderDto.getPaymentType());

        CourierEntity courierEntity = userService.chooseCourier();
        order.setCourier(courierEntity);

        order.setCustomerAddress(orderDto.getAddress());

        order.setVendor(cartService.getVendorByCartID(orderDto.getCart().getId()));

        List<OrderItemEntity> orderItems = order.getOrderItems();
        for (CartItemEntity item : orderDto.getCart().getCartItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setDish(item.getDish());
            orderItem.setQuantity(item.getQuantity());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        cartService.deleteCartById(orderDto.getCart().getId());

        OrderEntity save = orderRepository.save(order);

        telegramBotService.sendMessageNewOrder(courierEntity.getChatId(), save);

        return save;
    }

    @Override
    public void updateStatusOrder(OrderStatus status, Long orderId) throws OrderNotFoundException {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setOrderStatus(status);
            if (status.equals(OrderStatus.DONE)) {
                order.setDeliveryDate(LocalDateTime.now());
            }
        } else throw new OrderNotFoundException("Ошибка при смене статуса заказа");

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
