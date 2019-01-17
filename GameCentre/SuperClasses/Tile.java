package fall2018.csc2017.GameCentre.SuperClasses;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A tile that utilizes ID and background, made for games that only require tapping a tile
 */
public abstract class Tile implements Comparable<Tile>,Serializable {

    /**
     * The background id to find the tile image.
     */
    protected int background;

    /**
     * The unique id.
     */
    protected int id;

    /**
     * The list contains information about background
     */
    protected List<Integer> tileList = new ArrayList<>();

    /**
     * A constructor that takes 2 parameters, id and background
     *
     * @param id The id of the tile
     * @param background The background of the tile
     */
    protected Tile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * A constructor that takes only one parameter, the backgroundID and sets ID to
     * backgroundID + 1
     *
     * @param backgroundId The backgroundId of the tile
     */
    protected Tile(int backgroundId) {
        id = backgroundId + 1;
    }

    /**
     * Get the background of the tile
     *
     * @return Returns an int representing the background
     */
    public abstract int getBackground();

    /**
     * Get the ID of the tile
     *
     * @return Returns an in representing the ID
     */
    public abstract int getId();

    /**
     * Add the appropriate tile images to a list of tile images
     *
     * @param tileList The list of tile images
     */
    protected abstract void addTileImages(List<Integer> tileList);

    /**
     * Compares the ID of two MineSweeperTiles
     *
     * @param o The other MineSweeperTile object
     * @return Returns the difference of the two ids
     */
    @Override
    public int compareTo(@NonNull Tile o) {
        return o.id - this.id;
    }
}
