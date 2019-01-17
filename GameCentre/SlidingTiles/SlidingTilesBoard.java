package fall2018.csc2017.GameCentre.SlidingTiles;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding slidingTilesTiles board.
 */
public class SlidingTilesBoard extends Observable implements Iterable<SlidingTilesTile>, Serializable {

    /**
     * The number of rows and columns that the board has, based on difficulty
     */
    private static int numRows;
    private static int numCols;

    public static void setDifficulty(int diff){
        numRows = 4 + diff;
        numCols = 4 + diff;
    }

    /**
     * Get the number of rows for the board
     *
     * @return represents the number of rows
     */
    public static int getNumRows() {
        return numRows;
    }

    /**
     * Get the number of columns for the board
     *
     * @return represents the number of columns
     */
    public static int getNumCols() {
        return numCols;
    }

    /**
     * The slidingTilesTiles on the board in row-major order.
     */
    private SlidingTilesTile[][] slidingTilesTiles = new SlidingTilesTile[numRows][numCols];

    /**
     * A new board of slidingTilesTiles in row-major order.
     * Precondition: len(slidingTilesTiles) == numRows * numCols
     *
     * @param slidingTilesTiles the slidingTilesTiles for the board
     */
    public SlidingTilesBoard(List<SlidingTilesTile> slidingTilesTiles) {
        Iterator<SlidingTilesTile> iter = slidingTilesTiles.iterator();

        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.slidingTilesTiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Returns the current arrangement of the board
     *
     * @return returns a list representing the current board
     */
    public List<SlidingTilesTile> getSlidingTilesTiles() {
        List<SlidingTilesTile> currentArrangement = new ArrayList<>();

        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                currentArrangement.add(this.slidingTilesTiles[row][col]);
            }
        }
        return currentArrangement;
    }

    /**
     * Return the number of slidingTilesTiles on the board.
     *
     * @return the number of slidingTilesTiles on the board
     */
    public int numTiles() {
        return numCols * numRows;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public SlidingTilesTile getTile(int row, int col) {
        return slidingTilesTiles[row][col];
    }

    /**
     * Swap the slidingTilesTiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    public void swapTiles(int row1, int col1, int row2, int col2) {
        //Swaps slidingTilesTiles as specified
        SlidingTilesTile tempSlidingTilesTile = this.slidingTilesTiles[row1][col1];
        this.slidingTilesTiles[row1][col1] = this.slidingTilesTiles[row2][col2];
        this.slidingTilesTiles[row2][col2] = tempSlidingTilesTile;
        setChanged();
        notifyObservers();
    }

    /**
     * Un-does a move on the board
     *
     * @param slidingTilesTiles represents the previous state of the board
     */
    public void undo(List<SlidingTilesTile> slidingTilesTiles) {
        Iterator<SlidingTilesTile> iter = slidingTilesTiles.iterator();
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.slidingTilesTiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Compares two objects to see if they are the same
     *
     * @param o The object to compare to
     * @return Returns true if the two objects are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlidingTilesBoard that = (SlidingTilesBoard) o;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                if (this.slidingTilesTiles[row][col] != that.slidingTilesTiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Return a board iterator
     *
     * @return returns a new board iterator
     */
    @Override
    @NonNull
    public Iterator<SlidingTilesTile> iterator() {
        //Return a BoardIterator
        return new BoardIterator();
    }

    /**
     * Iterate over slidingTilesTiles in a board
     */
    public class BoardIterator implements Iterator<SlidingTilesTile> {
        //These two variables keep track of rows and columns
        int rowTracker = 0;
        int colTracker = 0;


        /**
         * Checks to see if the iterator has a next tile or not
         */
        @Override
        public boolean hasNext() {
            //If rowTracker exceeds the total number of rows, we know we no longer have a next
            //because we are doing it by row major order
            return rowTracker < numRows;
        }

        /**
         * Returns the next item
         *
         * @return returns the next tile
         */
        @Override
        public SlidingTilesTile next() {
            if (!hasNext()) {
                throw new NoSuchElementException(String.format
                        ("End of board [%s .. %s]", 1, numCols * numRows));
            }
            //Remember the result
            SlidingTilesTile result = slidingTilesTiles[rowTracker][colTracker];
            //Get ready for the next call to next
            if (colTracker == numCols - 1) {
                rowTracker += 1;
                colTracker = 0;
            } else {
                colTracker += 1;
            }
            //Return what we remembered
            return result;
        }
    }
}

