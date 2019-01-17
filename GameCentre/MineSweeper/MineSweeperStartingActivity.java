package fall2018.csc2017.GameCentre.MineSweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.SuperClasses.StartingActivity;
import fall2018.csc2017.GameCentre.UserInterfaceElements.GameHubActivity;
import fall2018.csc2017.GameCentre.UserInterfaceElements.LaunchCentre;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.SavedGamesManager;
import fall2018.csc2017.GameCentre.DataManagers.StateManager;
import fall2018.csc2017.GameCentre.DataManagers.User;

/**
 * The MineSweeper starting activity
 */
public class MineSweeperStartingActivity extends StartingActivity {

    /**
     * Handles the files for the MineSweeperBoardManager
     */
    private FileManager<MineSweeperBoardManager> boardFileManager = new FileManager<>();

    /**
     * The state manager for sliding tiles.
     */
    public static StateManager<List> stateManager = new StateManager<>();

    /**
     * The board manager.
     */
    private MineSweeperBoardManager mineSweeperBoardManager;

    /**
     * What happens when MineSweeperStartingActivity is created
     *
     * @param savedInstanceState the current activity's previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fetch the current user passed in from the last activity
        curUser = (User) getIntent().getSerializableExtra("CurUser");

        //Fetch the user database
        mineSweeperBoardManager = new MineSweeperBoardManager();
        userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, this);

        //Save to the temporary save file
        boardFileManager.saveToFile(TEMP_SAVE_FILENAME, this, mineSweeperBoardManager);

        //Set the view
        setContentView(R.layout.activity_minesweeper_starting);
        //Create the buttons
        addStartButtonListener();
        addLoadButtonListener();
    }

    /**
     * Activate the start button.
     */
    @Override
    protected void addStartButtonListener() {
        ImageButton startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mineSweeperBoardManager = new MineSweeperBoardManager();
                mineSweeperBoardManager.setClicks(0);
                switchToGame();
            }
        });
    }

    /**
     * Activate the load button.
     */
    @Override
    protected void addLoadButtonListener() {
        ImageButton loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load the user data base to retrieve saved games
                Activity host = (Activity) v.getContext();
                userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, host);

                //Get the current user's saved games
                SavedGamesManager<MineSweeperBoardManager> curSaves = getMineSweeperBoardManagerSavedGamesManager();

                //Create a fresh state manager because of our choice of implementation
                stateManager = new StateManager<>();

                //Determine what saved game to load based on user selected difficulty
                mineSweeperBoardManager = curSaves.getSave(GameHubActivity.difficulty);

                if (mineSweeperBoardManager == null) {
                    Toast.makeText(getApplicationContext(), "There are no saved games to load!", Toast.LENGTH_SHORT).show();
                } else {
                    //Temporary save
                    boardFileManager.saveToFile(TEMP_SAVE_FILENAME, host, mineSweeperBoardManager);
                    makeToastLoadedText();
                    switchToGame();
                }
            }
        });
    }

    /**
     * Get the current saved games manager for MineSweeper
     *
     * @return Returns the SavedGamesManager for MineSweeper
     */
    //Suppressing warning here because SavedGamesManager is a generic class
    @SuppressWarnings("unchecked")
    private SavedGamesManager<MineSweeperBoardManager> getMineSweeperBoardManagerSavedGamesManager() {
        User currentUser;
        currentUser = userManager.getUser(curUser);
        return currentUser.userSavedGames.get("MineSweeper");
    }

    /**
     * Read the temporary board from disk when resuming the game.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mineSweeperBoardManager = boardFileManager.loadFromFile(TEMP_SAVE_FILENAME, this);
    }

    /**
     * Switch to the MineSweeperGameActivity view to play the game.
     */
    @Override
    protected void switchToGame() {
        Intent tmp = new Intent(this, MineSweeperGameActivity.class);
        //Temporary save
        boardFileManager.saveToFile(TEMP_SAVE_FILENAME, this, mineSweeperBoardManager);
        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);
        startActivity(tmp);
    }
}

