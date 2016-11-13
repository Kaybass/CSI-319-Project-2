package com.upmoon.alex.moongameoflife;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameOfLifeFragment extends Fragment {

    private RecyclerView mGOLBoard;
    private CellAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_of_life, container, false);



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
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


    //
    private class CellHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

        private boolean mAlive = false;
        private int mHorPos, mVerPos;

        private Button mCell;

        public CellHolder(View itemView){
            super(itemView);

            //button sutff
        }

        public void bindCell(boolean alive, int v, int h){
            mAlive = alive;
            mHorPos = h;
            mVerPos = v;
        }

        @Override
        public void onClick(View v){
            CurrentBoard.getInstance().flipCell(mVerPos,mHorPos);
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
            int vPosition = (position / mCells[0].length) - 1;

            int hPosition = (position % mCells[0].length) - 1;

            holder.bindCell(mCells[vPosition][hPosition],vPosition,hPosition);
        }

        @Override
        public int getItemCount(){
            //this math is right
            return mCells.length * mCells[0].length;
        }
    }
}
