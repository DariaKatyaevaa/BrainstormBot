package oop.gamebot;
import java.util.HashMap;

class UserManager
{
    HashMap<Long, User> users;
    HashMap<User, String[]> sortedByStatistics;
    HashMap<User, MessageHandler> userMessageHandlerMap;

    UserManager()
    {
        users = new HashMap<>();
        userMessageHandlerMap = new HashMap<>();
        sortedByStatistics = new HashMap<>();
    }
}