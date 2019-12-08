package oop.gamebot.games.cities;

import oop.gamebot.User;
import oop.gamebot.games.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class GameCities implements Game
{
    public HashMap<String,ArrayList<String>> citiesMap = new HashMap<String,ArrayList<String>>();
    private String[] citiesList;
    public List<String> usedCities;
    private String lastLetter;
    public boolean gameStop = false;
    private User user;

    public GameCities(User user) throws FileNotFoundException
    {
        File file = new File(getClass().getClassLoader().getResource("cities").getFile());
        citiesList = new Scanner(file).useDelimiter("\\Z").next().split("\n");
        usedCities = new ArrayList<>();
        this.user = user;
        CreateMap();
    }

    private void CreateMap()
    {
        for (String city: citiesList)
        {
            String a = city.toLowerCase().trim();
            String key = GetFirst(a);
            if (citiesMap.containsKey(key))
            {
                ArrayList<String> q = citiesMap.get(key);
                q.add(a);
                citiesMap.put(key,q);
            }
            else
            {
                ArrayList<String> q = new ArrayList<>();
                q.add(a);
                citiesMap.put(key,q);
            }
        }
    }

    public String GetRandomCity()
    {
        int rnd = new Random().nextInt(citiesList.length);
        usedCities.add(citiesList[rnd].trim());
        lastLetter = GetLast(citiesList[rnd].trim());
        return citiesList[rnd];
    }

    private String GetRandomCityWith(ArrayList cities)
    {
        int rnd = new Random().nextInt(cities.size());
        String city = cities.get(rnd).toString().trim();
        while (usedCities.contains(city))
        {
            rnd = new Random().nextInt(cities.size());
            city = cities.get(rnd).toString().trim();
        }
        return city;
    }

    public String GetLast(String city)
    {
        city = city.trim();
        String a = String.valueOf(city.charAt(city.length()-1));
        if ((a.charAt(0)=='ь') || (a.charAt(0)=='ъ') || (a.charAt(0)=='ы'))
            return String.valueOf(city.charAt(city.length()-2));
        return a;
    }

    private String GetFirst(String city)
    {
        city = city.trim();
        return String.valueOf(city.charAt(0));
    }

    @Override
    public String startGame()
    {
        resetGame();
        return "Отлично!\n" + "\n" +
                "Правила нашей игры очень простые: вы должны написать город, название которого начинается на букву,\n" +
                "которой заканчивается предыдущее название города.\n" +
                "Названий городов на Ъ и Ь знак нет. Поэтому напишите город на букву, стоящую перед Ъ и Ь знаком.\n" +
                "Названия городов не должны повторяться.\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n" +
                "Для того, чтобы я поменял город, напиши ДРУГОЙ\n" +
                "Начинаем игру!\n";
    }

    @Override
    public void resetGame()
    {
        gameStop = false;
        usedCities.clear();
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        if("начать".equals(message))
        {
            gameStop = false;
            String city = GetRandomCity();
            lastLetter = GetLast(city);
            usedCities.add(city);
            return city;
        }
        else if("стоп".equals(message))
        {
            gameStop = true;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        else if("другой".equals(message))
        {
            gameStop = false;
            String city = GetRandomCity();
            lastLetter = GetLast(city);
            usedCities.add(city);
            return city;
        }
        else if("статистика".equals(message))
        {
            return user.StatisticToString();
        }
        else if (GetFirst(message).equals(lastLetter))
        {
            if (usedCities.contains(message))
            {
                user.statistic.get("Города")[1] += 1;
                return "Этот город уже был";
            }
            else if (citiesMap.get(lastLetter).contains(message))
            {
                user.statistic.get("Города")[0] += 1;
                usedCities.add(message);
                lastLetter = GetLast(message);
                String city = GetRandomCityWith(citiesMap.get(lastLetter));
                usedCities.add(city);
                lastLetter = GetLast(city);
                return city;
            }
        }
        return "Неверно! Попробуйте другой город :)";
    }
}
