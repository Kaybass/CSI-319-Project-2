package com.upmoon.alex.moongameoflife;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Alex on 10/29/2016.
 */

public class SaveGOLoffline implements SaveGOL  {

    public SaveGOLoffline(){

    }

    public int saveBoard(Context context, GameOfLifeBoard boardToSave, int saveSlot){
        String fileName = "gameOfLife" + Integer.toString(saveSlot);

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(boardToSave);

            objectOutputStream.close();
            fileOutputStream.close();

            Toast toast = Toast.makeText(context, "Game Saved Successfully!", Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
