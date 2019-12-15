package oop.gamebot;
import java.io.IOException;
import java.util.Scanner;

import oop.gamebot.games.calculate.GameCalculate;
import oop.gamebot.games.cities.GameCities;
import oop.gamebot.games.thesaurus.GameThesaurus;
import oop.gamebot.games.words.GameWords;
import org.json.simple.parser.ParseException;

class ConsoleBot
{
    private Scanner sc;
    private User user = new User((long) 0);

    ConsoleBot() throws IOException, ParseException {
        sc = new Scanner(System.in);
    }

    void start() throws IOException, ParseException {
        MessageHandler messageHandler = new MessageHandler(user);
        while (true)
        {
            String message = getMessage(sc);
            String answer = messageHandler.getAnswer(message);
            if(messageHandler.isStartGame())
            {
                StartGame(messageHandler.getGame());
                messageHandler.isStartGame(false);
                messageHandler.isStopGame(false);
            }
            if(messageHandler.isStopGame())
            {
                messageHandler.isStopGame(false);
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

    private void StartGame(String game) throws IOException, ParseException {
        if("Слова".equals(game))
        {
            GameWords gameWords = new GameWords(user);
            sendMessage(gameWords.startGame());
            sendMessage(gameWords.sendWord());
            while (!gameWords.isStopGame())
            {
                String msg = getMessage(sc);
                sendMessage(gameWords.giveAnswerToUser(msg));
            }

        }
        else if("Города".equals(game))
        {
            GameCities gameСities = new GameCities(user);
            sendMessage(gameСities.startGame());
            sendMessage(gameСities.GetRandomCity());
            while (!gameСities.isStopGame())
            {
                String msg = getMessage(sc);
                sendMessage(gameСities.giveAnswerToUser(msg));
            }

        }
        else if("Арифметика".equals(game))
        {
            GameCalculate gameCalculate = new GameCalculate(user);
            sendMessage(gameCalculate.startGame());
            sendMessage(gameCalculate.sendEquation());
            while (!gameCalculate.isStopGame())
            {
                String msg = getMessage(sc);
                sendMessage(gameCalculate.giveAnswerToUser(msg));
            }

        }
        else if("Энциклопедия".equals(game))
        {
            GameThesaurus gameThesaurus = new GameThesaurus(user);
            sendMessage(gameThesaurus.startGame());
            sendMessage(gameThesaurus.getQuestion());
            while (!gameThesaurus.isStopGame())
            {
                String msg = getMessage(sc);
                sendMessage(gameThesaurus.giveAnswerToUser(msg));
            }

        }
    }
}