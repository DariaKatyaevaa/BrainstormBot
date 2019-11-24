import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GameCities implements Game
{
    private HashMap<String,ArrayList<String>> allCities = new HashMap<String,ArrayList<String>>();
    private String[] rt;
    private ArrayList<String> usedCities;
    private String lastLetter;
    public boolean gameEnd = true;

    GameCities() throws FileNotFoundException
    {
        String path = "C:\\Users\\user\\IdeaProjects\\BrainstormBot\\content\\cities";
        rt = new Scanner(new File(path)).useDelimiter("\\Z").next().split("\n");
        usedCities = new ArrayList<String>();
        for (String city: rt)
        {
            String a = city.toLowerCase().trim();
            String key = GetFirst(a);
            if (allCities.containsKey(key))
            {
                ArrayList<String> q = allCities.get(key);
                q.add(a);
                allCities.put(key,q);
            }
            else
            {
                ArrayList<String> q = new ArrayList<>();
                q.add(a);
                allCities.put(key,q);
            }
        }
    }

    public String GetRandomCity()
    {
        int rnd = new Random().nextInt(rt.length);
        usedCities.add(rt[rnd].toLowerCase().trim());
        lastLetter = GetLast(rt[rnd].toLowerCase().trim());
        return rt[rnd].toLowerCase().trim();
    }
    public String GetRandomCityWith(ArrayList cities)
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
    public boolean Check(ArrayList<String> words,String world)
    {
        for (String word: words) {
            if (word == world)
                return true;
        }
        return false;
    }

    private String GetLast(String city)
    {
        String a = String.valueOf(city.charAt(city.length()-1));
        if ((a.charAt(0)=='ь') || (a.charAt(0)=='ъ') || (a.charAt(0)=='ы'))
            return String.valueOf(city.charAt(city.length()-2));
        return a;
    }
    private String GetFirst(String city)
    {
        return String.valueOf(city.charAt(0));
    }
    @Override
    public String startGame()
    {
        resetGame();
        return "Отлично!\n" + "\n" +
                "Правила нашей игры очень простые: вы должны написать город, название которого начинается на букву, которой заканчивается предыдущее название города.\n" +
                "Названий городов на Ъ и Ь знак нет. Поэтому напишите город на букву, стоящую перед Ъ и Ь знаком.\n" +
                "Названия городов не должны повторяться.\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n" +
                "Начинаем игру!\n";
    }
    @Override
    public void resetGame()
    {
        usedCities.clear();
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        String a = message.toLowerCase().trim();
        if(message.equals("стоп"))
        {
            gameEnd = false;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        if (GetFirst(a).equals(lastLetter))
        {
            if (usedCities.contains(a))
                return "Этот город уже был";
            if (allCities.get(lastLetter).contains(a))
            {
                usedCities.add(a);
                lastLetter = GetLast(a);
                String g = GetRandomCityWith(allCities.get(lastLetter));
                usedCities.add(g);
                lastLetter = GetLast(g);
                return g;
            }
        }
        return lastLetter;
    }
}