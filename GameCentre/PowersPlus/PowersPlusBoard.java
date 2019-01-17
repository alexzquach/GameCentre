package fall2018.csc2017.GameCentre.PowersPlus;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/*
This code is an adaptation and elements (such as algorithm and
implementation) which are inspired from the code at:
https://rosettacode.org/wiki/2048#Java

I do not consider this my complete own creation, rather my adaptation
of the original. All rights to the original coder and RosettaCode.
 */

/**
 * The PowersPlus main board.
 */
public class PowersPlusBoard extends Observable implements Iterable<PowersPlusTile>, Serializable {

    /**
     * The number of rows and columns for the board.
     */
    static int numRows = 4;
    static int numCols = 4;

    /**
     * The PowersPlus tiles on the board in row-major order.
     */
    private PowersPlusTile[][] powersPlusTiles = new PowersPlusTile[numRows][numCols];

    /**
     * Constructor of new PowerPlusBoard in row-major order.
     * Precondition: len(powerPlusTiles) == numRows * numCols.
     *
     * @param powersPlusTiles: the PowerPlusTiles for the board.
     */
    public PowersPlusBoard(List<PowersPlusTile> powersPlusTiles) {
        Iterator<PowersPlusTile> it = powersPlusTiles.iterator();
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.powersPlusTiles[row][col] = it.next();
            }
        }
    }

    /**
     * Get the number of rows of the board.
     *
     * @return int: returns the number of rows.
     */
    public static int getNumRows() {
        return numRows;
    }

    /**
     * Get the number of columns of the board.
     *
     * @return int: returns the number of columns.
     */
    public static int getNumCols() {
        return numCols;
    }

    /**
     * Returns the list representation of the PowersPlusBoard.
     *
     * @return List<PowersPlusTile>: returns a list representing the current board.
     */
    public List<PowersPlusTile> getPowersPlusTiles() {
        List<PowersPlusTile> powersPlusTilesCopy = new ArrayList<>();
        PowersPlusTile tempTile;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                PowersPlusTile thisTile = this.powersPlusTiles[row][col];
                tempTile = (thisTile == null) ? null : new PowersPlusTile(thisTile.getValue(),
                        thisTile.getPower());
                powersPlusTilesCopy.add(tempTile);
            }
        }
        return powersPlusTilesCopy;
    }

    /**
     * Return the number of powersPlusTiles on the board.
     *
     * @return int: the number of powersPlusTiles.
     */
    public int getNumTiles() {
        return numCols * numRows;
    }

    /**
     * Returns the tile at (row, col)
     *
     * @param row: the tile row
     * @param col: the tile column
     * @return PowersPlusTile the tile at (row, col)
     */
    public PowersPlusTile getTile(int row, int col) {
        return powersPlusTiles[row][col];
    }

    /**
     * Sets a tile at a row and col.
     *
     * @param newTile: The new tile
     */
    public void setTile(int newRow, int newCol, PowersPlusTile newTile) {
        powersPlusTiles[newRow][newCol] = newTile;
    }

    /**
     * Shifts the powersPlusTiles at (row1, col1) and (row2, col2)
     * one way swapping.
     *
     * @param row1: the first tile row
     * @param col1: the first tile col
     * @param row2: the second tile row
     * @param col2: the second tile col
     */
    public void shiftTiles(int row1, int col1, int row2, int col2) {
        this.powersPlusTiles[row2][col2] = this.powersPlusTiles[row1][col1];
        this.powersPlusTiles[row1][col1] = null;
    }

    /**
     * Un-does a move on the board.
     *
     * @param powersPlusTiles: represents the previous state of the board
     */
    public void undo(List<PowersPlusTile> powersPlusTiles) {
        Iterator<PowersPlusTile> it = powersPlusTiles.iterator();
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.powersPlusTiles[row][col] = it.next();
            }
        }
    }

    /**
     * Returns the new Iterator.
     *
     * @return returns a new board iterator
     */
    @Override
    @NonNull
    public Iterator<PowersPlusTile> iterator() {
        return new PowersPlusBoardIterator();
    }

    /**
     * Implementation of the board iterator.
     */
    public class PowersPlusBoardIterator implements Iterator<PowersPlusTile> {

        /**
         * Tracks the row and columns.
         */
        int rowTracker = 0;
        int colTracker = 0;

        /**
         * Determines if the iterator has a next Tile.
         */
        @Override
        public boolean hasNext() {
            return rowTracker < numRows;
        }

        /**
         * Returns the next item from iterator.
         *
         * @return PowersPlusTile: the next tile.
         */
        @Override
        public PowersPlusTile next() {
            if (!hasNext()) {
                throw new NoSuchElementException(String.format
                        ("End of board [%s .. %s]", 1, numCols * numRows));
            }
            PowersPlusTile result = powersPlusTiles[rowTracker][colTracker]; // Remember result
            // Get ready for the next call to next
            if (colTracker == numCols - 1) {
                rowTracker += 1;
                colTracker = 0;
            } else {
                colTracker += 1;
            }
            return result; // Return what we remembered
        }
    }

    /**
     * Changes and notifies any class implementing observable.
     */
    void notifyActivity() {
        setChanged();
        notifyObservers();
    }

    /**
     * Checks for equality between two board objects.
     *
     * @param o: The other object (not knowing its a board) to compare.
     * @return boolean: Whether the two objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowersPlusBoard that = (PowersPlusBoard) o;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                if (!this.powersPlusTiles[row][col].equals(that.powersPlusTiles[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }
}


