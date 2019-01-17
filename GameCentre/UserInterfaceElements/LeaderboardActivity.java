package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.Score;
import fall2018.csc2017.GameCentre.DataManagers.ScoreboardManager;
import fall2018.csc2017.GameCentre.DataManagers.User;

/**
 * The class responsible to generate and show the leader board to the user.
 */
public class LeaderboardActivity extends AppCompatActivity {

    /**
     * Default values for the type of game, difficulty, and user score data shown.
     */
    private static final String DEFAULT_GAME = "SlidingTiles";
    private static final String DEFAULT_DIFF = "Medium";
    private static final boolean DEFAULT_TOGG = true;

    /**
     * The save files for each game to be used.
     */
    public static final String DEFAULT_SAVE_FILENAME = "slidingtiles_highscore.ser";
    public static final String SLIDING_SAVE_FILENAME = "slidingtiles_highscore.ser";
    public static final String MINESWEEPER_SAVE_FILENAME = "minesweeper_highscore.ser";
    public static final String POWERS_SAVE_FILENAME = "powersplus_highscore.ser";

    /**
     * Default number of columns to display in the GridView
     */
    private static final int NUM_COLUMNS = 3;

    /**
     * The file manager for scoreboard
     */
    private FileManager<ScoreboardManager> scoreFileManager = new FileManager<>();

    /**
     * Class variables for the type of game, difficulty, and user score data that the user will
     * input through interaction with the activity.
     */
    private String difficulty = null;
    private boolean displayAllUsers;
    private String gameSelected = null;

    /**
     * The toggle button on whether specific or all user score data is shown.
     */
    private ToggleButton userToggle;

    /**
     * The GridView to display all score elements.
     */
    private GridView scoreboard;

    /**
     * Each TextView representing each item to display in the GridView
     */
    private ArrayList<View> scoreItems = new ArrayList<>();

    /**
     * The current (logged in) user that is opening the leader board.
     */
    private User curUser;

    /**
     * Runs the necessary code when the activity is first executed.
     *
     * @param savedInstanceState The previous state (if it exists) of the activity to come back to
     *                           It is null if there is no previous state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Fetches the current user and assigns default values
        curUser = (User) getIntent().getSerializableExtra("CurUser");
        difficulty = DEFAULT_DIFF;
        gameSelected = DEFAULT_GAME;
        displayAllUsers = DEFAULT_TOGG;

        // Initialization of scores and difficulties
        initializeScores(this);
        initializeDifficulties();

        // Adds input listening for difficulty radios, game spinner, and toggle
        addDifficultyListener();

        userToggle = findViewById(R.id.ToggleUserButton);
        userToggle.setChecked(!DEFAULT_TOGG);
        changeUserToggleListener();

        addGameListenerOnSpinnerSelection();

        // Adds output of scoreboard's GridView
        scoreboard = findViewById(R.id.ScoreBoardGridView);
        scoreboard.setNumColumns(NUM_COLUMNS);

    }

    /**
     * Helper method to initialize a radio button based on default difficulty.
     */
    private void initializeDifficulties() {
        RadioGroup difficultyForGame = findViewById(R.id.SelectDifficultyRadio);
        switch (difficulty) {
            case "Easy":
                difficultyForGame.check(R.id.radio_easy);
                break;
            case "Medium":
                difficultyForGame.check(R.id.radio_medium);
                break;
            case "Hard":
                difficultyForGame.check(R.id.radio_hard);
                break;
            default:
                difficultyForGame.check(R.id.radio_medium);
                break;
        }
    }

