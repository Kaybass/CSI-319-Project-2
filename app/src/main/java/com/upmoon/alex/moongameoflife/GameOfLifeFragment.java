package com.upmoon.alex.moongameoflife;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameOfLifeFragment extends Fragment {

    private RecyclerView mGOLBoard;
    private CellAdapter mAdapter;

    private Button mPauseButton, mResetButton, mCloneButton,
            mSaveLocalButton, mShareButton, mThemeButton;

    private static int mTimer = 800;
    private volatile boolean mPaused = true;

    private Thread mThread;

    private volatile boolean mRunning = true;

    private static boolean mPulse = true;//highkey things that I lack

    private Boolean[][] mResetCopy;

    private String mColorAlive, mColorDead;

    private boolean mCurrentThemeGreen;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_of_life, container, false);

        mCurrentThemeGreen = true;
        mColorAlive = "#5ff442";
        mColorDead = "#6b6b6b";

        mResetCopy = new Boolean[CurrentBoard.getInstance().getVerticalLength()]
                [CurrentBoard.getInstance().getHorizontalLength()];

        saveCopy();

        mPaused = true;

        mGOLBoard = (RecyclerView) view.findViewById(R.id.gol_recycler_view);
        mGOLBoard.setLayoutManager(new GridLayoutManager(getActivity(),
                CurrentBoard.getInstance().getHorizontalLength()));

        mAdapter = new CellAdapter(CurrentBoard.getInstance().getVerticalLength(),
                CurrentBoard.getInstance().getHorizontalLength());
        mGOLBoard.setAdapter(mAdapter);

        mPauseButton = (Button) view.findViewById(R.id.gol_pause_);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused()){
                    saveCopy();
                }
                flipPaused();
            }
        });

        mResetButton = (Button) view.findViewById(R.id.gol_reset_);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused()){
                    pushCopy();
                    CurrentBoard.getInstance().resetGenerations();
                    mAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "Pause first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCloneButton = (Button) view.findViewById(R.id.gol_clone_);
        mCloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused()) {
                    saveCopy();
                    CurrentBoard.getInstance().resetGenerations();
                    startActivity(new Intent(getActivity(), GameOfLifeActivity.class));
                }

            }
        });

        mSaveLocalButton = (Button) view.findViewById(R.id.gol_save_local_);
        mSaveLocalButton.setOnClickListener(new View.OnClickListener() {
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
                        SaveGOLoffline saveGame = new SaveGOLoffline();

                        saveGame.saveBoard(getContext(), CurrentBoard.getInstance().getBoard(), (int)id);
                    }
                });

                dialog.show();
            }
        });

        mShareButton = (Button) view.findViewById(R.id.gol_share_);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setDrawingCacheEnabled(true);
                ConvertBoardToImage convertBoardToImage = new ConvertBoardToImage();
                Bitmap boardBitmap = getView().getDrawingCache();
                convertBoardToImage.saveBoardAsPNG(getContext(), boardBitmap);

                File imagePath = new File(getContext().getCacheDir(), "images");
                File newFile = new File(imagePath, "image.png");
                Uri contentUri = FileProvider.getUriForFile(getContext(), "com.upmoon.alex.moongameoflife.fileprovider", newFile);

                if (contentUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, "Share my Game of Life Board"));
                }
            }
        });

        mThemeButton = (Button) view.findViewById(R.id.gol_theme_change_);
        mThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused()){
                    if(mCurrentThemeGreen){
                        mColorAlive = "#FF3B3B";
                        mColorDead  = "#060505";
                    } else{
                        mColorAlive = "#5ff442";
                        mColorDead  = "#6b6b6b";
                    }
                    mCurrentThemeGreen = !mCurrentThemeGreen;
                    mAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "Pause first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(mRunning) {
                    if(!isPaused()){

                        //Draw board
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                /*
                                * Redraws the entire layout, really slow
                                * */
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                        //update game
                        CurrentBoard.getInstance().update();
                        flipPulse();
                        Log.d("Worker Thread", "Successful run");
                    }

                    //Sleep
                    try {
                        Thread.sleep(threadTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        mThread.start();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        pushCopy();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(!isPaused())
            flipPaused();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        CurrentBoard.getInstance().resetGenerations();
        mRunning = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    /*
     *
     *  static thread functions
     */

    public boolean isPaused(){
        return mPaused;
    }

    public void flipPaused(){
        mPaused = !mPaused;
    }

    public static int threadTime(){
        return mTimer;
    }

    public static void setTimer(int mili){
        mTimer = mili;
    }

    public static boolean getPulse(){
        return mPulse;
    }

    public static void flipPulse(){
        mPulse = !mPulse;
    }

    public void saveCopy(){
        for(int i = 0; i < CurrentBoard.getInstance().getVerticalLength(); i++){
            for(int j = 0; j < CurrentBoard.getInstance().getHorizontalLength(); j++){
                mResetCopy[i][j] = CurrentBoard.getInstance().getCellStatus(i,j);
            }
        }
    }

    public void pushCopy(){
        for(int i = 0; i < CurrentBoard.getInstance().getVerticalLength(); i++){
            for(int j = 0; j < CurrentBoard.getInstance().getHorizontalLength(); j++){
                CurrentBoard.getInstance().setCell(i,j,mResetCopy[i][j]);
            }
        }
        CurrentBoard.getInstance().resetGenerations();
    }

    /*
    *
    * Recycler classes
    * */
    private class CellHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

        private boolean mAlive = false;
        private int mHorPos, mVerPos;

        private TextView mCell;

        public CellHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mCell = (TextView) itemView.findViewById(R.id.gol_cell_item);
        }

        public void bindCell(int v, int h){
            mAlive = CurrentBoard.getInstance().getCellStatus(v,h);
            mHorPos = h;
            mVerPos = v;
            if(mAlive) {
                if(getPulse())
                    mCell.setText("8");
                else
                    mCell.setText("*");
                mCell.setTextColor(Color.parseColor(mColorAlive));
            }
            else {
                mCell.setText("0");
                mCell.setTextColor(Color.parseColor(mColorDead));
            }
        }

        @Override
        public void onClick(View v){
            Log.d("View Holder", "That Tickles!");
            if(isPaused()){
                Log.d("View Holder", "Changing things");
                CurrentBoard.getInstance().flipCell(mVerPos,mHorPos);
                mAlive = !mAlive;
                if(mAlive) {
                    if(getPulse())
                        mCell.setText("8");
                    else
                        mCell.setText("*");
                    mCell.setTextColor(Color.parseColor(mColorAlive));
                }
                else {
                    mCell.setText("0");
                    mCell.setTextColor(Color.parseColor(mColorDead));
                }
            }
        }
    }

    private class CellAdapter extends RecyclerView.Adapter<CellHolder>{

        private int rows, columns;

        public CellAdapter(int r, int c){

            rows = r;
            columns = c;
        }

        @Override
        public CellHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater LI = LayoutInflater.from(getActivity());
            View view = LI.inflate(R.layout.gol_grid_item,parent,false);
            return new CellHolder(view);
        }

        @Override
        public void onBindViewHolder(CellHolder holder, int position){

            //this math is not wrong
            int vPosition = position / columns;

            int hPosition = position % columns;

            //Log.d("BIND VIEW HOLDER", Integer.toString(vPosition) + " " + Integer.toString(hPosition) );

            holder.bindCell(vPosition,hPosition);
        }

        @Override
        public int getItemCount(){
            //this math is right
            return rows * columns;
        }
    }
}
