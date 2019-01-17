package fall2018.csc2017.GameCentre.PowersPlus;

/*
Taken from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/CustomAdapter.java

This Class is an overwrite of the Base Adapter class
It is designed to aid setting the textView sizes and positions in the GridView
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.GameCentre.SuperClasses.GridViewAdapter;

/**
 * The adapter that allows the PowersPlus board to be displayed.  Extends GridViewAdapater
 */
public class PowersPlusAdapter extends GridViewAdapter {

    /**
     * Constructs the Adapter.
     *  @param textViews :    what TextViews to update in the GridView.
     * @param columnWidth :  each column's width.
     * @param columnHeight : each column's height.
     */
    PowersPlusAdapter(ArrayList<View> textViews, int columnWidth, int columnHeight) {
        super(textViews, columnWidth, columnHeight);
    }

    /**
     * Constructs the view for each cell of the grid view. Overrides the super method.
     *
     * @param position:    the position of the cell.
     * @param convertView: given view to convert to TextView.
     * @param parent:      the parent object of the View.
     * @return View: the desired view of the cell.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textview;

        if (convertView == null) {
            textview = (TextView) mItems.get(position);
        } else {
            textview = (TextView) convertView;
        }

        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        textview.setLayoutParams(params);

        return textview;
    }
}

