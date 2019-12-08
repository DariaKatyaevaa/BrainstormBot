package oop.gamebot.games.calculate;

import oop.gamebot.User;
import oop.gamebot.games.Game;

import java.util.HashMap;
import java.util.function.BinaryOperator;

public class GameCalculate implements Game
{
    private String equation;
    private HashMap<Integer, BinaryOperator<Integer>> operations = new HashMap<>();
    private HashMap<Integer, String> stringOperations = new HashMap<>();
    private int first;
    private int second;
    private int operation;
    private String answer;
    private boolean stopGame = false;
    private int attempts;
    private User user;


    public GameCalculate(User user)
    {
        CreateMap();
        resetGame();
        this.user = user;
    }

    private void CreateMap()
    {
        operations.put(0, (Integer::sum));
        operations.put(1, (x, y) -> x - y);
        operations.put(2, (x, y) -> x * y);
        operations.put(3, (x, y) -> x / y);
        stringOperations.put(0, " + ");
        stringOperations.put(1, " - ");
        stringOperations.put(2, " * ");
        stringOperations.put(3, " / ");
    }

    public boolean isStopGame()
    {
        return stopGame;
    }

    private void GenerateNumbers()
    {
        operation = (int) (Math.random() * 4);
        if (operation == 3)
        {
            first = (int) (Math.random() * 100);
            second = (int) (Math.random() * 50);
            if(first % second !=0)
                second = second - (first % second);
        }
        else if (operation == 2)
        {
            first = (int) (Math.random() * 50);
            second = (int) (Math.random() * 10);
        }
        else {
            first = (int) (Math.random() * 500);
            second = (int) (Math.random() * 500);
        }
    }

    private void CreateEquation()
    {
        equation = first + stringOperations.get(operation) + second;
        BinaryOperator<Integer> func = operations.get(operation);
        answer = Integer.toString(func.apply(first, second));
    }

    @Override
    public String startGame()
    {
        stopGame = false;
        return "Отлично!\n" + "\n" +
            "В этой игре вам всего лишь нужно получить ответ выражения.\n" +
            "Для того, чтобы прекратить игру, напиши СТОП\n" +
            "Давайте играть!\n";
    }
    public String sendEquation()
    {
        return equation;
    }

    @Override
    public void resetGame()
    {
        attempts = 5;
        GenerateNumbers();
        CreateEquation();
        stopGame = false;
    }

    @Override
    public String giveAnswerToUser(String message)
    {
        if("начать".equals(message))
        {
            stopGame = false;
            return sendEquation();
        }
        else if("еще".equals(message) || "ещё".equals(message))
        {
            resetGame();
            return equation;
        }
        else if("статистика".equals(message))
        {
            return user.StatisticToString();
        }
        else if("стоп".equals(message))
        {
            stopGame = true;
            return "Хорошо, спасибо за игру!\n\n" +
                    "Если хотите поиграть в другую игру, напишите название игры или " +
                    "напишите ИГРЫ, чтобы получить список игр.";
        }
        else if (attempts != 0)
        {
            if(message.equals(answer))
            {
                user.setStatistic("Арифметика", "win");
                return "Верно! Для того, чтобы сыграть еще раз напишите ЕЩЁ.";
            }

            attempts--;

            if (attempts == 1)
                return String.format("Попробуйте еще! \nОсталось %s попытка.", attempts);

            else if (attempts == 0)
            {
                user.setStatistic("Арифметика", "lose");
                return "К сожалению попытки закончились. Вы проиграли:(\n " +
                        "Ответ " + answer + ".\n" +
                        "Для того, чтобы сыграть еще раз напишите ЕЩЁ.";
            }

            return String.format("Попробуйте еще! \nОсталось %s попытки.", attempts);
        }
        return "Не понял!";
    }

}
