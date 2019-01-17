package fall2018.csc2017.GameCentre.PowersPlus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
This code is an adaptation and elements (such as algorithm and
implementation) which are inspired from the code at:
https://rosettacode.org/wiki/2048#Java

I do not consider this my complete own creation, rather my adaptation
of the original. All rights to the original coder and RosettaCode.
 */

/**
 * The board manager for the PowersPlus game.
 */
public class PowersPlusBoardManager implements Serializable {

    /**
     * Generates a random number to be used as a seed for all others.
     */
    private Random randomSeed = new Random();

    /**
     * The powersDifficulty of each PowersPlus game. -1: power^9, 0: power^10, 1: power^11.
     */
    private static int powersDifficulty;

    /**
     * The number to multiply each value per merge.
     */
    private int base = randomSeed.nextInt(3) + 2;

    /**
     * The power to multiply the base with.
     */
    private int power = 10 + powersDifficulty;

    /**
     * The number that determines the victory of the game.
     */
    private int target = (int) Math.pow(base, power);

    /**
     * The highest value the user has gotten in the game.
     */
    private int highestValue = 0;

    /**
     * Keeps track of the previous score increments done in the game.
     */
    private List<Integer> pastScores;

    /**
     * The current score of the user.
     */
    private int currentScore = 0;

    /**
     * Whether the game is over.
     */
    private boolean gameOver = false;

    /**
     * Whether the game was won.
     */
    private boolean isWin = false;

    /**
     * The board to be manipulated.
     */
    private PowersPlusBoard board;

    /**
     * Boolean to check if we're currently searching moves.
     */
    private boolean checkingAvailableMoves;

    /**
     * Returns the base the current game will use. Randomly generated every new game.
     *
     * @return int: the current game's base.
     */
    public int getBase() {
        return base;
    }

    /**
     * Returns the power for the target.
     *
     * @return int: the power of the target.
     */
    public int getPower() {
        return power;
    }

    /**
     * Retrieves the current score of the game for the user.
     *
     * @return int: the current score.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Assigns a new value to highestValue.
     *
     * @param highestValue: the new highestValue.
     */
    public void setHighestValue(int highestValue) {
        this.highestValue = highestValue;
    }

    /**
     * Returns the current highest power of the user.
     *
     * @return int: the highest power.
     */
    public int getHighestPower() {
        return getExponent(highestValue);
    }

    /**
     * Returns if the game is over.
     *
     * @return boolean: if the game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the powersDifficulty for the game.
     *
     * @param diff: The powersDifficulty.
     */
    public static void setDifficulty(int diff) {
        powersDifficulty = diff;
    }

    /**
     * Returns the powersDifficulty for the game.
     *
     * @return int: The difficulty of the game.
     */
    public static int getPowersDifficulty() {
        return powersDifficulty;
    }

    /**
     * Returns the current board.
     *
     * @return PowersPlusBoard: the current board.
     */
    public PowersPlusBoard getBoard() {
        return board;
    }

