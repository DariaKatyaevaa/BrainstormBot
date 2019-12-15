package oop.gamebot;
import org.json.simple.parser.ParseException;
import java.io.IOException;

// чтобы сделть коммит ctrl + k, чтобы запушить ctrl+shift+k
class Main
{
     public static void main(String[] args) throws IOException, ParseException {
        String workerConsole = "Console";
        String workerTelegram = "Telegram";
        MainBot mainBot = new MainBot(workerTelegram);
    }
}