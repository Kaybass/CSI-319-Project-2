package com.upmoon.alex.moongameoflife;

import java.io.Serializable;

/**
 * Created by Joe Listro on 11/16/2016.
 */

public class serializableBooleanGrid implements Serializable {

    private boolean gridArray[][];

    public serializableBooleanGrid() {
        super();

    }

    public void saveGrid(boolean[][] newGridArray){
        gridArray = newGridArray;
        return;
    }

    public boolean[][] loadGrid() {
        return gridArray;
    }
}
