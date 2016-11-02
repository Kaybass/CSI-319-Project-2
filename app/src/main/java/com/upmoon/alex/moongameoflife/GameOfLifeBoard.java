package com.upmoon.alex.moongameoflife;

/**
 * Created by Alex on 10/29/2016.
 */
public class GameOfLifeBoard {
    private static GameOfLifeBoard ourInstance = new GameOfLifeBoard();

    public static GameOfLifeBoard getInstance() {
        return ourInstance;
    }

    /* Hold the variable dimensions of the board. */
    private int rows, columns;
    private int generations; // Current generation of this game

    /* Create 2D array to hold the cells */
    private GameOfLifeCell [][] cells;

    private GameOfLifeBoard() {
    }

    /* Save game state */
    public void saveState() {
        return;
    }
}
