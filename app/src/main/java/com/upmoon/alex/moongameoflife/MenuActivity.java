package com.upmoon.alex.moongameoflife;

import android.support.v4.app.Fragment;

public class MenuActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new MenuFragment();
    }
}
