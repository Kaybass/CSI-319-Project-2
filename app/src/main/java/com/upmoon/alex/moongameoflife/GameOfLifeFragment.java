package com.upmoon.alex.moongameoflife;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameOfLifeFragment extends Fragment {

    private RecyclerView mGOLBoard;
    private CellAdapter mAdapter;

    private static int mTimer = 1000;
    private static boolean mPaused = true;

    private Thread mThread;

    private volatile boolean mRunning = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_of_life, container, false);

        mGOLBoard = (RecyclerView) view.findViewById(R.id.gol_recycler_view);
        mGOLBoard.setLayoutManager(new GridLayoutManager(getActivity(),
                CurrentBoard.getInstance().getHorizontalLength()));

        boolean[][] cellStatuses = new boolean[CurrentBoard.getInstance().getVerticalLength()]
                                            [CurrentBoard.getInstance().getHorizontalLength()];
        for (int i = 0; i < CurrentBoard.getInstance().getVerticalLength(); i++){
            for(int j = 0; j < CurrentBoard.getInstance().getHorizontalLength(); j++){
                cellStatuses[i][j] = CurrentBoard.getInstance().getCellStatus(i,j);
            }
        }



        mAdapter = new CellAdapter(cellStatuses);
        mGOLBoard.setAdapter(mAdapter);

        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(mRunning) {
                    if(!isPaused()){
                        CurrentBoard.getInstance().update();
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        }) ;
                    }

                    Log.d("Worker Thread", "Successful run");

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
     *
     */

    public static boolean isPaused(){
        return mPaused;
    }

    public static void flipPaused(){
        mPaused = !mPaused;
    }

    public static int threadTime(){
        return mTimer;
    }

    public static void setTimer(int mili){
        mTimer = mili;
    }

    //
    private class CellHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

        private boolean mAlive = false;
        private int mHorPos, mVerPos;

        private TextView mCell;

        public CellHolder(View itemView){
            super(itemView);

            mCell = (TextView) itemView.findViewById(R.id.gol_cell_item);
        }

        public void bindCell(boolean alive, int v, int h){
            mAlive = alive;
            mHorPos = h;
            mVerPos = v;
            if(mAlive) {
                mCell.setText("*");
                mCell.setTextColor(Color.parseColor("#5ff442"));
            }
            else {
                mCell.setText("0");
                mCell.setTextColor(Color.parseColor("#6b6b6b"));
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
                    mCell.setText("*");
                    mCell.setTextColor(Color.parseColor("#5ff442"));
                }
                else {
                    mCell.setText("0");
                    mCell.setTextColor(Color.parseColor("#6b6b6b"));
                }
            }
        }
    }

    private class CellAdapter extends RecyclerView.Adapter<CellHolder>{

        private boolean[][] mCells;

        public CellAdapter(boolean[][] cells){
            mCells = cells;
        }

        @Override
        public CellHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater LI = LayoutInflater.from(getActivity());
            View view = LI.inflate(R.layout.gol_grid_item,parent,false);
            return new CellHolder(view);
        }

        @Override
        public void onBindViewHolder(CellHolder holder, int position){


            //this math is maybe wrong
            int vPosition = (position / mCells[0].length);

            int hPosition = (position % mCells[0].length);

            Log.d("BIND VIEW HOLDER", Integer.toString(vPosition) + " " + Integer.toString(hPosition) );

            holder.bindCell(mCells[vPosition][hPosition],vPosition,hPosition);
        }

        @Override
        public int getItemCount(){
            //this math is right
            return mCells.length * mCells[0].length;
        }
    }
}
