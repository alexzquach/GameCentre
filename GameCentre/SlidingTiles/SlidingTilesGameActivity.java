package fall2018.csc2017.GameCentre.SlidingTiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

import fall2018.csc2017.GameCentre.UserInterfaceElements.TilesAdapter;
import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.UserInterfaceElements.GameHubActivity;
import fall2018.csc2017.GameCentre.UserInterfaceElements.LaunchCentre;
import fall2018.csc2017.GameCentre.UserInterfaceElements.LeaderboardActivity;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.SavedGamesManager;
import fall2018.csc2017.GameCentre.DataManagers.Score;
import fall2018.csc2017.GameCentre.DataManagers.ScoreboardManager;
import fall2018.csc2017.GameCentre.DataManagers.User;
import fall2018.csc2017.GameCentre.DataManagers.UserManager;

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private SlidingTilesBoardManager slidingTilesBoardManager;

    /**
     * The user manager
     */
    private UserManager userManager;

    /**
     * Where the high scores for sliding tiles are saved
     */
    public static final String SLIDING_SAVE_FILENAME = "slidingtiles_highscore.ser";

    /**
     * The scoreboard manager for sliding tiles
     */
    public static ScoreboardManager slidingTilesScores = new ScoreboardManager(false);

    /**
     * The file manager for scoreboard
     */
    private FileManager<ScoreboardManager> scoreboardFileManager = new FileManager<>();

    /**
     * The file manager for the user database
     */
    private FileManager<UserManager> userFileManager = new FileManager<>();

    /**
     * The file manager for the board
     */
    private FileManager<SlidingTilesBoardManager> boardFileManager = new FileManager<>();

    /**
     * The buttons to display.
     */
    private ArrayList<View> tileButtons;

    /**
     * The grid view and calculated columnWidth and columnHeight
     */
    private SlidingTilesGestureDetectGridView gridView;
    private static int columnWidth, columnHeight;
    public static User curUser;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new TilesAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * What happens when we load the activity
     *
     * @param savedInstanceState Represents the current activities previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Fetch the current user
        curUser = (User) getIntent().getSerializableExtra("CurUser");

        super.onCreate(savedInstanceState);

        //Load a board from the temp save file
        slidingTilesBoardManager = boardFileManager.loadFromFile(SlidingTilesStartingActivity.TEMP_SAVE_FILENAME, this);

        //Loads the scores database for sliding tiles
        slidingTilesScores = scoreboardFileManager.loadFromFile(LeaderboardActivity.SLIDING_SAVE_FILENAME, this);

        //Loads the user database for later use
        userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, this);

        //Creates the buttons
        createTileButtons(this);
        setContentView(R.layout.activity_sliding_main);
        addUndoListener();
        addScoreBoardButtonListener();

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SlidingTilesBoard.getNumCols());
        gridView.setSlidingTilesBoardManager(slidingTilesBoardManager);
        slidingTilesBoardManager.getSlidingTilesBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / SlidingTilesBoard.getNumCols();
                        columnHeight = displayHeight / SlidingTilesBoard.getNumRows();

                        display();
                    }
                });
    }

    /**
     * Adds a listener and scoreboard button functionality to the main game UI.
     */
    private void addScoreBoardButtonListener() {
        ImageButton scoreboard = findViewById(R.id.imgScoreboard2);
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLeaderboard();
            }
        });
    }

    /**
     * Switches to the leaderboard when the button is clicked
     */
    private void switchToLeaderboard() {
        Intent tmp = new Intent(this, LeaderboardActivity.class);

        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);

        startActivity(tmp);
    }

    /**
     * Adds a listener and undo functionality to the UI undo button.
     */
    //Suppressing warnings because the StateManager class is a generic class and we as the
    //programmers know what is inside that class
    @SuppressWarnings("unchecked")
    private void addUndoListener() {
        final Button undo = findViewById(R.id.buttonUndo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SlidingTilesTile> undoneMove = SlidingTilesStartingActivity.stateManager.undo();
                if (undoneMove == null) {
                    Toast.makeText(getApplicationContext(), "No more moves left to undo!", Toast.LENGTH_SHORT).show();
                } else {
                    //Undoes the move and displays
                    if (slidingTilesBoardManager.gameOver()) {
                        Toast.makeText(getApplicationContext(), "The Game is over! " +
                                        "Start a new game or view your score on the scoreboard!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        slidingTilesBoardManager.getSlidingTilesBoard().undo(undoneMove);
                        slidingTilesBoardManager.setClicks(slidingTilesBoardManager.getClicks() - 1);
                        display();
                    }
                }
            }
        });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        SlidingTilesBoard slidingTilesBoard = slidingTilesBoardManager.getSlidingTilesBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != SlidingTilesBoard.getNumRows(); row++) {
            for (int col = 0; col != SlidingTilesBoard.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(slidingTilesBoard.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    //Suppressing warnings here because the SavedGamesManager class is a generic class
    //but we as the programmer know what object that the SavedGamesManager will currently contain
    @SuppressWarnings("unchecked")
    private void updateTileButtons() {
        //Gets the current objects
        SlidingTilesBoard slidingTilesBoard = slidingTilesBoardManager.getSlidingTilesBoard();
        User currentUser;
        currentUser = userManager.getUser(curUser);
        SavedGamesManager<SlidingTilesBoardManager> curSavedGames = currentUser.userSavedGames.get("SlidingTiles");

        //Auto saves every turn by using the current user's saved games (if its not a win turn)
        if (!slidingTilesBoardManager.gameOver()) {
            saveGame(currentUser, curSavedGames, slidingTilesBoardManager);
        }

        //Updates the click counter
        updateScore();

        int nextPos = 0;
        for (View b : tileButtons) {
            int row = nextPos / SlidingTilesBoard.getNumRows();
            int col = nextPos % SlidingTilesBoard.getNumCols();
            b.setBackgroundResource(slidingTilesBoard.getTile(row, col).getBackground());
            nextPos++;
        }

        //Checks to see if the game is over after updating the tiles
        if (slidingTilesBoardManager.gameOver()) {
            //Erase the saves when the game is finished
            saveGame(currentUser, curSavedGames, null);
            //Save the high score
            saveScore();
        }
    }

    /**
     * Updates the clicks text view
     */
    private void updateScore() {
        //Updates the click counter at the top to notify the user of their score
        TextView clickCounter = findViewById(R.id.txtClicks);
        String msg = "" + slidingTilesBoardManager.getClicks();
        clickCounter.setText(msg);
    }

    /**
     * Saves the game object to file
     *
     * @param currentUser The current user playing
     * @param curSavedGames The current user's saved games
     * @param o The game object to be saved
     */
    private void saveGame(User currentUser, SavedGamesManager<SlidingTilesBoardManager> curSavedGames, SlidingTilesBoardManager o) {
        curSavedGames.setSave(GameHubActivity.difficulty, o);
        //Replaces the updated user in the database
        currentUser.userSavedGames.put("SlidingTiles", curSavedGames);
        userManager.replaceUser(currentUser);
        userFileManager.saveToFile(LaunchCentre.USER_SAVE_FILENAME, this, userManager);
    }

    /**
     * Saves the score of the user into the scoreboard.
     */
    void saveScore() {
        Score curUserScore = new Score(curUser,
                "SlidingTiles",
                GameHubActivity.difficulty,
                slidingTilesBoardManager.getClicks());
        slidingTilesScores.addScore(curUserScore);
        scoreboardFileManager.saveToFile(SLIDING_SAVE_FILENAME, this, slidingTilesScores);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        boardFileManager.saveToFile(SlidingTilesStartingActivity.TEMP_SAVE_FILENAME, this, slidingTilesBoardManager);
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}

