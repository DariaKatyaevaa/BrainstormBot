package oop.gamebot.games.calculate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameCalculateUnit4Test extends Assert {

    @Before
    public void setUp(){
        var q = new GameCalculate(new User);
        var wrongAnswer = (Integer.MAX_VALUE - q.answer).toString();
        q.startGame();
    }

    @Test
    public void testgiveAnswerToUser(){
        assertEquals(String.format("Попробуйте еще! \nОсталось %s попытки.", q.attempts), q.giveAnswerToUser(wrongAnswer));
        assertEquals(String.format("Попробуйте еще! \nОсталось %s попытки.", q.attempts), q.giveAnswerToUser(wrongAnswer));
        assertEquals(String.format("Попробуйте еще! \nОсталось %s попытки.", q.attempts), q.giveAnswerToUser(wrongAnswer));
        assertEquals(String.format("Попробуйте еще! \nОсталась %s попытка.", q.attempts), q.giveAnswerToUser(wrongAnswer));
        assertEquals("К сожалению попытки закончились. Вы проиграли:(\n " +
                "Ответ " + q.answer + ".\n" +
                "Для того, чтобы сыграть еще раз напишите ЕЩЁ.", q.giveAnswerToUser(wrongAnswer));
        assertEquals(q.giveAnswerToUser("еще"), q.equation);
        assertEquals(q.giveAnswerToUser(q.answer),"Верно! Для того, чтобы сыграть еще раз напишите ЕЩЁ.");
    }
}