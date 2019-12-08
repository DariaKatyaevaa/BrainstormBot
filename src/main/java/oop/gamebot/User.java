package oop.gamebot;
import oop.gamebot.games.calculate.GameCalculate;
import oop.gamebot.games.cities.GameCities;
import oop.gamebot.games.words.GameWords;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class User
{
    private Long userId;
    private Map<String, Integer[]> statistic = new HashMap<>();
    private GameWords gameWords;
    private GameCities gameCities;
    private GameCalculate gameCalculate;
    private boolean isPlaying;
    private boolean isPlayingWord;
    private boolean isPlayingCity;
    private boolean isPlayingCalculate;

    User(Long userId) throws FileNotFoundException {
        this.userId = userId;
        gameWords = new GameWords(this);
        gameCalculate = new GameCalculate(this);
        gameCities = new GameCities(this);
        CreateStatisticMap();
        isPlaying = false;
        isPlayingWord = false;
        isPlayingCity = false;
        isPlayingCalculate = false;
    }

    User(Integer userId) throws FileNotFoundException {
        this.userId = Long.valueOf(userId);
        gameWords = new GameWords(this);
        gameCalculate = new GameCalculate(this);
        gameCities = new GameCities(this);
        CreateStatisticMap();
        isPlaying = false;
        isPlayingWord = false;
        isPlayingCity = false;
        isPlayingCalculate = false;
    }

    public void setStatistic(String game, String status)
    {
        if("win".equals(status))
        {
            statistic.get(game)[0] += 1;
        }
        else if("lose".equals(status))
        {
            statistic.get(game)[1] += 1;
        }
    }

    Long getUserId()
    {
        return userId;
    }


    GameWords getGameWords()
    {
        return gameWords;
    }

    GameCalculate getGameCalculate()
    {
        return gameCalculate;
    }

    GameCities getGameCities()
    {
        return gameCities;
    }

    boolean isPlaying()
    {
        return isPlaying;
    }

    boolean isPlayingWord()
    {
        return isPlayingWord;
    }

    boolean isPlayingCalculate()
    {
        return isPlayingCalculate;
    }

    boolean isPlayingCity()
    {
        return isPlayingCity;
    }

    void isPlaying(boolean status)
    {
        isPlaying = status;
    }

    void isPlayingWord(boolean status)
    {
        isPlayingWord = status;
    }

    void isPlayingCalculate(boolean status)
    {
        isPlayingCalculate = status;
    }

    void isPlayingCity(boolean status)
    {
        isPlayingCity = status;
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