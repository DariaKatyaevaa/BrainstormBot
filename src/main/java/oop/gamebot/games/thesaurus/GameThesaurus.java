package oop.gamebot.games.thesaurus;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import oop.gamebot.User;
import oop.gamebot.games.Game;


public class GameThesaurus implements Game
{
    private User user;
    private boolean stopGame = false;
    private JSONParser jsonParser = new JSONParser();
    private JSONObject thesaurus;
    private int correctAnswer;
    private String correctAnswerStr;
    private Map<Integer, String> question;

    public GameThesaurus(User user) throws IOException, ParseException {
        FileReader reader = new FileReader(new File(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("thesaurus.json")).getFile()));
        this.thesaurus = (JSONObject) jsonParser.parse(reader);
        this.user = user;
        this.question = createMap();
    }

    private Map<Integer, String> createMap()
    {
        String[] answers = getRandomWords();
        Map<Integer, String> map = new HashMap<>();
        if (correctAnswer == 1)
        {
            JSONObject object = (JSONObject) thesaurus.get(answers[0]);
            String string = object.get("word") + " - " + object.get("definition");
            map.put(1, string);
            JSONObject object_second = (JSONObject) thesaurus.get(answers[1]);
            JSONObject object_third = (JSONObject) thesaurus.get(answers[2]);
            String string_second = object_second.get("word") + " - " + object_third.get("definition");
            String string_third = object_third.get("word") + " - " + object_second.get("definition");
            map.put(2, string_second);
            map.put(3, string_third);

        }
        else if (correctAnswer == 2)
        {
            JSONObject object = (JSONObject) thesaurus.get(answers[1]);
            String string = object.get("word") + " - " + object.get("definition");
            map.put(2, string);
            JSONObject object_second = (JSONObject) thesaurus.get(answers[0]);
            JSONObject object_third = (JSONObject) thesaurus.get(answers[2]);
            String string_second = object_second.get("word") + " - " + object_third.get("definition");
            String string_third = object_third.get("word") + " - " + object_second.get("definition");
            map.put(1, string_second);
            map.put(3, string_third);
        }
        else {
            JSONObject object = (JSONObject) thesaurus.get(answers[2]);
            String string = object.get("word") + " - " + object.get("definition");
            map.put(3, string);
            JSONObject object_second = (JSONObject) thesaurus.get(answers[1]);
            JSONObject object_third = (JSONObject) thesaurus.get(answers[0]);
            String string_second = object_second.get("word") + " - " + object_third.get("definition");
            String string_third = object_third.get("word") + " - " + object_second.get("definition");
            map.put(2, string_second);
            map.put(1, string_third);
        }
        return map;
    }

    public String getQuestion()
    {
        return "1. " + question.get(1) + "\n" +
                "2. " + question.get(2) + "\n" +
                "3. " + question.get(3);
    }

    public boolean isStopGame()
    {
        return stopGame;
    }

    private String[] getRandomWords()
    {
        Random random = new Random();
        int firstWord = random.nextInt(thesaurus.size());
        int secondWord = random.nextInt(thesaurus.size());
        int thirdWord = random.nextInt(thesaurus.size());

        correctAnswer = random.nextInt((3 - 1) + 1) + 1;
        correctAnswerStr = String.valueOf(correctAnswer);
        String[] answers = new String[3];
        answers[0] = String.valueOf(firstWord);
        answers[1] = String.valueOf(secondWord);
        answers[2] = String.valueOf(thirdWord);
        return answers;
    }

    @Override
    public String startGame()
    {
        resetGame();
        return "Отлично!\n" + "\n" +
                "Правила игры: вам нужно выбрать правильное определения\n" +
                "для слова, которое вам будет предложенно.\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n" +
                "Начинаем игру!\n";
    }

    @Override
    public void resetGame()
    {
        stopGame = false;
        question = createMap();
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        if("начать".equals(message))
        {
            stopGame = false;
            return getQuestion();
        }
        else if("стоп".equals(message))
        {
            stopGame = true;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        else if("другой".equals(message))
        {
            resetGame();
            return getQuestion();
        }
        else if("статистика".equals(message))
        {
            return user.StatisticToString();
        }
        else if (message.equals(correctAnswerStr))
        {
            resetGame();
            user.setStatistic("Энциклопедия", "win");
            return "Верно! \n" + getQuestion();

        }
        else {
            user.setStatistic("Энциклопедия", "lose");
            String correct = correctAnswerStr;
            resetGame();
            return "Неверно! Правильный ответ " + correct + ".\n" + getQuestion();
        }
    }
}
