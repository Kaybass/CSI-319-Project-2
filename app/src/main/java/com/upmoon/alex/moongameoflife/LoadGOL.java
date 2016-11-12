package com.upmoon.alex.moongameoflife;

/**
 * Created by Alex on 10/29/2016.
 */

public interface LoadGOL {

    String[] loadBoardNames();

    GameOfLifeBoard loadBoard(String id);
}
