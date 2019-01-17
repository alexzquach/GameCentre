package fall2018.csc2017.GameCentre.SuperClasses;

/*
Taken from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/CustomAdapter.java

This Class is an overwrite of the Base Adapter class
It is designed to aid setting the button sizes and positions in the GridView
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * A GridViewAdapter that tile games and the Leaderboard using a GridView will use to display the
 * individual cells
 */
public class GridViewAdapter extends BaseAdapter {

    /**
     * The list of items
     */
    protected ArrayList<View> mItems;

    /**
     * The heights and widths of columns
     */
    protected int mColumnWidth;
    protected int mColumnHeight;

    /**
     * Constructor for initializing the adapter.
     * @param items :        Each element for each cell of the Grid View.
     * @param columnWidth :  The cell width for each column.
     * @param columnHeight : The cell height for each row.
     */
    public GridViewAdapter(ArrayList<View> items, int columnWidth, int columnHeight) {
        mItems = items;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    /**
     * Get the total number of buttons
     *
     * @return Returns the number of tiles
     */
    @Override
    public int getCount() {
        return mItems.size();
    }

    /**
     * Get the item at a specific position
     *
     * @param position The position of the user click
     * @return Return the item at position
     */
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Gets the items ID number given a specified position.
     *
     * @param position: the position to get the item id.
     * @return long: the desired item ID.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the desired GridView layout at the specific position.
     *
     * @param position:    The given position.
     * @param convertView: Which type of view inputted.
     * @param parent:      The superclass of the view inputted.
     * @return TextView: The desired TextView at the position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;

        if (convertView == null) {
            item = mItems.get(position);
        } else {
            item = convertView;
        }

        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        item.setLayoutParams(params);

        return item;
    }
}
