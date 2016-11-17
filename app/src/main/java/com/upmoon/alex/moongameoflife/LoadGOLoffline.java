package com.upmoon.alex.moongameoflife;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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

    public GameOfLifeBoard loadBoard(Context context, String id) {
        String fileName = "gameOfLife" + id;

        GameOfLifeBoard boardToLoad = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            boardToLoad = (GameOfLifeBoard) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch(IOException | ClassNotFoundException e) {
            return null;
            //e.printStackTrace();
        }

        return boardToLoad;
    }
}
