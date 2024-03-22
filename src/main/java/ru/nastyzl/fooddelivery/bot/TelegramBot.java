package ru.nastyzl.fooddelivery.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final ContainerMessage containerMessage;
    private final UpdateStatusService updateStatusService;
    private final TelegramRegistrationService telegramRegistrationService;
    private static final String DELIVERED_BUTTON = "Заказ у меня";

    private static final String DONE_BUTTON = "Завершить заказ";

    private static final String HELP_TEXT = "Здесь будет справочная информация для вас! Позже...";


    @Value("${bot.name}")
    private String name;

    public TelegramBot(@Value("${bot.token}") String token, ContainerMessage containerMessage, UpdateStatusService updateStatusService, TelegramRegistrationService telegramRegistrationService) {
        super(token);
        this.containerMessage = containerMessage;
        this.updateStatusService = updateStatusService;
        this.telegramRegistrationService = telegramRegistrationService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/orders", "мои заказы"));
        listOfCommands.add(new BotCommand("/help", "справочная информация"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logger.error("Ошибка при загрузке команд для бота: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            if (update.getMessage().hasContact()) {
                sendMessage(telegramRegistrationService.process(update));
            } else if (update.getMessage().hasText()) {

                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                switch (messageText) {
                    case "/orders":
                        break;
                    case "/help":
                        sendMessage(chatId, HELP_TEXT);
                        break;
                    default:
                        sendMessage(telegramRegistrationService.process(update));
                }
            }
        } else if (update.hasCallbackQuery()) {

            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals(DELIVERED_BUTTON)) {
                EditMessageReplyMarkup editMessageReplyMarkup = updateDeliveredButtonToDone(chatId, messageId, DONE_BUTTON);
                try {
                    execute(editMessageReplyMarkup);
                    updateStatusService.sendUpdateStatus(OrderStatus.DELIVERED, containerMessage.findMessage(chatId, messageId));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            } else if (callBackData.equals(DONE_BUTTON)) {
                EditMessageText editMessageText = EditMessageText.builder()
                        .chatId(chatId)
                        .text("Заказ завершен! Продолжайте в том же духе!")
                        .messageId((int) messageId)
                        .build();

                sendEditMessageText(editMessageText);
                updateStatusService.sendUpdateStatus(OrderStatus.DONE, containerMessage.findMessage(chatId, messageId));

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            execute(new DeleteMessage(String.valueOf(chatId), editMessageText.getMessageId()));
                        } catch (TelegramApiException e) {
                            logger.error("Ошибка удаления сообщения по таймеру: " + e.getMessage());
                        }
                    }
                }, 5000);
            }
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    public void sendMessageAndUpdateButton(EditMessageReplyMarkup editMessageReplyMarkup) {
        try {
            execute(editMessageReplyMarkup);
            updateStatusService.sendUpdateStatus(OrderStatus.DELIVERED, containerMessage.findMessage(Long.valueOf(editMessageReplyMarkup.getChatId()),
                    editMessageReplyMarkup.getMessageId().longValue()));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public Integer sendMessageAndGetId(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                Message execute = execute(sendMessage);
                return execute.getMessageId();
            } catch (TelegramApiException e) {
                logger.error(e.toString());
            }
        }
        return null;
    }

    private void sendEditMessageReplyMarkup(EditMessageReplyMarkup editMessageReplyMarkup) {
        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            logger.error("Сообщение не отправлено (editMessageReplyMarkup) : " + e.getMessage());
        }
    }

    private void sendEditMessageText(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            logger.error("Сообщение не отправлено в бот: " + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Сообщение не отправлено в бот: " + e.getMessage());
        }
    }

    private EditMessageReplyMarkup updateDeliveredButtonToDone(long chatId, long messageId, String nameButton) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId((int) messageId)
                .replyMarkup(getUpdateKeyboard(nameButton)).build();
    }

    private InlineKeyboardMarkup getUpdateKeyboard(String nameButton) {
        InlineKeyboardButton button = new InlineKeyboardButton(nameButton);
        button.setCallbackData(nameButton);
        return new InlineKeyboardMarkup(List.of(List.of(button)));

    }

    public void sendMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error(e.toString());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
