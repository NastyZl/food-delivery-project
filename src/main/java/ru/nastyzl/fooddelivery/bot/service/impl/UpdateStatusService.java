package ru.nastyzl.fooddelivery.bot.service.impl;

import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.exception.OrderNotFoundException;
import ru.nastyzl.fooddelivery.service.OrderService;

@Service
public class UpdateStatusService {

    private final OrderService orderService;

    public UpdateStatusService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void sendUpdateStatus(OrderStatus status, Long idOrder)  {
        try {
            orderService.updateStatusOrder(status, idOrder);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
