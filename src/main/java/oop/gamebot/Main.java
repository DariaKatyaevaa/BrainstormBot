package oop.gamebot;

import java.io.FileNotFoundException;

// чтобы сделть коммит ctrl + k, чтобы запушить ctrl+shift+k
public class Main
{
    public static void main(String[] args) throws FileNotFoundException {
        String workerConsole = "Console";
        String workerTelegram = "Telegram";
        MainBot mainBot = new MainBot(workerTelegram);
    }
}