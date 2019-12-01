package oop.gamebot.games.words;

import oop.gamebot.User;
import oop.gamebot.games.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameWords implements Game
{
    private String word;
    private String mixedWord;
    private String[] wordList;
    private List<String> usedWords;
    private int attempts = 5;
    public boolean stopGame;
    private User user;

    public GameWords(User user) throws FileNotFoundException
    {
        File file = new File(getClass().getClassLoader().getResource("words").getFile());
        wordList = new Scanner(file).useDelimiter("\\Z").next().split("\n");
        word = getRandomWord(wordList);
        mixedWord = shuffle(word);
        stopGame = false;
        usedWords = new ArrayList<>();
        this.user = user;
    }

    private String shuffle(String word)
    {
        List<Character> characters = new ArrayList<>();
        for (char c : word.toCharArray())
        {
            characters.add(c);
        }
        StringBuilder mixedWord = new StringBuilder(word.length());
        while (characters.size() != 0)
        {
            int randPicker = (int) (Math.random() * characters.size());
            mixedWord.append(characters.remove(randPicker));
        }
        return mixedWord.toString();
    }

    private String getRandomWord(String[] words)
    {
        int randPicker = (int) (Math.random() * words.length);
        while (words[randPicker].length() > 7)
            randPicker = (int) (Math.random() * words.length);
        return words[randPicker];
    }

    @Override
    public String startGame()
    {
        stopGame = false;
        return "Отлично!\n" + "\n" +
                "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                "Если вы не справитесь за 5 попыток, то я победил!\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n";
    }

    public String sendWord()
    {
        usedWords.add(word);
        return mixedWord;
    }

    @Override
    public void resetGame()
    {
        attempts = 5;
        word = getRandomWord(wordList);
        mixedWord = shuffle(word);
        stopGame = false;
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        if("еще".equals(message) || "ещё".equals(message))
        {
            resetGame();
            return sendWord();
        }
        else if("начать".equals(message))
        {
            stopGame = false;
            return sendWord();
        }
        else if("стоп".equals(message))
        {
            stopGame = true;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        else if("статистика".equals(message))
        {
            return user.StatisticToString();
        }
        else if (attempts != 0)
        {
            if(message.equals(word))
            {
                resetGame();
                user.statistic.get("Слова")[0] += 1;
                return "Верно! Для того, чтобы сыграть еще раз напишите ЕЩЁ.";
            }

            attempts--;

            if (attempts == 1)
                return String.format("Попробуйте еще! \nОсталось %s попытка.", attempts);

            else if (attempts == 0)
            {
                user.statistic.get("Слова")[1] += 1;
                return "К сожалению попытки закончились. Вы проиграли:(\n " +
                        "Я загадывал слово " + word + ".\n" +
                        "Для того, чтобы сыграть еще раз напишите ЕЩЁ.";
            }

           return String.format("Попробуйте еще! \nОсталось %s попытки.", attempts);
        }
       return "Не понял!";
    }
}
