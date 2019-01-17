package fall2018.csc2017.GameCentre.SlidingTiles;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.SuperClasses.Tile;

/**
 * A SlidingTilesTile in a sliding tiles puzzle, extends tile.
 */
public class SlidingTilesTile extends Tile{

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
     * A SlidingTilesTile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public SlidingTilesTile(int id, int background) {
        super(id, background);
    }

    /**
     * A tile with a background id; look up and set the id.
     *
     * @param backgroundId the background ID of a single tile
     */
    public SlidingTilesTile(int backgroundId) {

        //Call the super constructor
        super(backgroundId);
        //Add all the tiles into a list then go through that list to assign the correct tile
        List<Integer> tileList = new ArrayList<>();
        addTileImages(tileList);

        if (id == SlidingTilesBoard.getNumRows() * SlidingTilesBoard.getNumCols()) {
            background = tileList.get(24);
        }else{
            for (int i = 0; i < SlidingTilesBoard.getNumRows() * SlidingTilesBoard.getNumCols() - 1; i++){
                if (id == i + 1){
                    background = tileList.get(i);
                }
            }
        }
    }

    /**
     * Adds all the tile images to a list to be used to assign tiles
     * Note: It is hardcoded but there is nothing else we can do
     *
     * @param tileList the list of tile images
     */
    @Override
    protected void addTileImages(List<Integer> tileList){
        tileList.add(R.drawable.tile_1);
        tileList.add(R.drawable.tile_2);
        tileList.add(R.drawable.tile_3);
        tileList.add(R.drawable.tile_4);
        tileList.add(R.drawable.tile_5);
        tileList.add(R.drawable.tile_6);
        tileList.add(R.drawable.tile_7);
        tileList.add(R.drawable.tile_8);
        tileList.add(R.drawable.tile_9);
        tileList.add(R.drawable.tile_10);
        tileList.add(R.drawable.tile_11);
        tileList.add(R.drawable.tile_12);
        tileList.add(R.drawable.tile_13);
        tileList.add(R.drawable.tile_14);
        tileList.add(R.drawable.tile_15);
        tileList.add(R.drawable.tile_16);
        tileList.add(R.drawable.tile_17);
        tileList.add(R.drawable.tile_18);
        tileList.add(R.drawable.tile_19);
        tileList.add(R.drawable.tile_20);
        tileList.add(R.drawable.tile_21);
        tileList.add(R.drawable.tile_22);
        tileList.add(R.drawable.tile_23);
        tileList.add(R.drawable.tile_24);
        tileList.add(R.drawable.tile_blank);
    }

    /**
     * Compares if two objects are the same
     *
     * @param o The object to compare to
     * @return Returns True if the object is the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlidingTilesTile that = (SlidingTilesTile) o;
        return id == that.id;
    }
}
