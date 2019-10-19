import java.io.FileNotFoundException;
import java.util.Scanner;

class ConsoleBot
{
    private Scanner sc;

    ConsoleBot()
    {
        sc = new Scanner(System.in);
    }

     void start() throws FileNotFoundException
     {
        MessageHandler messageHandler = new MessageHandler();
        while (true)
        {
            String message = getMessage(sc);
            String answer = messageHandler.getAnswer(message);
            if(messageHandler.startGame)
            {
                StartGame(messageHandler.game);
                messageHandler.startGame = false;
                messageHandler.stopGame = true;
            }
            if(messageHandler.stopGame)
            {
                messageHandler.stopGame = false;
                continue;
            }
            sendMessage(answer);
        }
    }

    private static String getMessage(Scanner sc)
    {
        return sc.nextLine().toLowerCase();
    }

    private static void sendMessage(String messageSend)
    {
        System.out.println(messageSend);
    }

    private void StartGame(String game) throws FileNotFoundException
    {
        if(game.equals("Слова"))
        {
            GameWords gameWords = new GameWords();
            sendMessage(gameWords.startGame());
            sendMessage(gameWords.sendWord());
            while (gameWords.stopGame)
            {
                String msg = getMessage(sc);
                sendMessage(gameWords.giveAnswerToUser(msg));
            }

        }
    }
}