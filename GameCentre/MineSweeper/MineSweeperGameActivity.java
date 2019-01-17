package fall2018.csc2017.GameCentre.MineSweeper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
public class MineSweeperGameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private MineSweeperBoardManager mineSweeperBoardManager;

    /**
     * The user manager
     */
    private UserManager userManager;

    /**
     * Where the high scores for sliding tiles are saved
     */
    public static final String MINESWEEPER_SAVE_FILENAME = "minesweeper_highscore.ser";

    /**
     * The scoreboard manager for sliding tiles
     */
    public static ScoreboardManager minesweeperScores = new ScoreboardManager(false);

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
    private FileManager<MineSweeperBoardManager> boardFileManager = new FileManager<>();

    /**
     * The buttons to display.
     */
    private ArrayList<View> tileButtons;

    /**
     * Grid View and calculated column height and width based on device size
     */
    private MineSweeperGestureDetectGridView gridView;
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
        mineSweeperBoardManager = boardFileManager.loadFromFile(MineSweeperStartingActivity.TEMP_SAVE_FILENAME, this);

        //Loads the scores database for sliding tiles
        minesweeperScores = scoreboardFileManager.loadFromFile(LeaderboardActivity.MINESWEEPER_SAVE_FILENAME, this);

        //Loads the user database for later use
        userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, this);

        //Creates the buttons
        createTileButtons(this);
        setContentView(R.layout.activity_minesweeper_main);
        addScoreBoardButtonListener();

        // Add View to activity
        gridView = findViewById(R.id.gridMS);
        gridView.setNumColumns(MineSweeperBoard.getNumCols());
        gridView.setMineSweeperBoardManager(mineSweeperBoardManager);
        mineSweeperBoardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / MineSweeperBoard.getNumCols();
                        columnHeight = displayHeight / MineSweeperBoard.getNumRows();

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
     * Switches to leaderboard after the leaderboard button has been clicked
     */
    private void switchToLeaderboard() {
        Intent tmp = new Intent(this, LeaderboardActivity.class);

        //Store the current user in the next activity
        tmp.putExtra("CurUser", curUser);

        startActivity(tmp);
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        MineSweeperBoard mineSweeperBoard = mineSweeperBoardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != MineSweeperBoard.getNumRows(); row++) {
            for (int col = 0; col != MineSweeperBoard.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(mineSweeperBoard.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    //Suppressing unchecked assignment warning because we are using generic classes
    @SuppressWarnings("unchecked")
    private void updateTileButtons() {
        MineSweeperBoard mineSweeperBoard = mineSweeperBoardManager.getBoard();
        User currentUser;
        currentUser = userManager.getUser(curUser);
        SavedGamesManager<MineSweeperBoardManager> curSavedGames = currentUser.userSavedGames.get("MineSweeper");
        //Auto saves every turn by using the current user's saved games (if its not a win turn)
        if (!mineSweeperBoardManager.gameOver()) {
            saveGame(currentUser, curSavedGames, mineSweeperBoardManager);
        }

        //Updates the click counter
        updateScore();

        int nextPos = 0;
        for (View b : tileButtons) {
            int row = nextPos / MineSweeperBoard.getNumCols();
            int col = nextPos % MineSweeperBoard.getNumCols();
            b.setBackgroundResource(mineSweeperBoard.getTile(row, col).getBackground());
            nextPos++;
        }

        //Checks to see if the game is over right when the game is over
        if (mineSweeperBoardManager.gameOver()) {
            //Deletes the saved games when the game is over
            saveGame(currentUser, curSavedGames, null);
            //Make sure the game is won in order to save a win
            if (mineSweeperBoardManager.isWin()) {
                //Saves the user's high score
                saveScore();
            } else {
                changeSmile();
            }
        }
    }

    /**
     * Updates the smile at the top of the game.
     */
    private void changeSmile() {
        ImageView smiley = findViewById(R.id.imgSmiley);
        smiley.setImageResource(R.drawable.saddey);
    }

    /**
     * Updates the click counter
     */
    private void updateScore() {
        //Updates the click counter at the top to notify the user of their score
        TextView clickCounter = findViewById(R.id.txtClicks);
        String msg = "" + mineSweeperBoardManager.getClicks();
        clickCounter.setText(msg);
    }

    /**
     * Saves the current game object
     *
     * @param currentUser   The current user
     * @param curSavedGames The current user's saved games
     * @param o             The game object to save
     */
    private void saveGame(User currentUser, SavedGamesManager<MineSweeperBoardManager> curSavedGames, MineSweeperBoardManager o) {
        curSavedGames.setSave(GameHubActivity.difficulty, o);
        //Replaces the updated user in the database
        currentUser.userSavedGames.put("MineSweeper", curSavedGames);
        userManager.replaceUser(currentUser);
        userFileManager.saveToFile(LaunchCentre.USER_SAVE_FILENAME, this, userManager);
    }

    /**
     * Saves the user's high score
     */
    private void saveScore() {
        Score curUserScore = new Score(curUser,
                "MineSweeper",
                GameHubActivity.difficulty,
                mineSweeperBoardManager.getClicks());
        minesweeperScores.addScore(curUserScore);
        scoreboardFileManager.saveToFile(MINESWEEPER_SAVE_FILENAME, this, minesweeperScores);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        boardFileManager.saveToFile(MineSweeperStartingActivity.TEMP_SAVE_FILENAME, this, mineSweeperBoardManager);
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}

