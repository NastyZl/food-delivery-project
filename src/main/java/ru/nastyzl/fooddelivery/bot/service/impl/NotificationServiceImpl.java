package ru.nastyzl.fooddelivery.bot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.nastyzl.fooddelivery.bot.ContainerMessage;
import ru.nastyzl.fooddelivery.bot.TelegramBot;
import ru.nastyzl.fooddelivery.bot.service.NotificationService;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final TelegramBot telegramBot;
    private final ContainerMessage containerMessage;
    private static final String DELIVERED_BUTTON = "Заказ у меня";
    private static final String DONE_BUTTON = "Завершить заказ";

    public NotificationServiceImpl(TelegramBot telegramBot, ContainerMessage containerMessage) {
        this.telegramBot = telegramBot;
        this.containerMessage = containerMessage;
    }

    public void sendNotification(OrderEntity order) {
        SendMessage message = SendMessage.builder()
                .chatId(order.getCourier().getChatId())
                .text(getNotificationMessage(order))
                .replyMarkup(order.getOrderStatus().equals(OrderStatus.DELIVERED) ? getNotificationKeyboard(DONE_BUTTON) : getNotificationKeyboard(DELIVERED_BUTTON))
                .build();

        containerMessage.addMessages(order.getCourier().getChatId(), telegramBot.sendMessageAndGetId(message).longValue(), order.getId());
    }

    private InlineKeyboardMarkup getNotificationKeyboard(String nameButton) {
        InlineKeyboardButton button = new InlineKeyboardButton(nameButton);
        button.setCallbackData(nameButton);
        return new InlineKeyboardMarkup(List.of(List.of(button)));
    }

    private String getNotificationMessage(OrderEntity order) {
        StringBuilder message = new StringBuilder();
        message.append("Уведомление о новом заказе:\n");
        message.append("ID заказа: ").append(order.getId()).append("\n");
        message.append("Дата заказа: ").append(order.getOrderDate()).append("\n");
        message.append("\n");
        message.append("Покупатель: ").append(order.getCustomer().getFirstName()).append("\n");
        message.append("Телефон: ").append(order.getCustomer().getPhone()).append("\n");
        message.append("Адрес: ").append(order.getCustomerAddress()).append("\n");
        message.append("\n");
        message.append("Повар: ").append(order.getVendor().getFirstName()).append("\n");
        message.append("Телефон: ").append(order.getVendor().getPhone()).append("\n");
        message.append("Адрес: ").append(order.getVendor().getAddress()).append("\n");
        message.append("\n");
        message.append("Список товаров:\n");
        for (OrderItemEntity item : order.getOrderItems()) {
            message.append(getDescription(item)).append("\n");
        }
        message.append("\n");
        message.append("Итоговая сумма заказа: ").append(order.getTotalAmount()).append(" руб.\n");
        message.append("Способ оплаты: ").append(order.getPaymentType().getValue()).append("\n");
        return message.toString();
    }

    private String getDescription(OrderItemEntity orderItem) {
        return "Наименование: " + orderItem.getDish().getDishName() + ", Количество: " + orderItem.getQuantity() + ", Цена: " + orderItem.getDish().getCurrentPrice() + " руб.";
    }
}
