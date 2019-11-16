import java.util.*;

class MessageHandler
{
    private List<String> answersStory = new LinkedList<>();
    private String[] games = new String[] {"Слова", "Арифметика", "Города"};
    boolean startGame = false;
    boolean isPlaying = false;
    boolean stopGame = false;
    String game;

    MessageHandler(){}

     String getAnswer(String message) {
        if(message.equals("/start") || message.equals("привет"))
        {
            answersStory.add("/start");
            return "Привет!\n" +
                    "Я игровой бот, который заставит ваши мозги шевелиться!\n" +
                    "Давайте поиграем:)\n\nНапишите ИГРЫ, чтобы посмотреть список игр.";
        }
        if(message.equals("игры"))
        {
            answersStory.add(message);
            return Arrays.toString(games) +
                    "\n\n Напишите название игры, в которую вы бы хотели поиграть";
        }
         if(message.equals("слова"))
         {
             startGame = true;
             game = "Слова";
             answersStory.add("слова");
             return "Отлично!\n" + "\n" +
                     "Правила нашей игры очень простые: перед Вами появится набор букв.\n" +
                     "Для того, чтобы выиграть вам нужно составить из этих букв слово.\n" +
                     "Если вы не справитесь за 5 попыток, то я победил!\n" +
                     "Для того, чтобы прекратить игру, напиши СТОП\n" +
                     "Начинаем игру!\n";
         }
         if(message.equals("города"))
         {
             startGame = true;
             game = "Города";
             answersStory.add("города");
             return "Отлично!\n" + "\n" +
                     "Правила нашей игры очень простые: вы должны написать город, название которого начинается на букву, которой заканчивается предыдущее название города.\n" +
                     "Названий городов на Ъ и Ь знак нет. Поэтому напишите город на букву, стоящую перед Ъ и Ь знаком.\n" +
                     "Названия городов не должны повторяться.\n" +
                     "Для того, чтобы прекратить игру, напиши СТОП\n" +
                     "Начинаем игру!\n";
         }

        return "Не понял!";
    }
}