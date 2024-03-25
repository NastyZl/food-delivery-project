package ru.nastyzl.fooddelivery.bot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.nastyzl.fooddelivery.bot.service.TelegramRegistrationService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramRegistrationServiceImpl implements TelegramRegistrationService {
    Logger logger = LoggerFactory.getLogger(TelegramRegistrationServiceImpl.class);

    private final UserService userService;

    public TelegramRegistrationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage process(Update update) {
        Message message = update.getMessage();

        logger.info(message.toString());
        if (userService.findCourierByChatId(message.getChatId()).isPresent()) {
            return registerSuccessMessage(message);
        }
        if (message.hasContact()) {
            return handleRegisterIfExist(message);
        }
        return createRegisterMessage(message.getChatId());
    }

    private SendMessage handleRegisterIfExist(Message message) {
        boolean isRegister = userService.findCourierByPhoneNumber(getPhoneNumber(message.getContact())).isPresent();
        if (isRegister) {
            return handleRegister(message);
        }
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Вашего номера нет в базе.")
                .build();
    }


    private String getPhoneNumber(Contact contact) {
        var phoneNumber = contact.getPhoneNumber();
        if (!phoneNumber.startsWith("+"))
            return "+7" + phoneNumber.substring(1);
        return phoneNumber;
    }

    private SendMessage handleRegister(Message message) {
        message.getContact().setPhoneNumber(getPhoneNumber(message.getContact()));
        userService.activateCourier((message.getContact()));
        return registerSuccessMessage(message);
    }

    private SendMessage createRegisterMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Нажми, чтобы подтвердить регистрацию и получать уведомления о заявках.")
                .replyMarkup(getRegistrationKeyboard())
                .build();
    }

    private ReplyKeyboardMarkup getRegistrationKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>(List.of(new KeyboardRow(
                List.of(getRegistrationButton())
        )));
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private KeyboardButton getRegistrationButton() {
        KeyboardButton button = new KeyboardButton("Зарегистрироваться");
        button.setRequestContact(true);
        return button;
    }

    private SendMessage registerSuccessMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Ожидайте заявок на доставку")
                .build();
    }
}
