package oop.gamebot;
import java.util.HashMap;

class UserManager
{
    HashMap<Long, User> users;
    HashMap<User, MessageHandler> userMessageHandlerMap;

    UserManager()
    {
        users = new HashMap<>();
        userMessageHandlerMap = new HashMap<>();
    }
}