package fall2018.csc2017.GameCentre.DataManagers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

import fall2018.csc2017.GameCentre.MineSweeper.MineSweeperBoardManager;
import fall2018.csc2017.GameCentre.PowersPlus.PowersPlusBoardManager;
import fall2018.csc2017.GameCentre.SlidingTiles.SlidingTilesBoardManager;

/**
 * User class with equal user and other properties.
 */
public class User implements Serializable {

    /**
     * The name of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * This hashmap contains all the saved games for user.
     */
    public HashMap<String, SavedGamesManager> userSavedGames = new HashMap<>();

    /**
     * The constructor which takes the following parameter and initialize the value.
     *
     * @param username the name of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Return user name.
     *
     * @return the user name to return.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return user's password.
     *
     * @return the password to return.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Create three locations to store the saved games for games SlidingTiles, MineSweeper and
     * PowerPlus.  Will expand as we need to create new saved games for new users
     */
    public void createSavedGames() {
        SavedGamesManager<SlidingTilesBoardManager> SlidingTilesSaves = new SavedGamesManager<>();
        SavedGamesManager<MineSweeperBoardManager> MineSweeperSaves = new SavedGamesManager<>();
        SavedGamesManager<PowersPlusBoardManager> PowersPlusSaves = new SavedGamesManager<>();
        userSavedGames.put("SlidingTiles", SlidingTilesSaves);
        userSavedGames.put("MineSweeper", MineSweeperSaves);
        userSavedGames.put("PowersPlus", PowersPlusSaves);
    }

    /**
     * Adds an appropriate saved game slot for new games that were added after the user joined
     *
     * @param game The game that was added
     */
    public void addSave(String game){
        switch (game){
            case "MineSweeper":
                SavedGamesManager<MineSweeperBoardManager> MineSweeperSaves = new SavedGamesManager<>();
                userSavedGames.put(game, MineSweeperSaves);
                break;
            case "PowersPlus":
                SavedGamesManager<PowersPlusBoardManager> PowersPlusSaves = new SavedGamesManager<>();
                userSavedGames.put(game, PowersPlusSaves);
                break;
        }
    }

    /**
     * Compares user and another object for equality, we only need to compare usernames
     * if they are both user objects because same username is same user
     *
     * @param o the object to compare to
     * @return returns true if the user is the same as the object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }
}
