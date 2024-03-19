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
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final UserService userService;

    private static final String DELIVERED_BUTTON = "Заказ у меня";

    private static final String DONE_BUTTON = "Завершить заказ";

    private static final String HELP_TEXT = "Здесь будет справочная информация для вас! Позже...";

    @Value("${bot.name}")
    private String name;

    public TelegramBotService(@Value("${bot.token}") String token, UserService userService) {
        super(token);
        this.userService = userService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "запустить бота"));
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

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.contains("/activation")) {
                var username = messageText.substring(messageText.indexOf(" ") + 1);
                if (userService.findByChatId(chatId).isPresent()) {
                    sendMessage(chatId, "У вас уже есть аккаунт!");
                } else if (userService.activateCourier(username, chatId)) {
                    sendMessage(chatId, "Мы вас идентифицировали, ожидайте заявок на доставку");
                } else
                    sendMessage(chatId, "Пользователя с таким логинов в системе нет! Сначала надо зарегистрировать на сайте.");
            }
            switch (messageText) {
                case "/start":
                    startCommand(update.getMessage());
                    break;
                case "/orders":
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Такой команды я не знаю.");
            }
        } else if (update.hasCallbackQuery()) {

            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals(DELIVERED_BUTTON)) {

                EditMessageReplyMarkup editMessageReplyMarkup = updateButtonToDone(chatId, messageId);

                try {
                    execute(editMessageReplyMarkup);
                } catch (TelegramApiException e) {
                    logger.error("Сообщение не отправлено в бот: " + e.getMessage());
                }

            } else if (callBackData.equals(DONE_BUTTON)) {

                String text = "Заказ завершен! Продолжайте в том же духе!";

                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText(text);
                editMessageText.setChatId(chatId);

                editMessageText.setMessageId((int) messageId);

                try {
                    execute(editMessageText);
                } catch (TelegramApiException e) {
                    logger.error("Сообщение не отправлено в бот: " + e.getMessage());
                }

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

    private void startCommand(Message msg) {

        logger.info("Пользователь (" + msg.getChatId() + ") запустил бот");

        if (userService.findByChatId(msg.getChatId()).isPresent()) {
            sendMessage(msg.getChatId(), "Привет, " + msg.getChat().getFirstName() + ". Бот готов к работе.");
        } else startCommandResponseMessage(msg.getChatId(), msg.getChat().getFirstName());
    }

    private void startCommandResponseMessage(long chatId, String name) {
        String answer = "Привет, " + name + "! Чтобы активировать свою учетную запись, отправьте команду '/activation ваш_логин'.";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    public void sendMessageNewOrder(long chatId, OrderEntity order) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        String orderDetail = "Детали заказа:\n" +
                "Дата заказа: " + order.getOrderDate() + "\n" +
                "Информация о заказчике: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() + " " + order.getCustomer().getPhone() + "\n" +
                "Продавец: " + order.getVendor().getFirstName() + " " + order.getVendor().getLastName() + " " + order.getVendor().getPhone() + "\n" +
                "Сумма заказа: " + order.getTotalAmount() + "\n" +
                "Тип оплаотты: " + order.getPaymentType().getValue() + "\n" +
                "Адрес доставки: " + order.getCustomerAddress();

        List<OrderItemEntity> orderItems = order.getOrderItems();

        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("Блюдо\t\tКоличество\t\tЦена\n");
        for (OrderItemEntity item : orderItems) {
            tableBuilder.append(item.getDish().getDishName())
                    .append("\t\t")
                    .append(item.getQuantity())
                    .append("\t\t")
                    .append(item.getDish().getCurrentPrice())
                    .append("\n");
        }

        message.setText(orderDetail + "\n" + tableBuilder);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        var button = new InlineKeyboardButton();

        button.setText(DELIVERED_BUTTON);
        button.setCallbackData(DELIVERED_BUTTON);

        row.add(button);
        keyboard.add(row);
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Сообщение не отправлено в бот: " + e.getMessage());
        }
    }

    private EditMessageReplyMarkup updateButtonToDone(long chatId, long messageId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        var button = new InlineKeyboardButton();

        button.setText(DONE_BUTTON);
        button.setCallbackData(DONE_BUTTON);

        row.add(button);
        keyboard.add(row);
        markup.setKeyboard(keyboard);

        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(chatId);
        editMessage.setMessageId((int) messageId);
        editMessage.setReplyMarkup(markup);
        return editMessage;
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
