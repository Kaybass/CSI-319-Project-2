package com.upmoon.alex.moongameoflife;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


public class MenuFragment extends Fragment {

    private static final int REQUEST_GAME = 0;

    private TextView menuText;

    private Button newGameButton, loadLocalButton, loadOnlineButton, resetLocalButton;

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

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentBoard.getInstance().setBoard(new GameOfLifeBoard(22,22));

                startActivity(new Intent(getActivity(), GameOfLifeActivity.class));
                return;
            }
        });

        loadLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Load Locally Saved Grid");

                ListView localSavedGrids = new ListView(getActivity());
                final String[] stringArray = new String[] { "Save Slot 1", "Save Slot 2", "Save Slot 3"};
                ArrayAdapter<String> gridAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                localSavedGrids.setAdapter(gridAdapter);

                builder.setView(localSavedGrids);
                final Dialog dialog = builder.create();

                localSavedGrids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        LoadGOLoffline loadGame = new LoadGOLoffline();

                        GameOfLifeBoard imaboard = loadGame.loadBoard(getContext(), Integer.toString((int)id));

                        if(imaboard != null) {

                            CurrentBoard.getInstance().setBoard(imaboard);

                            startActivity(new Intent(getActivity(), GameOfLifeActivity.class));
                        }
                        else{
                            Toast.makeText(getActivity(), "No save exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();

                return;
            }
        });

        loadOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Load Saved Grids Online");

                ListView onlineSavedGrids = new ListView(getActivity());
                final String[] stringArray = new String[] { "Not implemented sorry David" };
                ArrayAdapter<String> gridAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                onlineSavedGrids.setAdapter(gridAdapter);

                onlineSavedGrids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Toast toast = Toast.makeText(getActivity(), stringArray[(int)id], LENGTH_SHORT);
                        toast.show();
                    }
                });

                builder.setView(onlineSavedGrids);
                final Dialog dialog = builder.create();

                dialog.show();
            }
        });

        /*
        resetLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return;
            }
        });*/

        return view;
    }
}
