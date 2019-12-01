package oop.gamebot;

import java.util.HashMap;

class ChatManager {
    Long chatId;
    HashMap<Integer, User> users;
    HashMap<User, String[]> sortedByStatistics;
    HashMap<User, MessageHandler> userMessageHandlerMap;

    ChatManager(Long chatId)
    {
        this.chatId = chatId;
        users = new HashMap<>();
        userMessageHandlerMap = new HashMap<>();
        sortedByStatistics = new HashMap<>();
    }
}