    /**
     * Toggles the condition for the only-user scores to be displayed, or all users'
     * scores to be displayed based on the click of the ToggleButton from the user.
     * <p>
     * Also regenerates the score GridView based on any input change.
     */
    public void changeUserToggleListener() {
        userToggle.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllUsers = !userToggle.isChecked();
                // Generates every time a value is updated
                generateLeaderboard(gameSelected, difficulty, displayAllUsers);
            }
        });
    }

    /**
     * Selects the game to display the leader board for and updates the values every time
     * the user changes the game selected in the Spinner.
     * <p>
     * Also regenerates the GridView board if the user selects a different game.
     */
    public void addGameListenerOnSpinnerSelection() {
        final Spinner gameSpinner = findViewById(R.id.SelectGameList);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                gameSelected = gameSpinner.getSelectedItem().toString();
                // Generates every time a value is updated
                generateLeaderboard(gameSelected, difficulty, displayAllUsers);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /**
     * Changes the difficulty setting based on which RadioButton the user selects.
     * <p>
     * Also regenerates the GridView board for any difficulty change the user inputs.
     */
    private void addDifficultyListener() {
        final RadioGroup difficultyForGame = findViewById(R.id.SelectDifficultyRadio);
        difficultyForGame.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final RadioButton selected = difficultyForGame.findViewById(checkedId);
                difficulty = selected.getText().toString();
                // Generates every time a value is updated
                generateLeaderboard(gameSelected, difficulty, displayAllUsers);
            }
        });
    }

    /**
     * Displays the scoreboard to the activity's GridView.
     */
    public void display() {
        scoreboard.setAdapter(new LeaderboardAdapter(scoreItems,
                scoreboard.getColumnWidth(),
                scoreboard.getHeight() / (ScoreboardManager.getSHOWLIMIT() + 1)));
    }

    /**
     * Updates the elements of the scoreboard given what the user wants in terms of
     * game type, game difficulty, and which users to show.
     *
     * @param difficulty: The difficulty the user wishes to see.
     * @param allUsers:   Whether the user wants to see all user scores or just theirs.
     * @param gameScores: The scores for the specified game.
     */
    public void updateScores(String difficulty,
                             boolean allUsers,
                             ScoreboardManager gameScores) {

        List<Score> new_scores;
        // Gets the top 10 scores from all users or just the logged in user.
        new_scores = allUsers ? (gameScores.getTopScoresByDifficulty(difficulty)) :
                (gameScores.getTopScoresByUser(curUser, difficulty));

        // Initial header of Rank, User, Score, and possibly more.
        updateItemText(0, "Rank");
        updateItemText(1, "User");
        updateItemText(2, "Score");

        for (int i = 1; i < (new_scores.size() + 1); i++) {
            // Change the rank, username, and score
            updateItemText(NUM_COLUMNS * i, i + "");
            updateItemText(NUM_COLUMNS * i + 1,
                    new_scores.get(i - 1).getUser().getUsername());
            updateItemText(NUM_COLUMNS * i + 2, new_scores.get(i - 1).getValue() + "");
        }
    }

    /**
     * Helper method that updates the score item for the grid
     * with new values at the given position.
     *
     * @param index: The position of the item to update.
     * @param text:  The new text the item will get.
     */
    private void updateItemText(int index, String text) {
        TextView temp;
        temp = (TextView) scoreItems.get(index);
        temp.setText(text);
        scoreItems.set(index, temp);
    }

    /**
     * Creates and sets all the scores in scoreItems as null-strings.
     *
     * @param context: The given context of the class activity.
     */
    public void initializeScores(Context context) {
        this.scoreItems.clear();
        for (int row = 0; row < (ScoreboardManager.getSHOWLIMIT() + 1) * NUM_COLUMNS; row++) {
            TextView temp = new TextView(context);
            temp.setText("");
            this.scoreItems.add(temp);
        }
    }

    /**
     * Generates the actual leader board based on specified inputs. This method is executed on
     * default values to ensure some generation of the board on activity start up.
     *
     * @param game       The game the user selects (to display) using the game selecting Spinner.
     * @param difficulty The difficulty the user inputs using the difficulty RadioButtons.
     * @param displayAll Whether the user wants to display all users' scores (true) or only their
     *                   scores (false.
     */
    private void generateLeaderboard(String game, String difficulty, boolean displayAll) {
        // No difficulty or game selected.
        if (game == null || difficulty == null) {
            Toast.makeText(getApplicationContext(), "Select a difficulty to continue!", Toast.LENGTH_SHORT).show();
        } else {
            String fileName;
            switch (game) {
                case "SlidingTiles":
                    fileName = SLIDING_SAVE_FILENAME;
                    break;
                case "MineSweeper":
                    fileName = MINESWEEPER_SAVE_FILENAME;
                    break;
                case "PowersPlus":
                    fileName = POWERS_SAVE_FILENAME;
                    break;
                default:
                    fileName = DEFAULT_SAVE_FILENAME; // by default
                    break;
            }
            ScoreboardManager scoreboardManager = scoreFileManager.loadFromFile(fileName, this);
            // Clears, updates and displays scores.
            initializeScores(this);
            updateScores(difficulty, displayAll, scoreboardManager);
            display();
        }
    }
}
