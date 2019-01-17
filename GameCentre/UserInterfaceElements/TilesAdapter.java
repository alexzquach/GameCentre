package fall2018.csc2017.GameCentre.UserInterfaceElements;

/*
Taken from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/CustomAdapter.java

This Class is an overwrite of the Base Adapter class
It is designed to aid setting the button sizes and positions in the GridView
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.SuperClasses.GridViewAdapter;

/**
 * A custom adapter used for games that require a special tile button, like slidingtiles
 * and mine sweeper, extends GridViewAdapter
 */
public class TilesAdapter extends GridViewAdapter {

    /**
     * Constructor
     *  @param buttons The list of buttons
     * @param columnWidth The width of each column
     * @param columnHeight The height of each column
     */
    public TilesAdapter(ArrayList<View> buttons, int columnWidth, int columnHeight) {
        super(buttons, columnWidth, columnHeight);
    }

    /**
     * Returns the desired GridView layout at the specific position.  Overrides super method
     *
     * @param position:    The given position.
     * @param convertView: Which type of view inputted.
     * @param parent:      The superclass of the view inputted.
     * @return TextView: The desired TextView at the position.
     */
    @Override
    public Button getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if (convertView == null) {
            button = (Button) mItems.get(position);
        } else {
            button = (Button) convertView;
        }

        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);

        return button;
    }
}
