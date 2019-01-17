package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.MineSweeper.MineSweeperBoard;
import fall2018.csc2017.GameCentre.MineSweeper.MineSweeperStartingActivity;
import fall2018.csc2017.GameCentre.PowersPlus.PowersPlusBoardManager;
import fall2018.csc2017.GameCentre.PowersPlus.PowersPlusStartingActivity;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.ScoreboardManager;
import fall2018.csc2017.GameCentre.SlidingTiles.SlidingTilesBoard;
import fall2018.csc2017.GameCentre.SlidingTiles.SlidingTilesStartingActivity;
import fall2018.csc2017.GameCentre.DataManagers.User;

/**
 * The game selection hub, where the user selects what to play when they have logged on
 */
public class GameHubActivity extends AppCompatActivity {

    /**
     * The file manager for scoreboards
     */
    private FileManager<ScoreboardManager> scoreboardFileManager = new FileManager<>();

    /**
     * Keeps track of the difficulty that the user selected, can be generalized
     */
    public static String difficulty;

    /**
     * The current logged in user
     */
    private User curUser;

    /**
     * What happens when the activity is loaded
     *
     * @param savedInstanceState Represents the current activity's last previous saved state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make general later

        //Fetches the high scores
        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, sliding tiles
        checkIfEmpty(LeaderboardActivity.SLIDING_SAVE_FILENAME, false);
        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, minesweeper
        checkIfEmpty(LeaderboardActivity.MINESWEEPER_SAVE_FILENAME, false);
        //Checks to see if the file is empty, if so, create a new empty file with a default scoreboard, PowersPlus
        checkIfEmpty(LeaderboardActivity.POWERS_SAVE_FILENAME, true);

        //Reinitialize difficulty
        difficulty = null;

        //Fetches the current user
        curUser = (User) getIntent().getSerializableExtra("CurUser");
        //Sets the view
        setContentView(R.layout.activity_gamehub);
        //Adds the necessary buttons
        addSlidingTilesButtonListener();
        addMineSweeperButtonListener();
        addPowersPlusButtonListener();
        addLeaderBoardButtonListener();
        addDifficultyListener();
        getAddLogOutButtonListener();
    }

    /**
     * Checks to see if the highscore file for a particular game is empty, if so
     * create a new file
     *
     * @param SaveFilename The game we are trying to retrieve highscores for
     */
    private void checkIfEmpty(String SaveFilename, boolean isHighToLow) {
        ScoreboardManager potentialScoreBoardManager;
        potentialScoreBoardManager = scoreboardFileManager.loadFromFile(SaveFilename,
                this);
        if (potentialScoreBoardManager == null) {
            potentialScoreBoardManager = new ScoreboardManager(isHighToLow);
            scoreboardFileManager.saveToFile(SaveFilename,
                    this, potentialScoreBoardManager);
        }
    }

    /**
     * The logout button listener
     */
    private void getAddLogOutButtonListener() {
        Button logOut = findViewById(R.id.buttonLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLaunchCentre();
                Toast.makeText(getApplicationContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * The difficulty button listener to check what difficulty the user selected
     */
    private void addDifficultyListener() {
        final RadioGroup difficultySlidingTiles = findViewById(R.id.slidingTilesDiff);
        //The on checked listener
        difficultySlidingTiles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton selected = difficultySlidingTiles.findViewById(checkedId);
                //Change the difficulty according to what the user selected
                if (selected.getText().equals("Medium")) {
                    difficulty = "Medium";
                } else if (selected.getText().equals("Easy")) {
                    difficulty = "Easy";
                } else if (selected.getText().equals("Hard")) {
                    difficulty = "Hard";
                }
            }
        });

    }

    /**
     * Checks to see if the user has selected Minesweeper as their game
     */
    private void addMineSweeperButtonListener() {
        ImageButton mineSweeper = findViewById(R.id.imgMineSweeper);
        mineSweeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (difficulty == null) {
                    Toast.makeText(getApplicationContext(), "Please select a difficulty to continue!", Toast.LENGTH_SHORT).show();
                } else {
                    int diff = getDifficultyNumber();
                    MineSweeperBoard.setDifficulty(diff);
                    //setMineSweeperDifficulty();
                    switchToGame(MineSweeperStartingActivity.class);
                }
            }
        });
    }

    /**
     * Checks to see if the user has selected Powersplus as their game
     */
    private void addPowersPlusButtonListener() {
        ImageButton powersPlus = findViewById(R.id.imgPowersPlus);
        powersPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (difficulty == null) {
                    Toast.makeText(getApplicationContext(), "Please select a difficulty to continue!", Toast.LENGTH_SHORT).show();
                } else {
                    int diff = getDifficultyNumber();
                    PowersPlusBoardManager.setDifficulty(diff);
                    //setPowersPlusDifficulty();
                    switchToGame(PowersPlusStartingActivity.class);
                }
            }
        });

    }

    /**
     * Checks to see if the user has selected sliding tiles as their game
     */
    private void addSlidingTilesButtonListener() {
        ImageButton slidingTiles = findViewById(R.id.imgSlidingTiles);
        slidingTiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (difficulty == null) {
                    Toast.makeText(getApplicationContext(), "Please select a difficulty to continue!", Toast.LENGTH_SHORT).show();
                } else {
                    int diff = getDifficultyNumber();
                    SlidingTilesBoard.setDifficulty(diff);
                    //setSlidingTilesDifficulty();
                    switchToGame(SlidingTilesStartingActivity.class);
                }
            }
        });
    }

    /**
     * Gets the difficulty number in order to determine the correct sizing for games
     *
     * @return Returns an integer representing the difficulty based on the radio button
     */
    private int getDifficultyNumber(){
        switch (difficulty) {
            case "Medium":
                return 0;
            case "Easy":
                return -1;
            default:
                return 1;
            }
    }

    /**
     * Opens the scoreboard when selected
     */
    private void addLeaderBoardButtonListener() {
        ImageButton scoreboard = findViewById(R.id.imgScoreboard);
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLeaderboard();
            }
        });
    }

    /**
     * Switches to the leaderboard
     */
    private void switchToLeaderboard() {
        Intent tmp = new Intent(this, LeaderboardActivity.class);

        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);

        startActivity(tmp);
    }

    /**
     * Switches to the launch centre
     */
    private void switchToLaunchCentre() {
        Intent tmp = new Intent(this, LaunchCentre.class);
        finish();
        startActivity(tmp);
    }

    /**
     * Switches to game that was selected
     */
    private void switchToGame(Class nextActivity) {
        Intent tmp = new Intent(this, nextActivity);
        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);
        startActivity(tmp);
    }

    /**
     * Disables the back button
     */
    @Override
    public void onBackPressed() {
    }

}
