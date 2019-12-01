package oop.gamebot;

import oop.gamebot.games.words.GameWords;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class TelegramBot extends TelegramLongPollingBot
{
    private UserManager userManager = new UserManager();
    private HashMap<Long, ChatManager> chatManagerHashMap = new HashMap<>();
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
            }
        }
        else {
            Long chatId = update.getMessage().getChatId();

            if (!userManager.users.containsKey(chatId)) {
                try {
                    userManager.users.put(chatId, new User(chatId));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                userManager.userMessageHandlerMap.put(userManager.users.get(chatId), new MessageHandler(userManager.users.get(chatId)));
            }
            String message;
            String answer;
            MessageHandler messageHandler = userManager.userMessageHandlerMap.get(userManager.users.get(chatId));
            if (messageHandler.startGameTG && !userManager.users.get(chatId).isPlaying) {
                try {
                    startGame(update, userManager.users.get(chatId), chatId);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageHandler.startGameTG && userManager.users.get(chatId).isPlaying)
                playing(update, userManager.users.get(chatId), messageHandler, chatId);
            else {
                message = getMessage(update);
                answer = messageHandler.getAnswer(message);
                sendMessage(answer, chatId);
            }
        }
    }

    private void workWithGroup(Update update) throws FileNotFoundException {
        Long chatId = update.getMessage().getChatId();
        Integer userId = update.getMessage().getFrom().getId();
        if (!chatManagerHashMap.containsKey(chatId))
        {
            chatManagerHashMap.put(chatId, new ChatManager(chatId));
        }
        if(!chatManagerHashMap.get(chatId).users.containsKey(userId))
        {
            chatManagerHashMap.get(chatId).users.put(userId, new User(userId));
            chatManagerHashMap.get(chatId).userMessageHandlerMap.put(chatManagerHashMap.get(chatId).users.get(userId),
                    new MessageHandler(chatManagerHashMap.get(chatId).users.get(userId)));
        }

        String message;
        String answer;
        User user = chatManagerHashMap.get(chatId).users.get(userId);
        MessageHandler messageHandler = chatManagerHashMap.get(chatId).userMessageHandlerMap.get(user);
        if (messageHandler.startGameTG && !user.isPlaying) {
            try {
                startGame(update, user, chatId);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (messageHandler.startGameTG && user.isPlaying)
            playing(update, user, messageHandler, chatId);
        else {
            message = getMessage(update).replace("/", "").trim();
            answer = messageHandler.getAnswer(message);
            sendMessage(answer, chatId);
        }
    }

    private void startGame(Update update, User user, Long chatId) throws TelegramApiException {
        user.isPlaying = true;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String message = getMessage(update).toLowerCase().replace("/", "").trim();
        if("слова".equals(message))
        {
            user.isPlayingWord = true;
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(user.gameWords.startGame());
        }
        else if("города".equals(message))
        {
            user.isPlayingCity = true;
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(user.gameCities.startGame());
        }
        else if("арифметика".equals(message))
        {
            user.isPlayingCalculate = true;
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(user.gameCalculate.startGame());
        }
        else if("статистика".equals(message))
        {
            user.isPlaying = false;
            sendMessage.setText(user.StatisticToString());
        }
        else{
            user.isPlaying = false;
            sendMessage.setText("Не понял, введите название игры!");
        }
        execute(sendMessage);
    }

    private void playing(Update update, User user, MessageHandler messageHandler, Long chatId)
    {
        if(user.isPlayingWord && user.isPlaying && !user.gameWords.stopGame)
        {
            String msg = getMessage(update).replace("/", "").trim();
            sendMessage(user.gameWords.giveAnswerToUser(msg), chatId);
        }
        else if(user.gameWords.stopGame)
        {
            user.isPlayingWord = false;
            user.isPlaying = false;
            messageHandler.startGameTG = false;
        }

        else if(user.isPlayingCity && user.isPlaying && !user.gameCities.gameStop)
        {
            String msg = getMessage(update).replace("/", "").trim();
            sendMessage(user.gameCities.giveAnswerToUser(msg), chatId);
        }
        else if(user.gameCities.gameStop)
        {
            user.isPlayingCity = false;
            user.isPlaying = false;
            messageHandler.startGameTG = false;
        }

        else if(user.isPlayingCalculate && user.isPlaying && !user.gameCalculate.stopGame)
        {
            String msg = getMessage(update).replace("/", "").trim();
            sendMessage(user.gameCalculate.giveAnswerToUser(msg), chatId);
        }
        else if(user.gameCalculate.stopGame)
        {
            user.isPlayingCalculate = false;
            user.isPlaying = false;
            messageHandler.startGameTG = false;
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
