package ru.nastyzl.fooddelivery.bot.service;

import ru.nastyzl.fooddelivery.model.OrderEntity;

public interface NotificationService {
    void sendNotification(OrderEntity order);

}
