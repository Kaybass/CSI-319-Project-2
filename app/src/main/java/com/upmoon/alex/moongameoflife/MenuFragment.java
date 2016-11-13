package com.upmoon.alex.moongameoflife;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MenuFragment extends Fragment {

    private static final int REQUEST_GAME = 0;

    private TextView menuText;

    private Button newGameButton, loadLocalButton, loadOnlineButton, resetLocalButton;

    private static final String[] things = new String[] {"kek","meme","wwwwwww"};

    private int i = 0;

    private static boolean paused = true;

    private static int timer = 500;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuText         = (TextView) view.findViewById(R.id.menu_text);
        newGameButton    = (Button)   view.findViewById(R.id.new_game);
        loadLocalButton  = (Button)   view.findViewById(R.id.load_local);
        loadOnlineButton = (Button)   view.findViewById(R.id.load_online);


        /*
        * This is me playing around with changing the ui
        *
        * */

        if(newGameButton == null || loadLocalButton == null || loadOnlineButton == null)
            Log.d("sda","asd");

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(!isPaused())
                                updateUI();
                        }
                    }) ;


                    //game logic

                    try {
                        Thread.sleep(threadTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        t.start();


        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipPause();
                return;
            }
        });

        loadLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTimer();
                return;
            }
        });/*

        loadOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return;
            }
        });

        resetLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return;
            }
        });*/

        return view;
    }

    public void updateUI(){
        menuText.setText(things[i]);
        if(i>=2)
            i = 0;
        else
            i++;
    }

    public static boolean isPaused(){
        return paused;
    }
    public static void flipPause(){
        paused = !paused;
    }

    public static int threadTime(){
        return timer;
    }

    public static void changeTimer(){
        if(timer == 500)
            timer = 1000;
        else
            timer = 500;
    }
}
