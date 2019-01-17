package fall2018.csc2017.GameCentre.MineSweeper;

import android.content.Context;
import android.widget.Toast;

/**
 * A movement controller for Minesweeper
 */
class MineSweeperMovementController {

    /**
     * The MineSweeperBoardManager
     */
    private MineSweeperBoardManager mineSweeperBoardManager = null;

    /**
     * A default constructor
     */
    MineSweeperMovementController() {
    }

    /**
     * Set the MineSweeperBoardManager to the current manager
     *
     * @param MineSweeperBoardManager The current board manager
     */
    void setMineSweeperBoardManager(MineSweeperBoardManager MineSweeperBoardManager) {
        this.mineSweeperBoardManager = MineSweeperBoardManager;
    }

    /**
     * Process a TapMovement
     *
     * @param context The current activity context
     * @param position The position on the GestureDetectGridView represent by an int
     */
    void processTapMovement(Context context, int position) {
        //Checks if the game is still going on or not
        if (!mineSweeperBoardManager.gameOver()) {

            mineSweeperBoardManager.touchMove(position);
            if (mineSweeperBoardManager.gameOver() && mineSweeperBoardManager.getBoard().isLost()) {

                Toast.makeText(context, "You lose! Sorry!", Toast.LENGTH_SHORT).show();
            }else if(mineSweeperBoardManager.gameOver() && mineSweeperBoardManager.isWin()){
                Toast.makeText(context, "You win!", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Checks if the game is disabled to make sure if the game is over or not
            if (mineSweeperBoardManager.gameOver()) {
                Toast.makeText(context, "The Game is over! Start a new game or view your score on the scoreboard!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
            }

        }
    }
}