package oop.gamebot.games.calculate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameCalculateUnit4Test extends Assert {

    @Before
    public void setUp(){
        var q = new GameCalculate(new User);
    }

    @Test
    public void teststartGame(){
        assertEquals("Отлично!\n" + "\n" +
                "В этой игре вам всего лишь нужно получить ответ выражения.\n" +
                "Для того, чтобы прекратить игру, напиши СТОП\n" +
                "Давайте играть!\n", q.startGame());
    }

    @Test
    public void testgiveAnswerToUser1() {
        q.startGame();
        var wrongAnswer = (Integer.MAX_VALUE - q.answer).toString();
        assertEquals(String.format("Попробуйте еще! \nОсталось %s попытки.", q.attempts), q.giveAnswerToUser(wrongAnswer));
    }

    @Test
    public void testgiveAnswerToUser2() {
        q.startGame();
        assertEquals("Верно! Для того, чтобы сыграть еще раз напишите ЕЩЁ.",q.giveAnswerToUser(q.answer));
    }

    @Test
    public void testgiveAnswerToUser3() {
        assertEquals("Не понял!", "какой-то текст");
    }


}