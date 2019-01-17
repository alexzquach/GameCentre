package fall2018.csc2017.GameCentre.MineSweeper;

import java.util.ArrayList;
import java.util.List;

/**
 * The class contains several functions of getting the adjacent tiles different ways by id of the
 * tile or columns and row number of the tile.
 */
class MineSweeperAdjacencyCheck {

    /**
     * Get the adjacent tiles of a tile using its id, call getAdjacentTiles below
     * after you have converted id into the appropriate row and column
     *
     * @param tiles the list of all tiles
     * @param id    the id of the tile which all returned tiles adjacent to.
     * @return a list of all adjacent tiles (including four corners)
     */
    static List<MineSweeperTile> getAdjacentTiles(List<MineSweeperTile> tiles, int id) {
        List<MineSweeperTile> tileToReturn;
        int row = getRowAndColFromId(id).get(0);
        int col = getRowAndColFromId(id).get(1);
        MineSweeperTile[][] temp =
                new MineSweeperTile[MineSweeperBoard.getNumRows()][MineSweeperBoard.getNumCols()];
        for (int curRow = 0; curRow < MineSweeperBoard.getNumRows(); curRow++) {
            for (int curCol = 0; curCol < MineSweeperBoard.getNumCols(); curCol++) {
                temp[curRow][curCol] = tiles.get(curRow * MineSweeperBoard.getNumCols() + curCol);
            }
        }
        tileToReturn = getAdjacentTiles(temp, row, col);
        return tileToReturn;
    }

    /**
     * Get the adjacent tiles of a tile using its columns and rows, then get the four corners
     * by call getAdjacentTiles_recursive below
     *
     * @param tiles the list of all tiles
     * @param row   the row number of the given tile
     * @param col   the columns number of the given tile
     * @return Returns a list of all adjacent tiles (including four corners)
     */
    static List<MineSweeperTile> getAdjacentTiles(MineSweeperTile[][] tiles, int row, int col) {
        List<MineSweeperTile> tileToReturn = new ArrayList<>();
        for (int curRow = 0; curRow < MineSweeperBoard.getNumRows(); curRow++) {
            for (int curCol = 0; curCol < MineSweeperBoard.getNumCols(); curCol++) {
                if (curRow + 1 == row && curCol + 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow - 1 == row && curCol + 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow + 1 == row && curCol - 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow - 1 == row && curCol - 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow == row && curCol + 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow - 1 == row && curCol == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow + 1 == row && curCol == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                } else if (curRow == row && curCol - 1 == col) {
                    tileToReturn.add(tiles[curRow][curCol]);
                }
            }
        }
        return tileToReturn;
    }

    /**
     * Return a list that contains the row and columns of the corresponding tile with the given id.
     *
     * @param id the id of the tile
     * @return Returns a list with row number at index 0 and columns number at index 1
     */
    static List<Integer> getRowAndColFromId(int id) {
        List<Integer> rowAndCol = new ArrayList<>();
        int row = (id - 1) / MineSweeperBoard.getNumCols();
        int col = (id - 1) % MineSweeperBoard.getNumCols();
        rowAndCol.add(row);
        rowAndCol.add(col);
        return rowAndCol;
    }
}
