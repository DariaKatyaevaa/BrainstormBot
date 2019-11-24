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
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot
{
    private UserManager userManager = new UserManager();
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    TelegramBot()
    {
        GetKeyboard();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        Long chatId = update.getMessage().getChatId();
        if(!userManager.users.containsKey(chatId))
        {
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
        if(messageHandler.startGameTG && !userManager.users.get(chatId).isPlaying) {
            try {
                startGame(update, userManager.users.get(chatId));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if(messageHandler.startGameTG && userManager.users.get(chatId).isPlaying)
            playing(update, userManager.users.get(chatId), messageHandler);
        else
        {
            message = getMessage(update);
            answer = messageHandler.getAnswer(message);
            sendMessage(answer, chatId);
        }
    }

    private void startGame(Update update, User user) throws TelegramApiException {
        user.isPlaying = true;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(user.chatId);
        if(getMessage(update).toLowerCase().equals("слова"))
        {
            user.isPlayingWord = true;
            sendMessage.setText(user.gameWords.startGame());
        }
        else if(getMessage(update).toLowerCase().equals("города"))
        {
            user.isPlayingCity = true;
            sendMessage.setText(user.gameCities.startGame());
        }
        else if(getMessage(update).toLowerCase().equals("арифметика"))
        {
            user.isPlayingCalculate = true;
            sendMessage.setText(user.gameCalculate.startGame());
        }
        else{
            sendMessage.setText("Не понял");
        }
        execute(sendMessage);
    }

    private void playing(Update update, User user, MessageHandler messageHandler)
    {
        if(user.isPlayingWord && user.isPlaying && !user.gameWords.stopGame)
        {
            String msg = getMessage(update);
            sendMessage(user.gameWords.giveAnswerToUser(msg), user.chatId);
        }
        else if(user.gameWords.stopGame)
        {
            user.isPlayingWord = false;
            user.isPlaying = false;
            messageHandler.startGameTG = false;
        }

        else if(user.isPlayingCity && user.isPlaying && !user.gameCities.gameStop)
        {
            String msg = getMessage(update);
            sendMessage(user.gameCities.giveAnswerToUser(msg), user.chatId);
        }
        else if(user.gameCities.gameStop)
        {
            user.isPlayingCity = false;
            user.isPlaying = false;
            messageHandler.startGameTG = false;
        }

        else if(user.isPlayingCalculate && user.isPlaying && !user.gameCalculate.stopGame)
        {
            String msg = getMessage(update);
            sendMessage(user.gameCalculate.giveAnswerToUser(msg), user.chatId);
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
