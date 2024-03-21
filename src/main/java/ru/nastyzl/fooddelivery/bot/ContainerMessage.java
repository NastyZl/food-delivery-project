package ru.nastyzl.fooddelivery.bot;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ContainerMessage {

    HashMap<Long, HashMap<Long, Long>> containerMessages = new HashMap<>();

    public HashMap<Long, HashMap<Long, Long>> getContainerMessages() {
        return containerMessages;
    }

    public void addMessages(Long chatId, Long messageId, Long orderId) {
        HashMap<Long, Long> messages = containerMessages.get(chatId);
        if (messages == null) {
            messages = new HashMap<>();
            containerMessages.put(chatId, messages);
        }
        messages.put(messageId, orderId);

    }

    public void removeMessage(Long chatId, Long messageId) {
        HashMap<Long, Long> messages = containerMessages.get(chatId);
        if (messages != null) {
            messages.remove(messageId);
        }
    }

    public Long findMessage(Long chatId, Long messageId) {
        HashMap<Long, Long> messages = containerMessages.get(chatId);
        if (messages != null) {
            return messages.get(messageId);
        }
        return null;
    }
}
