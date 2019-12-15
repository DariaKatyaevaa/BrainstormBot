package oop.gamebot;

import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

class TelegramBot extends TelegramLongPollingBot
{
    private UserManager userManager = new UserManager();
    private Map<Long, ChatManager> chatManagerHashMap = new HashMap<>();
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    TelegramBot()
    {
        GetKeyboard();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if(update.getMessage().isGroupMessage()) {
            try {
                workWithGroup(update);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Long chatId = update.getMessage().getChatId();

            if (!userManager.getUsers().containsKey(chatId)) {
                try {
                    userManager.setUser(chatId, new User(chatId));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userManager.setMessageHandler(userManager.getUsers().get(chatId),
                        new MessageHandler(userManager.getUsers().get(chatId)));
            }
            String message;
            String answer;
            MessageHandler messageHandler = userManager.getMessageHandler().get(userManager.getUsers().get(chatId));
            if (messageHandler.isStartGameTG() && !userManager.getUsers().get(chatId).isPlaying()) {
                try {
                    startGame(update, userManager.getUsers().get(chatId), chatId, false);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageHandler.isStartGameTG() && userManager.getUsers().get(chatId).isPlaying())
                playing(update, userManager.getUsers().get(chatId), messageHandler, chatId, false);
            else {
                message = getMessage(update);
                answer = messageHandler.getAnswer(message);
                sendMessage(answer, chatId);
            }
        }
    }

    private void workWithGroup(Update update) throws IOException, ParseException {
        Long chatId = update.getMessage().getChatId();
        List<org.telegram.telegrambots.meta.api.objects.User> users;
        users = update.getMessage().getNewChatMembers();
        Integer userId = update.getMessage().getFrom().getId();
        if (!chatManagerHashMap.containsKey(chatId))
        {
            ChatManager chatManager = new ChatManager(chatId);
            if(users != null) {
                for (org.telegram.telegrambots.meta.api.objects.User us : users) {
                    Integer id = us.getId();
                    User user = new User(id);
                    chatManager.addUser(id, user);
                }
            }
            chatManagerHashMap.put(chatId,chatManager);
        }
        User user;
        if(!chatManagerHashMap.get(chatId).getUsers().containsKey(userId))
        {
            ChatManager chat = chatManagerHashMap.get(chatId);
            chat.getUsers().put(userId, new User(userId));
            if(chat.isPlaying())
            {
                chat.getUsers().get(userId).isPlaying(true);
                chat.getUsers().get(userId).isPlayingCalculate(chat.isPlayingCalculate());
                chat.getUsers().get(userId).isPlayingCity(chat.isPlayingCity());
                chat.getUsers().get(userId).isPlayingWord(chat.isPlayingWord());
            }
        }
        String message;
        String answer;
        ChatManager chat = chatManagerHashMap.get(chatId);
        user = chat.getUsers().get(userId);
        MessageHandler messageHandler = chatManagerHashMap.get(chatId).getMessageHandler();
        if (messageHandler.isStartGameTG() && !chat.isPlaying()) {
            try {
                startGame(update, user, chatId, true);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (messageHandler.isStartGameTG() && chat.isPlaying())
            playing(update, user, messageHandler, chatId, true);
        else {
            message = getMessage(update).replace("/", "").trim();
            answer = messageHandler.getAnswer(message);
            sendMessage(answer, chatId);
        }
    }

    private void startGame(Update update, User user, Long chatId, boolean isGroup) throws TelegramApiException {
        user.isPlaying(true);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = getMessage(update).toLowerCase().replace("/", "").trim();
        if("слова".equals(message))
        {
            if(isGroup)
            {
                chatManagerHashMap.get(chatId).isPlaying(true);
                chatManagerHashMap.get(chatId).isPlayingWord(true);
                Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                for (Integer id : users.keySet())
                {
                    users.get(id).isPlayingWord(true);
                    users.get(id).isPlaying(true);
                }
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(chatManagerHashMap.get(chatId).getGameWords().startGame());
            }
            else {
                user.isPlayingWord(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(user.getGameWords().startGame());
            }
        }
        else if("города".equals(message))
        {
            if(isGroup)
            {
                chatManagerHashMap.get(chatId).isPlaying(true);
                chatManagerHashMap.get(chatId).isPlayingCity(true);
                Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                for (Integer id : users.keySet())
                {
                    users.get(id).isPlayingCity(true);
                    users.get(id).isPlaying(true);
                }
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(chatManagerHashMap.get(chatId).getGameCities().startGame());
            }
            else {
                user.isPlayingCity(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(user.getGameCities().startGame());
            }
        }
        else if("энциклопедия".equals(message))
        {
            if(isGroup)
            {
                chatManagerHashMap.get(chatId).isPlaying(true);
                chatManagerHashMap.get(chatId).isPlayingThesaurus(true);
                Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                for (Integer id : users.keySet())
                {
                    users.get(id).isPlayingThesaurus(true);
                    users.get(id).isPlaying(true);
                }
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(chatManagerHashMap.get(chatId).getGameThesaurus().startGame());
            }
            else {
                user.isPlayingThesaurus(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(user.getGameThesaurus().startGame());
            }
        }
        else if("арифметика".equals(message))
        {
            if(isGroup)
            {
                chatManagerHashMap.get(chatId).isPlaying(true);
                chatManagerHashMap.get(chatId).isPlayingCalculate(true);
                Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                for (Integer id : users.keySet())
                {
                    users.get(id).isPlayingCalculate(true);
                    users.get(id).isPlaying(true);
                }
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(chatManagerHashMap.get(chatId).getGameCalculate().startGame());
            }
            else {
                user.isPlayingCalculate(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText(user.getGameCalculate().startGame());
            }
        }
        else if("статистика".equals(message))
        {
            user.isPlaying(false);
            if(isGroup)
                sendMessage.setText(chatManagerHashMap.get(chatId).getUser().StatisticToString());
            else
                sendMessage.setText(user.StatisticToString());
        }
        else{
            user.isPlaying(false);
            sendMessage.setText("Не понял, введите название игры!");
        }
        execute(sendMessage);
    }

    private void playing(Update update, User user, MessageHandler messageHandler, Long chatId, boolean isGroup)
    {
        if(user.isPlayingWord() && user.isPlaying() && !user.getGameWords().isStopGame())
        {
            String msg = getMessage(update).replace("/", "").trim();
            if(isGroup)
                sendMessage(chatManagerHashMap.get(chatId).getGameWords().giveAnswerToUser(msg), chatId);
            else
                sendMessage(user.getGameWords().giveAnswerToUser(msg), chatId);
            if(user.getGameWords().isStopGame() |
                    (isGroup && chatManagerHashMap.get(chatId).getGameWords().isStopGame()))
            {
                if(isGroup)
                {
                    chatManagerHashMap.get(chatId).isPlaying(false);
                    chatManagerHashMap.get(chatId).isPlayingWord(false);
                    Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                    for (Integer id : users.keySet())
                    {
                        users.get(id).isPlayingWord(false);
                        users.get(id).isPlaying(false);
                    }
                }
                user.isPlayingWord(false);
                user.isPlaying(false);
                messageHandler.isStartGameTG(false);
            }

        }
        else if(user.isPlayingCity() && user.isPlaying() && !user.getGameCities().isStopGame())
        {
            String msg = getMessage(update).replace("/", "").trim();
            if(isGroup)
                sendMessage(chatManagerHashMap.get(chatId).getGameCities().giveAnswerToUser(msg), chatId);
            else
                sendMessage(user.getGameCities().giveAnswerToUser(msg), chatId);
            if(user.getGameCities().isStopGame()  |
                    (isGroup && chatManagerHashMap.get(chatId).getGameCities().isStopGame()))
            {
                if(isGroup)
                {
                    chatManagerHashMap.get(chatId).isPlaying(false);
                    chatManagerHashMap.get(chatId).isPlayingCity(false);
                    Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                    for (Integer id : users.keySet())
                    {
                        users.get(id).isPlayingCity(false);
                        users.get(id).isPlaying(false);
                    }
                }
                user.isPlayingCity(false);
                user.isPlaying(false);
                messageHandler.isStartGameTG(false);
            }
        }
        else if(user.isPlayingCalculate() && user.isPlaying() && !user.getGameCalculate().isStopGame())
        {
            String msg = getMessage(update).replace("/", "").trim();
            if(isGroup)
                sendMessage(chatManagerHashMap.get(chatId).getGameCalculate().giveAnswerToUser(msg), chatId);
            else
                sendMessage(user.getGameCalculate().giveAnswerToUser(msg), chatId);

            if(user.getGameCalculate().isStopGame() |
                    (isGroup && chatManagerHashMap.get(chatId).getGameCalculate().isStopGame()))
            {
                if(isGroup)
                {
                    chatManagerHashMap.get(chatId).isPlaying(false);
                    chatManagerHashMap.get(chatId).isPlayingCalculate(false);
                    Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                    for (Integer id : users.keySet())
                    {
                        users.get(id).isPlayingCalculate(false);
                        users.get(id).isPlaying(false);
                    }
                }
                user.isPlayingCalculate(false);
                user.isPlaying(false);
                messageHandler.isStartGameTG(false);
            }

        }
        else if(user.isPlayingThesaurus() && user.isPlaying() && !user.getGameThesaurus().isStopGame())
        {
            String msg = getMessage(update).replace("/", "").trim();
            if(isGroup)
                sendMessage(chatManagerHashMap.get(chatId).getGameThesaurus().giveAnswerToUser(msg), chatId);
            else
                sendMessage(user.getGameThesaurus().giveAnswerToUser(msg), chatId);

            if(user.getGameThesaurus().isStopGame() |
                    (isGroup && chatManagerHashMap.get(chatId).getGameThesaurus().isStopGame()))
            {
                if(isGroup)
                {
                    chatManagerHashMap.get(chatId).isPlaying(false);
                    chatManagerHashMap.get(chatId).isPlayingCalculate(false);
                    Map<Integer, User> users = chatManagerHashMap.get(chatId).getUsers();
                    for (Integer id : users.keySet())
                    {
                        users.get(id).isPlayingThesaurus(false);
                        users.get(id).isPlaying(false);
                    }
                }
                user.isPlayingThesaurus(false);
                user.isPlaying(false);
                messageHandler.isStartGameTG(false);
            }

        }
    }

    private String getMessage(Update update)
    {
        return update.getMessage().getText().toLowerCase();
    }

    private void sendMessage(String text, Long chatId)
    {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void GetKeyboard()
    {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        //KeyboardRow keyboardRowTwo = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        keyboardRow.add("начать");
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public String getBotUsername()
    {
        return "@BrainstormGameBot";
    }

    @Override
    public String getBotToken()
    {
        return "932546483:AAEyztcGmmhwHwOIwcNO3Omq49n8kf3hPSg";
    }
}
