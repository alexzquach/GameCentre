package fall2018.csc2017.GameCentre.MineSweeper;

import java.util.List;
import java.util.Objects;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.SuperClasses.Tile;

/**
 * A Tile in a sliding tiles puzzle, extends tile.
 */
public class MineSweeperTile extends Tile {

    /**
     * Get the value of if the tile is opened or not.
     *
     * @return boolean representing isNotOpened tile.
     */
    public boolean isNotOpened() {
        return !this.isOpened;
    }

    /**
     * Set the tile to be opened.
     *
     */
    void setOpened() {
        this.isOpened = true;
    }

    /**
     * Determines if the tile is opened or not
     */
    private boolean isOpened;

    /**
     * Set the background value as given value, following such rule:
     * -1: has a bomb on it
     * 0-8: number of bombs adjacent to this tile
     *
     * @param backgroundValue the value given to be set
     */
    public void setBackgroundValue(int backgroundValue) {
        this.backgroundValue = backgroundValue;
    }

    /**
     * Set the image according to its background value
     *
     * @param backgroundValue the value representing the condition of this tile.
     */
    public void setBackground(int backgroundValue) {
        background = tileList.get(backgroundValue + 1);
    }

    /**
     * The background value.
     */
    private int backgroundValue;

    /**
     * Get the background value of this tile.
     *
     * @return the background value of this tile
     */
    public int getBackgroundValue() {
        return backgroundValue;
    }

    /**
     * Return the background id.
     *
     * @return the background id
     */
    @Override
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId the id of this tile
     */
    public MineSweeperTile(int backgroundId) {

        //Call the super constructor
        super(backgroundId);

        //Experimenting with putting drawables in a list
        addTileImages(tileList);
        //Set the background to a closed minesweeper tile
        background = tileList.get(10);

        //Initialize the tile to a closed tile
        this.backgroundValue = 0;
        this.isOpened = false;
    }

    /**
     * Adds all the tile images to a list to be used to assign tiles
     * Note: It is hardcoded but there is nothing else we can do
     *
     * @param tileList the list of tile images
     */
    @Override
    protected void addTileImages(List<Integer> tileList) {
        tileList.add(R.drawable.minesweeper_mine);
        tileList.add(R.drawable.minesweeper_0);
        tileList.add(R.drawable.minesweeper_1);
        tileList.add(R.drawable.minesweeper_2);
        tileList.add(R.drawable.minesweeper_3);
        tileList.add(R.drawable.minesweeper_4);
        tileList.add(R.drawable.minesweeper_5);
        tileList.add(R.drawable.minesweeper_6);
        tileList.add(R.drawable.minesweeper_7);
        tileList.add(R.drawable.minesweeper_8);
        tileList.add(R.drawable.minesweeper_unopened);
    }

    /**
     * Compares 2 objects for equality
     *
     * @param o The other object to compare to
     * @return Returns true if the two objects being compared are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MineSweeperTile)) return false;
        MineSweeperTile that = (MineSweeperTile) o;
        return getBackground() == that.getBackground() &&
                getId() == that.getId() &&
                isOpened == that.isOpened &&
                getBackgroundValue() == that.getBackgroundValue() &&
                Objects.equals(tileList, that.tileList);
    }

}
