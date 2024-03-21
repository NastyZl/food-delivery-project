package ru.nastyzl.fooddelivery.bot;

import ru.nastyzl.fooddelivery.model.OrderEntity;

public interface NotificationService {
    void sendNotification(OrderEntity order);

}
