import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameWords implements Game
{
    private String word;
    private String mixedWord;
    private String[] wordList;
    private int gameCount = 5;
    boolean stopGame = true;

    GameWords() throws FileNotFoundException
    {
        String path = "C:\\Users\\daria\\IdeaProjects\\GameBot\\content\\words";
        wordList = new Scanner(new File(path)).useDelimiter("\\Z").next().split("\n");
        word = getRandomWord(wordList);
        mixedWord = shuffle(word);
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
        return words[randPicker];
    }

    @Override
    public String startGame()
    {
        return "Отлично!\n" + "\n" +
                "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                "Если вы не справитесь за 5 попыток, то я победил!\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n" +
                "Начинаем игру!\n";
    }

    public String sendWord()
    {
        return mixedWord;
    }

    @Override
    public void resetGame()
    {
        gameCount = 5;
        word = getRandomWord(wordList);
        mixedWord = shuffle(word);
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        if(message.equals("еще") || message.equals("ещё"))
        {
            resetGame();
            return sendWord();
        }
        if(message.equals("стоп"))
        {
            stopGame = false;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        if (gameCount != 0)
        {
            if(message.equals(word))
            {
                resetGame();
                return "Верно! Для того, чтобы сыграть еще раз напишите ЕЩЁ.";
            }

            gameCount--;

            if (gameCount == 1)
                return String.format("Попробуйте еще! \nОсталось %s попытка.", gameCount);

            if (gameCount == 0)
                return "К сожалению попытки закончились. Вы проиграли:(\n " +
                        "Для того, чтобы сыграть еще раз напишите ЕЩЁ.";

           return String.format("Попробуйте еще! \nОсталось %s попытки.", gameCount);
        }
       return "Не понял!";
    }
}
