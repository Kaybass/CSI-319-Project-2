package com.upmoon.alex.moongameoflife;

/**
 * Created by Alex on 10/29/2016.
 */

public class LoadGOLonline implements LoadGOL {

    //TODO add connection stuff

    public LoadGOLonline(String ip){

    }

    public String[] loadBoardNames(){

        //Not implemented
        return new String[] {};
    }

    public GameOfLifeBoard loadBoard(String id){

        return new GameOfLifeBoard();
    }
}
