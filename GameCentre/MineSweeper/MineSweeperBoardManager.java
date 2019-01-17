package fall2018.csc2017.GameCentre.MineSweeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class MineSweeperBoardManager implements Serializable {

    /**
     * The board being managed.
     */
    private MineSweeperBoard board;

    /**
     * The users clicks being managed, -1 represents the user not even clicking new game
     */
    private int clicks = -1;

    /**
     * The list of MineSweeper tiles
     */
    private List<MineSweeperTile> tiles = new ArrayList<>();

    /**
     * Manage a new shuffled board.
     * Background values:
     * 0-8 num bombs
     * -1 is a bomb
     */
    public MineSweeperBoardManager() {
        final int numTiles = MineSweeperBoard.getNumRows() * MineSweeperBoard.getNumCols();
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new MineSweeperTile(tileNum));
        }
        setBombTiles(tiles);
        // Using the bombs tiles from above, set all tiles to correct background's corresponding.
        checkBombs(tiles);
        this.board = new MineSweeperBoard(tiles);
    }

    /**
     * Set the number of clicks
     *
     * @param clicks The number of clicks to set
     */
    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    /**
     * Gets the number of clicks the user has done
     *
     * @return Gets an int representing the users clicks
     */
    public int getClicks() {
        return clicks;
    }

    /**
     * Return the current board
     *
     * @return Returns the MineSweeperBoard
     */
    public MineSweeperBoard getBoard() {
        return board;
    }

    /**
     * Returns the current list of tiles
     *
     * @return Return the List of MineSweeperTile
     */
    public List<MineSweeperTile> getTiles() {
        return tiles;
    }

    /**
     * Set NUM_BOMBS number of bombs at randomly generated locations.
     *
     * @param tiles list of all tiles
     */
    private void setBombTiles(List<MineSweeperTile> tiles) {
        Set<Integer> bombTilesId = new HashSet<>();
        Random random = new Random();
        // Generate random unique IDs(since its a set) until we have num bombs we want.
        while (bombTilesId.size() < MineSweeperBoard.getNumBombs()) {
            // Get a random tile from the list.
            int tempId = random.nextInt(tiles.size() - 1) + 1;
            bombTilesId.add(tempId);
        }
        // Convert all bomb id tiles to bombs.
        for (MineSweeperTile curTile : tiles) {
            if (bombTilesId.contains(curTile.getId())) {
                curTile.setBackgroundValue(-1);
            }
        }
    }

    /**
     * Loop through the given list of tiles and check adjacency of each tile and set the number
     * of bombs following such rule:
     * if any bombs then this is the number of adjacent bombs.
     *
     * @param tiles list of MineSweeperTile objects
     */
    private void checkBombs(List<MineSweeperTile> tiles) {
        for (MineSweeperTile tile : tiles) {
            int numOfBomb = 0;
            // Tile's not a bomb, check adjacency then.
            if (tile.getBackgroundValue() != -1) {
                // Go through all adjacent tiles, set curtile to num bombs in adjacent.
                List<MineSweeperTile> tileToCheck = MineSweeperAdjacencyCheck.getAdjacentTiles(tiles, tile.getId());
                for (MineSweeperTile t : tileToCheck) {
                    if (t.getBackgroundValue() == -1) {
                        numOfBomb += 1;
                    }
                }
                tile.setBackgroundValue(numOfBomb);
            }
        }
    }

    /**
     * Return whether all the non-bomb tiles are opened, or game is lost.
     *
     * @return Return whether the tiles are in row-major order
     */
    public boolean gameOver() {
        // Always check lose condition first.
        if (this.board.isLost()) {
            return true;
        }
        return isWin();
    }

    /**
     * Process a touch at position in the board, opening tiles as appropriate.
     *
     * @param position the position
     */
    public void touchMove(int position) {
        int row = position / MineSweeperBoard.getNumCols();
        int col = position % MineSweeperBoard.getNumCols();
        if (!gameOver() && board.getTile(row, col).isNotOpened()) {
            clicks += 1;
            this.board.open(row, col);
        }
    }

    /**
     * Check if the player has won the game.
     *
     * @return true if player won the game false otherwise
     */
    public boolean isWin() {
        // If any non-bomb tiles are not opened yet, still havent won...
        for (MineSweeperTile tile : board.getTiles()) {
            if (!(tile.getBackgroundValue() == -1) && tile.isNotOpened()) {
                return false;
            }
        }
        return true;
    }
}