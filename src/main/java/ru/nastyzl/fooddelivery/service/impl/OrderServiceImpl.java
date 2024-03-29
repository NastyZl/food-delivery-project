package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.exception.CartNotFoundException;
import ru.nastyzl.fooddelivery.exception.CourierNotFoundException;
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

/**
 * Service for handling order actions.
 */
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

    /**
     * Save order and delete cart,
     *
     * @param orderDto transfer for order
     * @return saves order
     * @throws UserNotFoundException if there is no user with the courier role in the database
     */

    @Override
    @Transactional
    public OrderEntity save(OrderDto orderDto) throws CartNotFoundException, CourierNotFoundException, UserNotFoundException {
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(orderDto.getCart().getCustomer());
        order.setTotalAmount(orderDto.getCart().getTotalPrise());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setPaymentType(orderDto.getPaymentType());

        CourierEntity courierEntity = userService.chooseCourier();
        courierEntity.addOrder(order);
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

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateStatusOrder(OrderStatus status, Long orderId) throws OrderNotFoundException {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setOrderStatus(status);
            if (status.equals(OrderStatus.DELIVERED)) {
                order.setOrderStatus(status);
            } else if (status.equals(OrderStatus.DONE)) {
                order.setOrderStatus(status);
                order.setDeliveryDate(LocalDateTime.now());
                CourierEntity courier = order.getCourier();
                courier.deleteOrder(orderId);
                if (userService.checkAvailable(courier.getId())) {
                    courier.setAvailability(true);
                }
            } else {
                throw new OrderNotFoundException("неизвесный статус");
            }
            orderRepository.save(order);
        } else throw new OrderNotFoundException("Ошибка при смене статуса заказа");
    }

    @Override
    public List<OrderEntity> findAllForCustomer(String username) throws UserNotFoundException {
        return orderRepository.findAllByCustomerId(userService.getCustomerByUsername(username).getId());
    }

    @Override
    public List<OrderEntity> findAllForVendor(String username) throws UserNotFoundException {
        return orderRepository.findAllByVendorId(userService.getVendorByUsername(username).getId());
    }

    @Override
    public Optional<OrderEntity> findById(Long id) {
        return orderRepository.findById(id);
    }

}
