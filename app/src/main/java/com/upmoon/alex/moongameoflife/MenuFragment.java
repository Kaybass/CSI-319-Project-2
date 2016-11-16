package com.upmoon.alex.moongameoflife;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

                CurrentBoard.getInstance().flipCell(2,2);
                CurrentBoard.getInstance().flipCell(2,3);
                CurrentBoard.getInstance().flipCell(3,2);
                CurrentBoard.getInstance().flipCell(2,1);
                CurrentBoard.getInstance().flipCell(1,2);
                CurrentBoard.getInstance().flipCell(10,2);
                CurrentBoard.getInstance().flipCell(10,3);
                CurrentBoard.getInstance().flipCell(10,5);
                CurrentBoard.getInstance().flipCell(10,6);
                CurrentBoard.getInstance().flipCell(10,4);

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
                final String[] stringArray = new String[] { "Example Grid 1", "Ship", "Golden Goat", "Loaf", "Boat" };
                ArrayAdapter<String> gridAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                localSavedGrids.setAdapter(gridAdapter);

                builder.setView(localSavedGrids);
                final Dialog dialog = builder.create();

                localSavedGrids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Toast toast = Toast.makeText(getActivity(), stringArray[(int)id], LENGTH_SHORT);
                        toast.show();
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
                String[] stringArray = new String[] { "Example Online Grid 1", "Glider", "Lightweight Space Ship", "Square" };
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
