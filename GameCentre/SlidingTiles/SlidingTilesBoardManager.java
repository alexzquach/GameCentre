package fall2018.csc2017.GameCentre.SlidingTiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manage a slidingTilesBoard, including swapping tiles, checking for a win, and managing taps.
 */
public class SlidingTilesBoardManager implements Serializable {

    /**
     * The slidingTilesBoard being managed.
     */
    private SlidingTilesBoard slidingTilesBoard;

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    /**
     * The users clicks being managed, -1 represents the user not even clicking new game
     */
    private int clicks = -1;

    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Determines if we are gameStarted or not
     */
    private boolean gameStarted = true;

    /**
     * Manage a slidingTilesBoard that has been pre-populated.
     *
     * @param slidingTilesBoard the slidingTilesBoard
     */
    public SlidingTilesBoardManager(SlidingTilesBoard slidingTilesBoard) {
        this.slidingTilesBoard = slidingTilesBoard;
    }

    /**
     * Return the current slidingTilesBoard.
     */
    public SlidingTilesBoard getSlidingTilesBoard() {
        return slidingTilesBoard;
    }

    /**
     * Manage a new shuffled slidingTilesBoard.
     */
    public SlidingTilesBoardManager() {
        List<SlidingTilesTile> slidingTilesTiles = new ArrayList<>();
        final int numTiles = SlidingTilesBoard.getNumRows() * SlidingTilesBoard.getNumCols();
        for (int tileNum = numTiles - 1; tileNum != -1; tileNum--) {
            slidingTilesTiles.add(new SlidingTilesTile(tileNum));
        }

        this.slidingTilesBoard = new SlidingTilesBoard(slidingTilesTiles);
        //Shuffles the board again if the game ends up being completed after shuffle
        shuffleBoard(numTiles);
        gameStarted = false;
    }

    /**
     * Shuffles the board
     *
     * @param numTiles The number of tiles we need to shuffle
     */
    private void shuffleBoard(int numTiles) {
        //Shuffling
        int temp;
        for (int i = 0; i < 100000; i++){
            temp = (int) (Math.random() * numTiles);
            touchMove(temp);
        }
        if (gameOver()){
            shuffleBoard(numTiles);
        }
    }

    /**
     * Returns the number of clicks the user has done
     *
     * @return returns an int representing the users clicks
     */
    public int getClicks(){
        return clicks;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean gameOver() {
        //Uses the iterator for slidingTilesBoard to check if the tiles are in row-major order
        Iterator<SlidingTilesTile> iter = slidingTilesBoard.iterator();
        for (int i = 0; i < slidingTilesBoard.numTiles(); i++) {
            if (iter.next().getId() != i + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    public boolean isValidTap(int position) {
        int row = position / SlidingTilesBoard.getNumCols();
        int col = position % SlidingTilesBoard.getNumCols();
        int blankId = slidingTilesBoard.numTiles();
        // Are any of the 4 the blank tile?
        SlidingTilesTile above = row == 0 ? null : slidingTilesBoard.getTile(row - 1, col);
        SlidingTilesTile below = row == SlidingTilesBoard.getNumRows() - 1 ? null : slidingTilesBoard.getTile(row + 1, col);
        SlidingTilesTile left = col == 0 ? null : slidingTilesBoard.getTile(row, col - 1);
        SlidingTilesTile right = col == SlidingTilesBoard.getNumCols() - 1 ? null : slidingTilesBoard.getTile(row, col + 1);
        //Checks if the game is over as well
        return ((below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId));
    }

    /**
     * Process a touch at position in the slidingTilesBoard, swapping tiles as appropriate.
     *
     * @param position the position
     */
    public void touchMove(int position) {
        int row = position / SlidingTilesBoard.getNumRows();
        int col = position % SlidingTilesBoard.getNumCols();
        int blankId = slidingTilesBoard.numTiles();
        int rowAdder = 0;
        int colAdder = 0;
        if (isValidTap(position)) {

            //Checks to see if the game has started because we use touchMove when shuffling
            if (!gameStarted) {
                //Increments the user score
                clicks += 1;
            }

            //Swaps up
            if (row - 1 >= 0 && slidingTilesBoard.getTile(row - 1, col).getId() == blankId) {
                rowAdder = -1;
                colAdder = 0;
            }

            //Swaps down
            if (row + 1 <= SlidingTilesBoard.getNumCols() - 1 && slidingTilesBoard.getTile(row + 1, col).getId() == blankId) {
                rowAdder = 1;
                colAdder = 0;
            }

            //Swaps to the left
            if (col - 1 >= 0 && slidingTilesBoard.getTile(row, col - 1).getId() == blankId) {
                rowAdder = 0;
                colAdder = -1;
            }

            //Swaps to the right
            if (col + 1 <= SlidingTilesBoard.getNumCols() - 1 && slidingTilesBoard.getTile(row, col + 1).getId() == blankId) {
                rowAdder = 0;
                colAdder = 1;
            }
            slidingTilesBoard.swapTiles(row + rowAdder, col + colAdder, row, col);
        }
    }
}