package com.upmoon.alex.moongameoflife;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Joseph Listro on 11/11/16.
 */

public class loadFragment extends DialogFragment {

    public static final String EXTRA_GAME =
            "com.upmoon.alex.moongameoflife.game";


    private static final String ARG_GAME = "game";
    private ListView mGamePicker;
    public static loadFragment newInstance() {
        Bundle args = new Bundle();

        loadFragment fragment = new loadFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_loader, null);

        mGamePicker = (ListView) v.findViewById(R.id.dialog_game_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Select a saved game.")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int gameID = -1;
                                /* TODO: ListView sets game value */
                                sendResult(Activity.RESULT_OK, gameID);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int savedGameID) {
        if (getTargetFragment() == null) {
            return;
        }

        if(savedGameID == -1) /* No game was selected */
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_GAME, savedGameID);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
