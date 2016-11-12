package com.upmoon.alex.moongameoflife;


import android.support.v4.app.Fragment;

public class GameOfLifeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new GameOfLifeFragment();
    }
}
