package com.upmoon.alex.moongameoflife;

import android.content.Context;

/**
 * Created by Alex on 10/29/2016.
 */

public interface LoadGOL {

    String[] loadBoardNames();

    GameOfLifeBoard loadBoard(Context context, String id);
}
