package fall2018.csc2017.GameCentre.SlidingTiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.DataManagers.SavedGamesManager;
import fall2018.csc2017.GameCentre.DataManagers.StateManager;
import fall2018.csc2017.GameCentre.DataManagers.User;
import fall2018.csc2017.GameCentre.DataManagers.UserManager;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.SuperClasses.StartingActivity;
import fall2018.csc2017.GameCentre.UserInterfaceElements.GameHubActivity;
import fall2018.csc2017.GameCentre.UserInterfaceElements.LaunchCentre;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class SlidingTilesStartingActivity extends StartingActivity {

    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

    /**
     * Handles the files for the user database
     */
    private FileManager<UserManager> userFileManager = new FileManager<>();

    /**
     * Handles the files for the slidingTilesBoardManager
     */
    private FileManager<SlidingTilesBoardManager> boardFileManager = new FileManager<>();

    /**
     * The state manager for sliding tiles.
     */
    public static StateManager<List> stateManager = new StateManager<>();

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager slidingTilesBoardManager;

    /**
     * The current logged in user that is being passed from activity to activity
     */
    private User curUser;

    /**
     * The user database that we will load from file
     */
    private UserManager userManager;

    /**
     * What happens when SlidingTilesStartingActivity is created
     *
     * @param savedInstanceState the current activity's previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fetch the current user passed in from the last activity
        curUser = (User) getIntent().getSerializableExtra("CurUser");

        //Fetch the user database
        slidingTilesBoardManager = new SlidingTilesBoardManager();
        userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, this);

        //Save to the temporary save file
        boardFileManager.saveToFile(TEMP_SAVE_FILENAME, this, slidingTilesBoardManager);

        //Set the view
        setContentView(R.layout.activity_sliding_starting_);
        //Create the buttons
        addStartButtonListener();
        addLoadButtonListener();
    }

    /**
     * Activate the start button.
     */
    @Override
    protected void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingTilesBoardManager = new SlidingTilesBoardManager();
                slidingTilesBoardManager.setClicks(0);
                switchToGame();
            }
        });
    }

    /**
     * Activate the load button.
     */
    @Override
    protected void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load the user data base to retrieve saved games
                Activity host = (Activity) v.getContext();
                userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, host);

                //Get the current user's saved games
                SavedGamesManager<SlidingTilesBoardManager> curSaves = getSlidingTilesBoardManagerSavedGamesManager();

                //Create a fresh state manager because of our choice of implementation
                stateManager = new StateManager<>();

                //Get the save game for this difficulty
                slidingTilesBoardManager = curSaves.getSave(GameHubActivity.difficulty);

                if (slidingTilesBoardManager == null){
                    Toast.makeText(getApplicationContext(), "There are no saved games to load!", Toast.LENGTH_SHORT).show();
                }else{
                    //Temporary save
                    boardFileManager.saveToFile(TEMP_SAVE_FILENAME, host, slidingTilesBoardManager);
                    makeToastLoadedText();
                    switchToGame();
                }
            }
        });
    }

    /**
     * Get the saved games manager for sliding tiles
     *
     * @return Return the SavedGamesManager for this current user for sliding tiles
     */
    //Suppressing warnings here because SavedGamesManager is generic
    @SuppressWarnings("unchecked")
    private SavedGamesManager<SlidingTilesBoardManager> getSlidingTilesBoardManagerSavedGamesManager() {
        //Get the current user's saved games
        User currentUser;
        currentUser = userManager.getUser(curUser);
        SavedGamesManager<SlidingTilesBoardManager> curSaves;
        curSaves = currentUser.userSavedGames.get("SlidingTiles");
        return curSaves;
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        slidingTilesBoardManager = boardFileManager.loadFromFile(TEMP_SAVE_FILENAME, this);
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    @Override
    protected void switchToGame() {
        Intent tmp = new Intent(this, SlidingTilesGameActivity.class);
        //Temporary save
        boardFileManager.saveToFile(TEMP_SAVE_FILENAME, this, slidingTilesBoardManager);
        //saveToFile(TEMP_SAVE_FILENAME);
        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);
        startActivity(tmp);
    }
}
