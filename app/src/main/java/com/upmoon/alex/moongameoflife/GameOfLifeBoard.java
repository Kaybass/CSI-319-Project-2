package com.upmoon.alex.moongameoflife;

import java.io.Serializable;

/**
 * Created by Alex on 10/29/2016.
 */


public class GameOfLifeBoard implements Serializable {
    private static GameOfLifeBoard ourInstance = new GameOfLifeBoard();

    /* Hold the variable dimensions of the board. */
    private int rows, columns;
    private int generations; // Current generation of this game

    /* Create 2D array to hold the cells */
    private GameOfLifeCell[][] cells;

    public GameOfLifeBoard() {

    }

    public GameOfLifeBoard(int rows, int columns) {
        /* Initialize the array of cells */
        cells = new GameOfLifeCell[rows][columns];

        /* Create a cell for each element in the 2D array */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new GameOfLifeCell(i,j);
            }
        }

        /* Set the instance variables */
        this.rows = rows;
        this.columns = columns;

        /* We start at the 0 gen */
        this.generations = 0;
    }

    /* Creates an array of cells given an old array of cells. */
    public GameOfLifeBoard(int rows, int columns, GameOfLifeCell[][] oldArray, int g) {
        /* Set the instance variables */
        this.rows = rows;
        this.columns = columns;

        cells = new GameOfLifeCell[rows][columns];

        /* Copy each cell from the oldArray. */
        for(int i = 0; i < Math.min(rows, oldArray.length); i++){
            for(int j = 0; j < Math.min(columns, oldArray[0].length); j++){
                cells[i][j] = oldArray[i][j];
            }
        }

        /* Make sure all the null new Cells are
         *  They would not be yet if the new number of rows
         *  or columns exceeds the old */
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(cells[i][j] == null)
                    cells[i][j] = new GameOfLifeCell(i, j);
            }
        }
        
        /* Set generation to the same */
        this.generations = g;
    }

    /* Returns the 2D GameOfLifeCell array */
    public GameOfLifeCell[][] extract(){
        return cells;
    }

    /* Method tpo update the entire board to the next gen */
    public void updateBoard() {
        /* Create a 2D byte array to keep track of the number of cells living */
        byte[][] livingNeighborsCount = new byte[rows][columns];

        /* Count the number of neighbors a cell has and write to the 2D byte array */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                /* Variables to save positions left and right of row and column */
                int leftOfRow = i + rows - 1;
                int rightOfRow = i + 1;
                int leftOfColumn = j + columns - 1;
                int rightOfColumn = j + 1;

                /* Check to see if the cells are alive or dead. If alive, livingNeighborsCount gets incremented */
                if (cells[i][j].getStatus()) {
                    livingNeighborsCount[leftOfRow % rows][leftOfColumn % columns]++;
                    livingNeighborsCount[leftOfRow % rows][j % columns]++;
                    livingNeighborsCount[(i + rows - 1) % rows][rightOfColumn % columns]++;
                    livingNeighborsCount[i % rows][leftOfColumn % columns]++;
                    livingNeighborsCount[i % rows][rightOfColumn % columns]++;
                    livingNeighborsCount[rightOfRow % rows][leftOfColumn % columns]++;
                    livingNeighborsCount[rightOfRow % rows][j % columns]++;
                    livingNeighborsCount[rightOfRow % rows][rightOfColumn % columns]++;
                }
            }
        }

        /* Change the status of the cell based on the number of living neighbors */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                /* If cell >= 4 living neighbors, it dies via overcrowding */
                if (livingNeighborsCount[i][j] >= 4) {
                    cells[i][j].setStatus(false);
                }

                /* If a cell has 0 or 1 living neighbors, it dies via exposure */
                if (livingNeighborsCount[i][j] < 2) {
                    cells[i][j].setStatus(false);
                }

                /* A cell is born if it has 3 living neighbors */
                if (livingNeighborsCount[i][j] == 3) {
                    cells[i][j].setStatus(true);
                    cells[i][j].resetAge();
                }
            }
        }

        /* Age all the cells. */
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                cells[i][j].age();
            }
        }

        /* Increment the generation */
        generations++;
    }

    public void flipCellStatus(int row, int col){
        cells[row][col].changeStatus();
    }

    /* Set generations */
    private void setGeneration(int g) {
        generations = g;
    }

    /* Get generations */
    private int getGeneration() {
        return generations;
    }

    /* Reset all cells to dead and generation to 0 */
    public void reset(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                cells[i][j].setStatus(false);
            }
        }
        generations = 0;
    }

}
