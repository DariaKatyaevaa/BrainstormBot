package oop.gamebot;

import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;

class MainBot
{
    MainBot(String worker) throws IOException, ParseException {
        if(worker.equals("Console"))
        {
            ConsoleBot consoleBot = new ConsoleBot();
            consoleBot.start();
        }
        if(worker.equals("Telegram"))
        {
            System.out.println("start");
            ApiContextInitializer.init();
            TelegramBotsApi TBA = new TelegramBotsApi();
            try
            {
                TBA.registerBot(new TelegramBot());
            }
            catch (TelegramApiRequestException e)
            {
                e.printStackTrace();
            }
        }
    }
}