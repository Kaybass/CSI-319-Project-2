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

    public void update(){
        mBoard.updateBoard();
    }

    public void flipCell(int v, int h){
        mBoard.flipCellStatus(v,h);
    }

    public void setCell(int r, int c, boolean b){
        mBoard.setCell(r,c,b);
    }

    public void resetGenerations() {mBoard.setGeneration(0);}

    public int getHorizontalLength(){
        return mBoard.getColumns();
    }

    public int getVerticalLength(){
        return mBoard.getRows();
    }

    public boolean getCellStatus(int r, int c){
        return mBoard.cellStatus(r,c);
    }
}
