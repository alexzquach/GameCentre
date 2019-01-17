package fall2018.csc2017.GameCentre.MineSweeper;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;


import fall2018.csc2017.GameCentre.SuperClasses.GestureDetectGridView;

/**
 * Extends GridView for handling a GridView for Minesweeper
 */
public class MineSweeperGestureDetectGridView extends GestureDetectGridView {

    /**
     * The movement controller
     */
    private MineSweeperMovementController mController;

    /**
     * A constructor using only context
     *
     * @param context The current activity context
     */
    public MineSweeperGestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    /**
     * A constructor using Context and AttributeSet
     *
     * @param context The current activity context
     * @param attrs The current attribute set
     */
    public MineSweeperGestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * A constructor using Context, AttributeSet and a style attribute
     *
     * @param context The current activity context
     * @param attrs The current attribute set
     * @param defStyleAttr The style for the attribute
     */
    public MineSweeperGestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initialization method
     *
     * @param context The current activity context
     */
    private void init(final Context context) {
        mController = new MineSweeperMovementController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = MineSweeperGestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    /**
     * Set the MineSweeperBoardManager
     *
     * @param mineSweeperBoardManager the MineSweeperBoardManager to set
     */
    public void setMineSweeperBoardManager(MineSweeperBoardManager mineSweeperBoardManager) {
        mController.setMineSweeperBoardManager(mineSweeperBoardManager);
    }
}
