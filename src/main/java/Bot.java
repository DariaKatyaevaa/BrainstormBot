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

public class Bot extends TelegramLongPollingBot {
    private long chatId;
    private boolean gameFlag = false;
    private boolean gameStart = false;
    private int gameCount = 5;
    private GameWords game;

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        chatId = update.getMessage().getChatId();
        try {
            sendMessage.setText(inputMessage(update.getMessage().getText()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
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

    private String inputMessage(String msg) throws FileNotFoundException {
        if(msg.equals("/start"))
            return "Привет!\nЯ игровой бот, который заставит ваши мозги шевелиться!\nДавайте поиграем:)";
        if(msg.toLowerCase().contains("да") || msg.toLowerCase().contains("давай")){
            gameFlag = true;
            return "Отлично!\n" +
                    "\n" +
                    "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                    "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                    "Если вы не справитесь за 5 попыток, то я победил!\n" +
                    "Начнём?";
        }

        if (gameFlag && !gameStart) {
            gameStart = true;
            return StartGame();
        }
        if (gameStart && gameCount != 0){
            if(msg.toLowerCase().contains(game.word) && game.word.contains(msg.toLowerCase()))
                return "Верно!";
            gameCount--;
            return String.format("Попробуйте еще! \nОсталось %s попыток.", gameCount);
        }

        if(gameStart && gameCount == 0){
            gameCount = 5;
            gameStart = false;
            gameFlag = false;
            return "К сожалению попытки закончились. Вы проиграли:(\n Хотите сыграть еще раз?";
        }

        return "Не понял!";
    }

    private String StartGame() throws FileNotFoundException {
        game = new GameWords();
        return game.mixedWord;
    }


}
