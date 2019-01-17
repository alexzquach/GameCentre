package fall2018.csc2017.GameCentre.MineSweeper;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;


/**
 * The sliding tiles board.
 * -1: already opened has a bomb
 * 0-8: already opened - number of bombs
 */
public class MineSweeperBoard extends Observable implements Iterable<MineSweeperTile>, Serializable {

    /**
     * The number of rows and columns
     */
    private static int numRows;
    private static int numCols;

    /**
     * The rate of bombs.
     */
    private static final double bombRatio = 0.1;

    /**
     * The boolean expression of game state, true if the player lost false otherwise.
     */
    private boolean isLost;

    /**
     * The number of bombs on the board, 15% of the board.
     */
    private static int numBombs;

    /**
     * The tiles on the board in row-major order.
     */
    private MineSweeperTile[][] tiles = new MineSweeperTile[numRows][numCols];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == numRows * numCols
     *
     * @param tiles the tiles for the board
     */
    public MineSweeperBoard(List<MineSweeperTile> tiles) {
        Iterator<MineSweeperTile> iter = tiles.iterator();
        isLost = false;
        numBombs = (int) (numRows * numCols * bombRatio);
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the number of rows
     *
     * @return Returns the number of rows
     */
    public static int getNumRows() {
        return numRows;
    }

    /**
     * Getter for the number of columns
     *
     * @return Returns the number of columns
     */
    public static int getNumCols() {
        return numCols;
    }

    /**
     * Getter for the number of bombs
     *
     * @return the number of bombs
     */
    public static int getNumBombs() {
        return numBombs;
    }

    /**
     * Sets the number of rows and columns depending on the number of difficulty
     *
     * @param diff The difficulty passed in
     */
    public static void setDifficulty(int diff) {
        numRows = 20 + (diff * 6);
        numCols = 12 + (diff * 4);
    }

    /**
     * Set the number of columns then updates the number of bombs.
     *
     * @param numCols the new number of columns
     */
    public static void setNumCols(int numCols) {
        MineSweeperBoard.numCols = numCols;
        numBombs = (int) (MineSweeperBoard.numCols * numRows * bombRatio);
    }

    /**
     * Set the number of rows then updates the number of bombs.
     *
     * @param numRows the new number of rows
     */
    public static void setNumRows(int numRows) {
        MineSweeperBoard.numRows = numRows;
        numBombs = (int) (numCols * MineSweeperBoard.numRows * bombRatio);
    }

    /**
     * Returns a list representation of tiles. In row major order.
     *
     * @return Returns a list of MineSweeperTiles.
     */
    public List<MineSweeperTile> getTiles() {
        List<MineSweeperTile> temp = new ArrayList<>();
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                temp.add(this.tiles[row][col]);
            }
        }
        return temp;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public MineSweeperTile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Opens the tile at row, col, and all tiles adjacent until it opens a numbered tile.
     *
     * @param row the row position of the tile of interest.
     * @param col the col position of the tile of interest.
     */
    public void open(int row, int col) {
        if (tiles[row][col].isNotOpened()) {
            // open the tile, always.
            tiles[row][col].setOpened();
            tiles[row][col].setBackground(tiles[row][col].getBackgroundValue());
            // Is itself a bomb tile, call game over.
            if (tiles[row][col].getBackgroundValue() == -1) {
                isLost = true;
            }
            // 0 bomb tile, call on adjacent ones, adjacent are either number or 0, since 0 bombs adjacent.
            if (tiles[row][col].getBackgroundValue() == 0) {
                // Recursively call on adjacent
                List<MineSweeperTile> adjacent =
                        MineSweeperAdjacencyCheck.getAdjacentTiles(tiles, row, col);
                for (MineSweeperTile curTile : adjacent) {
                    recursivePop(curTile, tiles);
                }
            }
        }
        setChanged();
        notifyObservers();
    }

    /**
     * This is a recursive helper which keep poping the adjancent tile until some number tile
     * has been popped
     *
     * @param curTile the current tile openning
     * @param tiles   the list of all tiles
     */
    private void recursivePop(MineSweeperTile curTile, MineSweeperTile[][] tiles) {
        curTile.setOpened();
        curTile.setBackground(curTile.getBackgroundValue());
        if (curTile.getBackgroundValue() == 0) {
            int row = MineSweeperAdjacencyCheck.getRowAndColFromId(curTile.getId()).get(0);
            int col = MineSweeperAdjacencyCheck.getRowAndColFromId(curTile.getId()).get(1);
            List<MineSweeperTile> rec_adjacent_tiles =
                    MineSweeperAdjacencyCheck.getAdjacentTiles(tiles, row, col);
            for (MineSweeperTile m : rec_adjacent_tiles) {
                if (m.isNotOpened()) {
                    recursivePop(m, tiles);
                }
            }
        }
    }

    /**
     * Return if the player lose the game.
     *
     * @return true if the player lose the game false otherwise.
     */
    public boolean isLost() {
        return isLost;
    }

    /**
     * Return a board iterator
     *
     * @return returns a new board iterator
     */
    @Override
    @NonNull
    public Iterator<MineSweeperTile> iterator() {
        //Return a BoardIterator
        return new MineSweeperBoardIterator();
    }

    /**
     * Iterate over tiles in a board
     */
    private class MineSweeperBoardIterator implements Iterator<MineSweeperTile> {
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
        public MineSweeperTile next() {
            if (!hasNext()) {
                throw new NoSuchElementException(String.format
                        ("End of board [%s .. %s]", 1, numCols * numRows));
            }
            //Remember the result
            MineSweeperTile result = tiles[rowTracker][colTracker];
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