    /**
     * Construct a new board.
     */
    public PowersPlusBoardManager() {
        List<PowersPlusTile> tiles = new ArrayList<>();
        final int numTiles = PowersPlusBoard.numCols * PowersPlusBoard.numRows;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(null);
        }
        this.board = new PowersPlusBoard(tiles);
        this.pastScores = new ArrayList<>();
        generateRandomTile();
        generateRandomTile();
    }

    /**
     * Manage a powersPlusBoard that has been pre-populated.
     *
     * @param powersPlusBoard: The pre-populated board to use.
     * @param base:            The given base for the numbers.
     * @param power:           The power to go up to.
     * @param target:          The largest value to go up to.
     * @param currentScore:    The current score the player should have.
     * @param pastScores:      The specified past scores the player has.
     */
    public PowersPlusBoardManager(PowersPlusBoard powersPlusBoard,
                                  int base,
                                  int power,
                                  int target,
                                  int currentScore,
                                  ArrayList<Integer> pastScores) {
        this.base = base;
        this.power = power;
        this.target = target;
        this.board = powersPlusBoard;
        this.pastScores = pastScores;
        this.currentScore = currentScore;
    }

    /**
     * Decreases the score made by move and decreases the highest value if necessary.
     */
    public void decreaseValues() {
        if (pastScores.size() != 0) {
            this.currentScore -= this.pastScores.remove(pastScores.size() - 1);
        }
        boolean found = false;
        for (PowersPlusTile tile : board.getPowersPlusTiles()) {
            if (tile != null && tile.getValue() == highestValue) {
                found = true;
            }
        }
        if (!found) {
            highestValue = highestValue / base;
        }
    }

    /**
     * Generates a new random tile in the board.
     */
    private void generateRandomTile() {
        int numTiles = board.getNumTiles();
        int pos = randomSeed.nextInt(numTiles); // Get a random tile position
        int row, col;
        // Cycle through every tile until an empty one is found.
        do {
            pos++;
            pos %= numTiles;
            row = pos / PowersPlusBoard.getNumCols();
            col = pos % PowersPlusBoard.getNumCols();
        } while (board.getTile(row, col) != null);
        // 10% chance to get an extra power
        int value = randomSeed.nextInt(10) == 0 ? base * base : base;
        if (value > highestValue) {
            highestValue = value;
        }
        board.setTile(row, col, new PowersPlusTile(value, base));
    }

    /**
     * Shifts the tiles upwards.
     *
     * @return boolean: whether the tiles have moved.
     */
    public boolean doMoveUp() {
        return doMove(0, -1, 0);
    }

    /**
     * Shifts the tiles upwards.
     *
     * @return boolean: whether the tiles have moved.
     */
    public boolean doMoveLeft() {
        return doMove(0, 0, -1);
    }

    /**
     * Shifts the tiles upwards.
     *
     * @return boolean: whether the tiles have moved.
     */
    public boolean doMoveDown() {
        return doMove(board.getNumTiles() - 1, 1, 0);
    }

    /**
     * Shifts the tiles upwards.
     *
     * @return boolean: whether the tiles have moved.
     */
    public boolean doMoveRight() {
        return doMove(board.getNumTiles() - 1, 0, 1);
    }

    /**
     * Clears all the tiles back to be able to merge for the next move.
     */
    private void clearTileMerged() {
        PowersPlusTile current;
        for (int row = 0; row < PowersPlusBoard.getNumCols(); row++) {
            for (int col = 0; col < PowersPlusBoard.getNumRows(); col++) {
                current = board.getTile(row, col);
                if (current != null) {
                    current.setIsMerged(false);
                    board.setTile(row, col, current);
                }
            }
        }
    }

    /**
     * Return if there are any moves that can be made.
     *
     * @return boolean: if there are any moves.
     */
    private boolean hasMoves() {
        // Goes through all moves to see if there is any to make.
        checkingAvailableMoves = true;
        boolean hasMoves = doMoveUp() || doMoveDown() || doMoveLeft() || doMoveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }

    /**
     * Returns if the game has been won.
     *
     * @return boolean: if the game has been won.
     */
    public boolean isWin() {
        return isWin;
    }

    /**
     * Carries out a move with a given direction and how to check for merging.
     *
     * @param countDown: Which direction to iterate through the board.
     * @param rowInc:    The next row we should look at.
     * @param colInc:    The next column we should look at.
     * @return boolean: Whether a move has been made or not.
     */
    private boolean doMove(int countDown, int rowInc, int colInc) {

        boolean moved = false;
        int score = 0;

        for (int i = 0; i < board.getNumTiles(); i++) {
            int pos = Math.abs(countDown - i); // Countdown: 15: Down/right, 0: Up/left
            int row = pos / PowersPlusBoard.getNumCols(), col = pos % PowersPlusBoard.getNumCols();

            if (board.getTile(row, col) == null) // If no tile to swap, move on to next tile.
                continue;
            int nextRow = row + rowInc,  nextCol = col + colInc;

            while (nextRow >= 0 && nextRow < PowersPlusBoard.getNumRows() && nextCol >= 0 && nextCol < PowersPlusBoard.getNumCols()) {
                PowersPlusTile nextTile = board.getTile(nextRow, nextCol);
                PowersPlusTile currentTile = board.getTile(row, col);

                if (nextTile == null) {
                    if (checkingAvailableMoves)
                        return true; // Stop here as a move can be made (if only checking).
                    board.shiftTiles(row, col, nextRow, nextCol);

                    row = nextRow;
                    col = nextCol;
                    nextRow += rowInc;
                    nextCol += colInc;
                    moved = true;
                } else if (nextTile.canMerge(currentTile)) {
                    if (checkingAvailableMoves)
                        return true; // Stop here as a move can be made (if only checking).
                    int value = nextTile.merge(currentTile); // A tile is "consumed" by nextTile
                    nextTile.setIsMerged(true);

                    score += updateScore(value); // Increments score regardless of base.
                    board.setTile(row, col, null);
                    moved = true;
                    break;
                } else
                    break;
            }
        }
        updateValues(moved, score); // Ends the game on no more moves, or target is met.
        board.notifyActivity();
        return moved;
    }

    /**
     * Updates the players score and determines if the game is over.
     *
     * @param moved: if a valid move has been made.
     * @param score: the score the user has gotten for the move.
     */
    private void updateValues(boolean moved, int score) {
        if (moved) {
            if (highestValue < target) {
                clearTileMerged();
                generateRandomTile();
                if (!hasMoves()) {
                    gameOver = true;
                    isWin = false;
                }
            } else if (highestValue == target) {
                gameOver = true;
                isWin = true;
            }
            currentScore += score;
            pastScores.add(score);
        }
    }

    /**
     * Updates the score as a fixed value no matter the randomized base.
     * Side effect of keeping track of the highest value.
     *
     * @param value: the value based on the move made.
     * @return int: the score to return.
     */
    private int updateScore(int value) {
        if (value > highestValue) {
            highestValue = value;
        }
        return (int) Math.pow(2, getExponent(value));
    }

    /**
     * Gets the associated power of a given value. This method is made
     * due to the lack of precision of floating point values.
     *
     * @param number: the number to get the exponent from.
     * @return int: the exponent of the number.
     */
    private int getExponent(int number) {
        int exponent = 1;
        while (Math.pow(base, exponent) < number) {
            exponent++;
        }
        return exponent;
    }
}
