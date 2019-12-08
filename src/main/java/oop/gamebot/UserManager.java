package oop.gamebot;
import java.util.HashMap;
import java.util.Map;

class UserManager
{
    private Map<Long, User> users;
    private Map<User, MessageHandler> userMessageHandlerMap;

    UserManager()
    {
        users = new HashMap<>();
        userMessageHandlerMap = new HashMap<>();
    }

    Map<Long, User> getUsers()
    {
        return users;
    }

    void setUser(Long chatId, User user)
    {
        if(!users.containsKey(chatId))
        {
            users.put(chatId, user);
        }
    }

    Map<User, MessageHandler> getMessageHandler()
    {
        return userMessageHandlerMap;
    }

    void setMessageHandler(User user, MessageHandler messageHandler)
    {
        if(!userMessageHandlerMap.containsKey(user))
        {
            userMessageHandlerMap.put(user, messageHandler);
        }
    }

}