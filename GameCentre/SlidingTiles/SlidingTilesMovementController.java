package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Context;
import android.widget.Toast;
import java.util.List;

/**
 * A movement controller for SlidingTiles
 */
class SlidingTilesMovementController {

    /**
     * The SlidingTiles
     */
    private SlidingTilesBoardManager slidingTilesBoardManager = null;

    /**
     * A default constructor
     */
    SlidingTilesMovementController() {
    }

    /**
     * Set the SlidingTilesBoardManager to the current manager
     *
     * @param slidingTilesBoardManager The current board manager
     */
    void setSlidingTilesBoardManager(SlidingTilesBoardManager slidingTilesBoardManager) {
        this.slidingTilesBoardManager = slidingTilesBoardManager;
    }

    /**
     * Process a TapMovement
     *
     * @param context The current activity context
     * @param position The position on the GestureDetectGridView represent by an int
     */
    void processTapMovement(Context context, int position, boolean display) {
        //Checks if the game is still going on or not
        if (slidingTilesBoardManager.isValidTap(position) && !slidingTilesBoardManager.gameOver()) {

            //Saves the previous move in the stateManager stack
            List<SlidingTilesTile> slidingTilesTiles = slidingTilesBoardManager.getSlidingTilesBoard().getSlidingTilesTiles();
            SlidingTilesStartingActivity.stateManager.save(slidingTilesTiles);

            slidingTilesBoardManager.touchMove(position);
            if (slidingTilesBoardManager.gameOver()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Checks if the game is disabled to make sure if the game is over or not
            if (slidingTilesBoardManager.gameOver()) {
                Toast.makeText(context, "The Game is over! Start a new game or view your score on the scoreboard!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


