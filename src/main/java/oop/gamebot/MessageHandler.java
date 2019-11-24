package oop.gamebot;

import java.util.*;


class MessageHandler
{
    private List<String> answersStory = new LinkedList<>();
    private String[] games = new String[] {"Слова", "Арифметика", "Города"};
    boolean startGame = false;
    boolean startGameTG = false;
    boolean stopGame = false;
    private User user;
    String game;

    MessageHandler(User user){
        this.user = user;
    }

    String getAnswer(String message) {
        if("/start".equals(message) || "привет".equals(message))
        {
            answersStory.add("/start");
            return "Привет!\n" +
                    "Я игровой бот, который заставит ваши мозги шевелиться!\n" +
                    "Давайте поиграем:)\n\nНапишите ИГРЫ, чтобы посмотреть список игр.\n" +
                    "Напишите СТАТИСТИКА после игры, если хотите узнать количество\nсвоих побед и поражений.";
        }
        if(message.equals("игры"))
        {
            startGameTG = true;
            answersStory.add(message);
            return Arrays.toString(games) +
                    "\n\n Напишите название игры, в которую вы бы хотели поиграть";
        }
        if("слова".equals(message))
        {
            startGame = true;
            game = "Слова";
            answersStory.add("слова");
            return "Отлично!\n" + "\n" +
                    "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                    "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                    "Если вы не справитесь за 5 попыток, то я победил!\n" +
                    "Для того, чтобы прекратить игру, напиши СТОП\n" +
                    "Давайте играть!\n";
        }
        if("города".equals(message))
        {
            startGame = true;
            game = "Города";
            answersStory.add("города");
            return "Отлично!\n" + "\n" +
                    "Правила нашей игры очень простые: вы должны написать город, название которого начинается на букву, которой заканчивается предыдущее название города.\n" +
                    "Названий городов на Ъ и Ь знак нет. Поэтому напишите город на букву, стоящую перед Ъ и Ь знаком.\n" +
                    "Названия городов не должны повторяться.\n" +
                    "Для того, чтобы прекратить игру, напиши СТОП\n" +
                    "Давайте играть!\n";
        }
        if("арифметика".equals(message))
        {
            startGame = true;
            game = "Арифметика";
            answersStory.add("арифметика");
            return "Отлично!\n" + "\n" +
                    "В этой игре вам всего лишь нужно сосчитать ответ выражения.\n" +
                    "Для того, чтобы прекратить игру, напиши СТОП\n" +
                    "Давайте играть!\n";
        }
        if("статистика".equals(message))
        {
            return user.StatisticToString();
        }

        return "Не понял!";
    }
}