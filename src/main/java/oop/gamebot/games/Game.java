package oop.gamebot.games;

public interface Game {
    String startGame();
    void resetGame();
    String giveAnswerToUser(String message);
}