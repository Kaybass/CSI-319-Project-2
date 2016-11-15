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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameOfLifeFragment extends Fragment {

    private RecyclerView mGOLBoard;
    private CellAdapter mAdapter;

    private Button mPauseButton, mResetButton, mCloneButton;

    private static int mTimer = 800;
    private static boolean mPaused = false;

    private Thread mThread;

    private volatile boolean mRunning = true;

    private static boolean mPulse = true;//highkey things that I lack

    private GameOfLifeBoard mResetCopy;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_of_life, container, false);

        mResetCopy = new GameOfLifeBoard(CurrentBoard.getInstance().getVerticalLength(),
                CurrentBoard.getInstance().getHorizontalLength());

        for(int i = 0; i < mResetCopy.getRows(); i++){
            for(int j = 0; j < mResetCopy.getColumns(); j++){
                if(CurrentBoard.getInstance().getCellStatus(i,j))
                    mResetCopy.flipCellStatus(i,j);
            }
        }

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
                flipPaused();
                mResetCopy = CurrentBoard.getInstance().getBoard();
            }
        });

        mResetButton = (Button) view.findViewById(R.id.gol_clone_);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCloneButton = (Button) view.findViewById(R.id.gol_reset_);
        mCloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPaused()){
                    CurrentBoard.getInstance().setBoard(null);
                    CurrentBoard.getInstance().setBoard(new GameOfLifeBoard(22,22));
                    for(int i = 0; i < mResetCopy.getRows(); i++){
                        for(int j = 0; j < mResetCopy.getColumns(); j++){
                            CurrentBoard.getInstance().setCell(i,j,mResetCopy.cellStatus(i,j));
                        }
                    }
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
     *  static thread functions
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

    public static boolean getPulse(){
        return mPulse;
    }

    public static void flipPulse(){
        mPulse = !mPulse;
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
                    if(getPulse())
                        mCell.setText("8");
                    else
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
