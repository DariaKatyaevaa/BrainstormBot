package oop.gamebot;


import oop.gamebot.User;
import oop.gamebot.games.calculate.GameCalculate;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class GameCalculateTest {

    @Before
    public void setUp(){

    }

    @Test
    public void GenerateTest() throws FileNotFoundException {
        GameCalculate gameCalculate = new GameCalculate(new User(new Long(0)));
    }
}