import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;

//toDo:
//Меню для игры:
//Кнопки начать игру, правила
// кнопки в режиме игры: закончить игру
// добавить счетчик попыток в игру

public class Bot extends TelegramLongPollingBot
{
    private boolean gameFlag = false;
    private boolean gameStart = false;
    private int gameCount = 5;
    private GameWords game;

    @Override
    public void onUpdateReceived(Update update)
    {
        update.getUpdateId();
        long chatId = update.getMessage().getChatId();
        try {
            sendMessage(chatId, inputMessage(update.getMessage().getText()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "@BrainstormGameBot";
    }

    @Override
    public String getBotToken() {
        return "932546483:AAEyztcGmmhwHwOIwcNO3Omq49n8kf3hPSg";
    }

    private void sendMessage(long chatId , String text)
    {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try { execute(message); }
        catch (TelegramApiException e) { e.printStackTrace(); }
    }

    private String inputMessage(String msg) throws FileNotFoundException
    {
        if(msg.equals("/start"))
            return "Привет!\nЯ игровой бот, который заставит ваши мозги шевелиться!\nДавайте поиграем:)";
        if(msg.toLowerCase().contains("да") || msg.toLowerCase().contains("давай"))
        {
            gameFlag = true;
            return "Отлично!\n" +
                    "\n" +
                    "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                    "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                    "Если вы не справитесь за 5 попыток, то я победил!\n" +
                    "Начнём?";
        }

        if (gameFlag && !gameStart)
        {
            gameStart = true;
            return StartGame();
        }
        if (gameStart && gameCount != 0)
        {
            if((game.word).equals(msg) ||
                    (msg.toLowerCase().contains(game.word) && game.word.contains(msg.toLowerCase())))
            {
                resetGameWord();
                return "Верно!\n Напишите ЕЩЁ если хотите сыграть ещё раз!";
            }
            System.out.println(msg+'\n'+game.word);
            gameCount--;
            return String.format("Попробуйте ещё! \nОсталось %s попыток.", gameCount);
        }

        if(gameCount==0 && gameStart)
        {
            resetGameWord();
            return "К сожалению попытки закончились. Вы проиграли:(\n Хотите сыграть ещё раз?";
        }

        if (msg.toLowerCase().equals("ещё"))
        {
            gameFlag = true;
            return "Отлично!\n";
        }

        return "Не понял!";
    }

    private String StartGame() throws FileNotFoundException
    {
        game = new GameWords();
        return game.mixedWord;
    }

    private void resetGameWord()
    {
        gameCount = 5;
        gameStart = false;
        gameFlag = false;
    }

}
