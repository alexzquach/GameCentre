package fall2018.csc2017.GameCentre.PowersPlus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
public class PowersPlusGameActivity extends AppCompatActivity implements Observer, View.OnTouchListener {

    /**
     * Responsible for detecting any swipes.
     */
    private GestureDetectorCompat detector;

    /**
     * The board manager.
     */
    private PowersPlusBoardManager powersPlusBoardManager;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * Where the high scores for powers plus are saved.
     */
    public static final String POWERS_SAVE_FILENAME = "powersplus_highscore.ser";

    /**
     * The scoreboard manager for powers plus.
     */
    public static ScoreboardManager powersPlusScore = new ScoreboardManager(true);

    /**
     * The file manager for scoreboard.
     */
    private FileManager<ScoreboardManager> scoreboardFileManager = new FileManager<>();

    /**
     * The file manager for the user database.
     */
    private FileManager<UserManager> userFileManager = new FileManager<>();

    /**
     * The file manager for the board.
     */
    private FileManager<PowersPlusBoardManager> boardFileManager = new FileManager<>();

    /**
     * The buttons to display.
     */
    private ArrayList<View> tileView;

    /**
     * Grid View and calculated column height and width based on device size.
     */
    private GridView gridView;

    /**
     * The specified column width and height for the Grid View to display.
     */
    private static int columnWidth, columnHeight;

