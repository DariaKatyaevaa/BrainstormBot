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
    private MessageHandler messageHandler;
    private GameWords gameWords;

    TelegramBot() throws FileNotFoundException
    {
        messageHandler = new MessageHandler();
        gameWords = new GameWords();
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        Long chatId = update.getMessage().getChatId();
        String message;
        String answer;
        if((messageHandler.startGame || messageHandler.isPlaying) && gameWords.stopGame)
            playing(update, chatId);
        else
        {
            message = getMessage(update);
            answer = messageHandler.getAnswer(message);
            sendMessage(answer, chatId);
        }
    }


    private void playing(Update update, long chatId)
    {
        if(messageHandler.game.equals("Слова") && !messageHandler.stopGame)
        {
            if(messageHandler.startGame)
            {
                sendMessage(gameWords.sendWord(), chatId);
                messageHandler.startGame = false;
                messageHandler.isPlaying = true;
            }
            String msg = getMessage(update);
            sendMessage(gameWords.giveAnswerToUser(msg), chatId);
        }
        else if(messageHandler.stopGame)
        {
            messageHandler.stopGame = false;
            messageHandler.isPlaying = false;
        }
    }

    private String getMessage(Update update)
    {
        return update.getMessage().getText().toLowerCase();
    }

    private void sendMessage(String text, Long chatId, String... commands)
    {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        if (commands.length != 0) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);

            List<KeyboardRow> keyboard = new ArrayList<>(); // Создаем список строк клавиатуры
            KeyboardRow keyboardFirstRow = new KeyboardRow(); // Первая строчка клавиатуры
            Arrays.stream(commands).forEach(keyboardFirstRow::add); // Добавляем кнопки в первую строчку клавиатуры
            keyboard.add(keyboardFirstRow); // Добавляем все строчки клавиатуры в список
            replyKeyboardMarkup.setKeyboard(keyboard); // и устанваливаем этот список нашей клавиатуре
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
