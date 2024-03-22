package ru.nastyzl.fooddelivery.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramRegistrationService {
    SendMessage process(Update update);
}