    /**
     * The user currently playing the game.
     */
    public static User curUser;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileViews();
        gridView.setAdapter(new PowersPlusAdapter(tileView, columnWidth, columnHeight));
    }

    /**
     * What happens when we load the activity
     *
     * @param savedInstanceState Represents the current activities previous saved state
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Fetch the current user
        curUser = (User) getIntent().getSerializableExtra("CurUser");
        super.onCreate(savedInstanceState);

        //Load a board from the temp save file
        powersPlusBoardManager = boardFileManager.loadFromFile(PowersPlusStartingActivity.TEMP_SAVE_FILENAME, this);
        //Loads the scores database for sliding tiles
        powersPlusScore = scoreboardFileManager.loadFromFile(LeaderboardActivity.POWERS_SAVE_FILENAME, this);
        //Loads the user database for later use
        userManager = userFileManager.loadFromFile(LaunchCentre.USER_SAVE_FILENAME, this);

        setContentView(R.layout.activity_powers_main);

        //Creates the buttons
        createTileButtons(this);
        updateTileViews();
        addUndoListener();
        addScoreBoardButtonListener();

        // Display goal in txtGoal
        TextView textGoal = findViewById(R.id.txtGoal);
        int base = powersPlusBoardManager.getBase();
        int power = powersPlusBoardManager.getPower();
        @SuppressLint("DefaultLocale") String message = String.format("Swipe to get the %dth power of %d!", power, base);
        textGoal.setText(message);

        // Add View to activity
        gridView = findViewById(R.id.gridPowersPlus);
        gridView.setNumColumns(PowersPlusBoard.getNumCols());
        powersPlusBoardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / PowersPlusBoard.getNumCols();
                        columnHeight = displayHeight / PowersPlusBoard.getNumRows();

                        display();
                    }
                });

        PowersPlusOnSwipeListener onSwipeListener = new PowersPlusOnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                boolean moved;
                List<PowersPlusTile> powersPlusTilesCopy = powersPlusBoardManager.getBoard().getPowersPlusTiles();
                PowersPlusStartingActivity.stateManager.save(powersPlusTilesCopy);
                //Checks to see what direction the user swiped in
                if (direction == Direction.right) {
                    moved = powersPlusBoardManager.doMoveRight();
                } else if (direction == Direction.up) {
                    moved = powersPlusBoardManager.doMoveUp();
                } else if (direction == Direction.down) {
                    moved = powersPlusBoardManager.doMoveDown();
                } else {
                    moved = powersPlusBoardManager.doMoveLeft();
                }
                //Checks to see if the swipe was valid, if not, then pop the state in the state stack
                if (!moved) {
                    PowersPlusStartingActivity.stateManager.undo();
                }
                return true;
            }
        };
        detector = new GestureDetectorCompat(this, onSwipeListener);
        gridView.setOnTouchListener(this);
    }

    /**
     * Checks the user's touch move on the screen
     *
     * @param view        The current view
     * @param motionEvent the motion event
     * @return returns true if the touch is valid
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (powersPlusBoardManager.isGameOver()) {
            Toast.makeText(getApplicationContext(), "The Game is over! " +
                            "Start a new game or view your score on the scoreboard!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return detector.onTouchEvent(motionEvent);
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
     * Switches to the leaderboard after the leaderboard button has been clicked
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
        tileView = new ArrayList<>();
        for (int row = 0; row != PowersPlusBoard.getNumRows(); row++) {
            for (int col = 0; col != PowersPlusBoard.getNumCols(); col++) {
                TextView tmp = new TextView(context);
                setTextViewProperties(tmp, R.drawable.powersplus_tile_blank, "");
                this.tileView.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    //Suppressing warnings here because SavedGamesManager is generic
    @SuppressWarnings("unchecked")
    private void updateTileViews() {
        PowersPlusBoard powersPlusBoard = powersPlusBoardManager.getBoard();
        User currentUser;
        currentUser = userManager.getUser(curUser);
        SavedGamesManager<PowersPlusBoardManager> curSavedGames = currentUser.userSavedGames.get("PowersPlus");
        //Auto saves every turn by using the current user's saved games (if its not a win turn)
        if (!powersPlusBoardManager.isGameOver()) {
            saveGame(currentUser, curSavedGames, powersPlusBoardManager);
        }

        updateScore(); //Updates the score values
        updateHighest();

        int nextPos = 0;
        for (View t : tileView) {
            int row = nextPos / PowersPlusBoard.getNumCols();
            int col = nextPos % PowersPlusBoard.getNumCols();
            updateTile(powersPlusBoard.getTile(row, col), (TextView) t);
            nextPos++;
        }

        //Checks to see if the game is over once the move has been made and the board has been updated
        if (powersPlusBoardManager.isGameOver()) {
            //Erases the user's saved games when the game is over
            saveGame(currentUser, curSavedGames, null);
            saveScore();
            makeToastGameOver(powersPlusBoardManager.isWin());
        }
    }

    /**
     * Saves the current game object
     *
     * @param currentUser   The current user playing
     * @param curSavedGames The current user's saved games
     * @param o             The game to be saved
     */
    private void saveGame(User currentUser, SavedGamesManager<PowersPlusBoardManager> curSavedGames, PowersPlusBoardManager o) {
        curSavedGames.setSave(GameHubActivity.difficulty, o);
        //Replaces the updated user in the database
        currentUser.userSavedGames.put("PowersPlus", curSavedGames);
        userManager.replaceUser(currentUser);
        userFileManager.saveToFile(LaunchCentre.USER_SAVE_FILENAME, this, userManager);
    }

    //Updates the score view
    private void updateScore() {
        //Updates the click counter at the top to notify the user of their score
        TextView scoreView = findViewById(R.id.txtScore);
        String currentScore = "" + powersPlusBoardManager.getCurrentScore();
        scoreView.setText(currentScore);
    }

    //Updates the score view
    private void updateHighest() {
        //Updates the click counter at the top to notify the user of their score
        TextView scoreView = findViewById(R.id.txtHighest);
        String currentHighest = "Current Highest Power: " +
                powersPlusBoardManager.getHighestPower() + ".";
        scoreView.setText(currentHighest);
    }

    /**
     * Updates the specific Tile given the new board arrangement.
     */
    void updateTile(PowersPlusTile given, TextView result) {
        if (given == null) {
            setTextViewProperties(result, R.drawable.powersplus_tile_blank, "");
        } else {
            String temp = "" + given.getValue();
            setTextViewProperties(result, R.drawable.powersplus_tile, temp);
        }
    }

    /**
     * Displays a toast message depending on if the user has won the game.
     */
    void makeToastGameOver(boolean won) {
        String message = (won) ? "You win! Congratulations!" : "You lost! Have another go!";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Saves the score of the user into the scoreboard.
     */
    void saveScore() {
        Score curUserScore = new Score(PowersPlusGameActivity.curUser,
                "PowersPlus",
                GameHubActivity.difficulty,
                powersPlusBoardManager.getCurrentScore());
        powersPlusScore.addScore(curUserScore);
        scoreboardFileManager.saveToFile(POWERS_SAVE_FILENAME, this, powersPlusScore);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        boardFileManager.saveToFile(PowersPlusStartingActivity.TEMP_SAVE_FILENAME, this, powersPlusBoardManager);
    }

    /**
     * Displays any updates to the board.
     *
     * @param o:   the object observing.
     * @param arg: any argument.
     */
    @Override
    public void update(Observable o, Object arg) {
        display();
    }

    /**
     * Sets the display properties of each cell in the grid view.
     *
     * @param textView: the desired TextView to change.
     * @param res:      The image to replace.
     * @param text:     The text to replace.
     */
    private void setTextViewProperties(TextView textView, @DrawableRes int res, String text) {
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(res);
        textView.setTextSize(24);
        textView.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        textView.setText(text);
    }

    /**
     * Adds a listener and undo functionality to the UI undo button.
     */
    //Suppressing warnings here because stateManager is generic
    @SuppressWarnings("unchecked")
    private void addUndoListener() {
        final Button undo = findViewById(R.id.buttonUndo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PowersPlusTile> undoneMove = PowersPlusStartingActivity.stateManager.undo();
                if (undoneMove == null) {
                    Toast.makeText(getApplicationContext(), "No more moves left to undo!", Toast.LENGTH_SHORT).show();
                } else {
                    //Undoes the move and displays
                    if (powersPlusBoardManager.isGameOver()) {
                        Toast.makeText(getApplicationContext(), "The Game is over! " +
                                        "Start a new game or view your score on the scoreboard!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        powersPlusBoardManager.getBoard().undo(undoneMove);
                        powersPlusBoardManager.decreaseValues();
                        //powersPlusBoardManager.decreaseHighestValue();
                        display();
                    }
                }
            }
        });
    }
}

