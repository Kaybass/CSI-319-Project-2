package com.upmoon.alex.moongameoflife;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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

        menuText = (TextView) view.findViewById(R.id.menu_text);
        newGameButton = (Button) view.findViewById(R.id.new_game);
        loadLocalButton = (Button) view.findViewById(R.id.load_local);
        loadOnlineButton = (Button) view.findViewById(R.id.load_online);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return;
            }
        });

        loadLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                loadFragment dialog = loadFragment.newInstance();
                dialog.setTargetFragment(MenuFragment.this, REQUEST_GAME);
                dialog.show(manager);
            }
        });

        loadOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return;
            }
        });

        resetLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO: Reset locally saved games */
                return;
            }
        });

        return view;
    }
}
