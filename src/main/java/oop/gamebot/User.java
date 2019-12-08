package oop.gamebot;
import oop.gamebot.games.calculate.GameCalculate;
import oop.gamebot.games.cities.GameCities;
import oop.gamebot.games.words.GameWords;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class User
{
    Long chatId;
    public HashMap<String, Integer[]> statistic = new HashMap<>();
    GameWords gameWords;
    GameCities gameCities;
    GameCalculate gameCalculate;
    boolean isPlaying;
    boolean isPlayingWord;
    boolean isPlayingCity;
    boolean isPlayingCalculate;

    User(Long chatId) throws FileNotFoundException {
        this.chatId = chatId;
        gameWords = new GameWords(this);
        gameCalculate = new GameCalculate(this);
        gameCities = new GameCities(this);
        CreateStatisticMap();
        isPlaying = false;
        isPlayingWord = false;
        isPlayingCity = false;
        isPlayingCalculate = false;
    }

    User(Integer chatId) throws FileNotFoundException {
        this.chatId = Long.valueOf(chatId);
        gameWords = new GameWords(this);
        gameCalculate = new GameCalculate(this);
        gameCities = new GameCities(this);
        CreateStatisticMap();
        isPlaying = false;
        isPlayingWord = false;
        isPlayingCity = false;
        isPlayingCalculate = false;
    }

    private void CreateStatisticMap()
    {
        Integer[] numbersWord = new Integer[2];
        numbersWord[0] = 0;
        numbersWord[1] = 0;
        Integer[] numbersCity = new Integer[2];
        numbersCity[0] = 0;
        numbersCity[1] = 0;
        Integer[] numbersCalc = new Integer[2];
        numbersCalc[0] = 0;
        numbersCalc[1] = 0;
        statistic.put("Слова", numbersWord);
        statistic.put("Арифметика", numbersCalc);
        statistic.put("Города", numbersCity);
    }

    public String StatisticToString()
    {
        return "    Игра СЛОВА   \n" +
                "Побед: " + statistic.get("Слова")[0] + "\n" +
                "Поражений: " + statistic.get("Слова")[1] + "\n\n" +
                "    Игра АРИФМЕТИКА   \n" +
                "Побед: " + statistic.get("Арифметика")[0] + "\n" +
                "Поражений: " + statistic.get("Арифметика")[1] + "\n\n" +
                "    Игра ГОРОДА   \n" +
                "Побед: " + statistic.get("Города")[0] + "\n" +
                "Поражений: " + statistic.get("Города")[1] + "\n";
    }
}