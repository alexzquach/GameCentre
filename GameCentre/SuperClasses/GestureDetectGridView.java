package fall2018.csc2017.GameCentre.SuperClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Handles a GridView for games that require a Gesture to make a move
 */
public class GestureDetectGridView extends GridView {

    /**
     * The minimum swiping distance
     */
    public static final int SWIPE_MIN_DISTANCE = 100;

    /**
     * The gesture detector
     */
    protected GestureDetector gDetector;

    /**
     * Check to see if there is a fling
     */
    private boolean mFlingConfirmed = false;

    /**
     * Where they touched on the X axis
     */
    private float mTouchX;
    /**
     * Where they touched on the Y axis
     */
    private float mTouchY;

    /**
     * A constructor using Context, AttributeSet and a style attribute
     *
     * @param context      The current activity context
     * @param attrs        The current attribute set
     * @param defStyleAttr The style for the attribute
     */
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * A constructor using Context and AttributeSet
     *
     * @param context The current activity context
     * @param attrs   The current attribute set
     */
    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * A constructor using only context
     *
     * @param context The current activity context
     */
    public GestureDetectGridView(Context context) {
        super(context);
    }

    /**
     * Intercepts the touch event
     *
     * @param ev The motion event
     * @return Boolean representing if there was a touch
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * What happens when there is a touch event
     *
     * @param ev The motion event
     * @return Returns true if there was a touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }
}
