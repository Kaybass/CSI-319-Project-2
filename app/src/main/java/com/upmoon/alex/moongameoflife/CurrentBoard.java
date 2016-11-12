package com.upmoon.alex.moongameoflife;

/**
 *
 * Current Board holds the board for the app
 */
public class CurrentBoard {

    private static CurrentBoard ourInstance;

    private GameOfLifeBoard mBoard;

    public static CurrentBoard getInstance() {

        if(ourInstance == null){
            ourInstance = new CurrentBoard();
            return ourInstance;
        }
        else{
            return ourInstance;
        }
    }

    private CurrentBoard() {
    }

    public void setBoard(GameOfLifeBoard board){
        mBoard = board;
    }

    public GameOfLifeBoard getBoard(){
        return mBoard;
    }
}
