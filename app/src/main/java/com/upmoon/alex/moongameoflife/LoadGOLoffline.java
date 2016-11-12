package com.upmoon.alex.moongameoflife;

/**
 * Created by Alex on 10/29/2016.
 */

public class LoadGOLoffline implements LoadGOL {

    public LoadGOLoffline(){

    }

    public String[] loadBoardNames(){

        //Not implemented
        return new String[] {};
    }

    public GameOfLifeBoard loadBoard(String id){

        return new GameOfLifeBoard();
    }
}
