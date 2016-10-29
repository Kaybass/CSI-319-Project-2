package com.upmoon.alex.moongameoflife;

/**
 * Created by Alex on 10/29/2016.
 */
public class GameOfLifeBoard {
    private static GameOfLifeBoard ourInstance = new GameOfLifeBoard();

    public static GameOfLifeBoard getInstance() {
        return ourInstance;
    }

    private GameOfLifeBoard() {
    }
}
