package ru.nastyzl.fooddelivery.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nastyzl.fooddelivery.bot.ContainerMessage;
import ru.nastyzl.fooddelivery.bot.TelegramBot;
import ru.nastyzl.fooddelivery.bot.service.NotificationService;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Listener {
    Logger logger = LoggerFactory.getLogger(Listener.class);
    private final NotificationService notificationService;
    private final UserService userService;
    private final TelegramBot telegramBot;

    private final ContainerMessage containerMessage;

    public Listener(NotificationService notificationService, UserService userService, TelegramBot telegramBot, ContainerMessage containerMessage) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.telegramBot = telegramBot;
        this.containerMessage = containerMessage;
    }

    @EventListener
    @Transactional
    public void handleContextRefreshEvent(ContextRefreshedEvent contextStartedEvent) {
        Optional<List<CourierEntity>> allActivatedCourier = userService.findAllActivatedCourier();
        if (allActivatedCourier.isEmpty()) {
            logger.warn("У вас нет ни одного действующего активного курьера! Пользователи не смогу сделать заказ.");
        } else {
            List<CourierEntity> courierEntities = allActivatedCourier.get();
            for (CourierEntity courier : courierEntities) {
                List<OrderEntity> orders = courier.getOrders().stream().filter(orderEntity -> !orderEntity.getOrderStatus().equals(OrderStatus.DONE)).collect(Collectors.toList());
                if (!orders.isEmpty()) {
                    for (OrderEntity order : orders) {
                        notificationService.sendNotification(order);
                    }
                }
            }
        }
    }

    @EventListener
    public void handleContextStopEvent(ContextClosedEvent event) {
        HashMap<Long, HashMap<Long, Long>> containerMessages = containerMessage.getContainerMessages();
        HashMap<Long, List<Long>> chatIdAndMessageId = new HashMap<>();
        containerMessages.forEach((chatId, message) -> {
            List<Long> messagesId = new ArrayList<>(message.keySet());
            chatIdAndMessageId.put(chatId, messagesId);
        });
        chatIdAndMessageId.forEach(this::deleteMessage);
    }

    private void deleteMessage(Long chatId, List<Long> messagesId) {
        for (Long messageId : messagesId) {
            try {
                logger.info("DELETE" + chatId + "  " + messageId);
                telegramBot.execute(new DeleteMessage(String.valueOf(chatId), messageId.intValue()));
            } catch (TelegramApiException e) {
                logger.error("Не удалось удалить сообщения из тг бота");
            }
        }
    }
}
