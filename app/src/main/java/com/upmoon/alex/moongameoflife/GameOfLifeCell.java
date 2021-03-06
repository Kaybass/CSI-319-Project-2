package com.upmoon.alex.moongameoflife;

import java.io.Serializable;


/**
 * Created by Alex on 10/29/2016.
 */

/* TODO: Draw the cell */

public class GameOfLifeCell implements Serializable {
    /* EC: Have cells live for a predetermined number of generations */
    private final static int LIFESPAN = 10;

    private int age; // Track the age of the cell
    private int row, column; // The location of the cell in the grid
    private boolean status; // (TRUE: Alive) OR (FALSE: Dead)

    /* Cell Constructor */
    public GameOfLifeCell (int row, int column) {
        status = false;
        this.row = row;
        this.column = column;
    }

    /* Change the status of a cell */
    public void changeStatus() {
        status = !status;

        /* If the cell is alive, set its age to zero */
        if (status) {
            age = 0;
        }
    }

    /* Set the status of a cell */
    public void setStatus (boolean newStatus) {
        status = newStatus;
    }

    /* Returns the status of a cell */
    public boolean getStatus() {
        return status;
    }

    /* Age the cell by incrementing by one. If the cell age has reached its lifespan, kill it */
    public void age() {
        if (age >= LIFESPAN) {
            /* The cell is too old. Kill the cell */
            status = false;
        } else {
            /* Increment the cell's age */
            age++;
        }
    }

    /* Reset the age of a cell */
    public void resetAge() {
        age = 0;
    }

    public void setAge(int newAge) {
        age = newAge;
    }

    public int getAge() {
        return age;
    }

}
