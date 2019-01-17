package fall2018.csc2017.GameCentre.UserInterfaceElements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.DataManagers.Score;
import fall2018.csc2017.GameCentre.DataManagers.ScoreboardManager;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.DataManagers.User;
import fall2018.csc2017.GameCentre.DataManagers.UserManager;

import static fall2018.csc2017.GameCentre.MineSweeper.MineSweeperGameActivity.minesweeperScores;


/**
 * The initial activity for the game centre.
 */
public class LaunchCentre extends AppCompatActivity {

    /**
     * The list of games implemented so far
     */
    private final String gamesList[] = {"SlidingTiles", "MineSweeper", "PowersPlus"};

    /**
     * The main save for users.
     */
    public static final String USER_SAVE_FILENAME = "user_file.ser";

    /**
     * The user database
     */
    private UserManager userManager = new UserManager();

    /**
     * Handles the file operations for the user database
     */
    private FileManager<UserManager> userFileManager = new FileManager<>();

    //Testing
//    private FileManager<ScoreboardManager> scoreboardFileManager = new FileManager<>();
//    /**
//     * The scoreboard manager for sliding tiles
//     */
//    public static ScoreboardManager minesweeperScores = new ScoreboardManager(false);
//    /**
//     * The scoreboard manager for sliding tiles
//     */
//    public static ScoreboardManager slidingTilesScores = new ScoreboardManager(false);
//    /**
//     * The scoreboard manager for powers plus.
//     */
//    public static ScoreboardManager powersPlusScore = new ScoreboardManager(true);


    /**
     * The onCreate for this activity
     *
     * @param savedInstanceState Represents the current activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks to see if the user database file is empty
        UserManager potentialNewManager;
        potentialNewManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, this);
        if (potentialNewManager == null) {
            //This creates a new file since one did not exist yet
            potentialNewManager = new UserManager();
            userFileManager.saveToFile(USER_SAVE_FILENAME, this, potentialNewManager);
        }


        //Testing
//        //Loads the scores database for sliding tiles
//        minesweeperScores = scoreboardFileManager.loadFromFile(LeaderboardActivity.MINESWEEPER_SAVE_FILENAME, this);
//        slidingTilesScores = scoreboardFileManager.loadFromFile(LeaderboardActivity.SLIDING_SAVE_FILENAME, this);
//        powersPlusScore = scoreboardFileManager.loadFromFile(LeaderboardActivity.POWERS_SAVE_FILENAME, this);
//
//
//        User tanjimmy = new User("tanjimmy", "123");
//        User quachal3 = new User("quachal3", "123");
//        User wasimzuh = new User ("wasimzuh", "123");
//        User yaofred = new User("yaofred", "123");
//        Score curUserScore10 = new Score(wasimzuh,
//                "PowersPlus",
//                "Easy",
//                64);
//        Score curUserScore = new Score(tanjimmy,
//                "PowersPlus",
//                "Medium",
//                128);
//        Score curUserScore2 = new Score(tanjimmy,
//                "PowersPlus",
//                "Hard",
//                256);
//        Score curUserScore3 = new Score(tanjimmy,
//                "PowersPlus",
//                "Easy",
//                400);
//        Score curUserScore4 = new Score(tanjimmy,
//                "PowersPlus",
//                "Medium",
//                90);
//        Score curUserScore5 = new Score(quachal3,
//                "PowersPlus",
//                "Hard",
//                314);
//        Score curUserScore6 = new Score(wasimzuh,
//                "PowersPlus",
//                "Medium",
//                6900);
//        Score curUserScore7 = new Score(tanjimmy,
//                "PowersPlus",
//                "Medium",
//                3200);
//        Score curUserScore8 = new Score(wasimzuh,
//                "PowersPlus",
//                "Hard",
//                700);
//        Score curUserScore9 = new Score(yaofred,
//                "MineSweeper",
//                "Easy",
//                598);
//        Score curUserScore11 = new Score(yaofred,
//                "PowersPlus",
//                "Medium",
//                420);
//        Score curUserScore12 = new Score(yaofred,
//                "PowersPlus",
//                "Hard",
//                10000);
//        powersPlusScore.addScore(curUserScore);
//        powersPlusScore.addScore(curUserScore2);
//        powersPlusScore.addScore(curUserScore3);
//        powersPlusScore.addScore(curUserScore4);
//        powersPlusScore.addScore(curUserScore5);
//        powersPlusScore.addScore(curUserScore6);
//        powersPlusScore.addScore(curUserScore7);
//        powersPlusScore.addScore(curUserScore8);
//        powersPlusScore.addScore(curUserScore9);
//        powersPlusScore.addScore(curUserScore10);
//        powersPlusScore.addScore(curUserScore11);
//        powersPlusScore.addScore(curUserScore12);
////        curUserScore9 = new Score(quachal3,
////                "MineSweeper",
////                "Easy",
////                2);
////        curUserScore11 = new Score(wasimzuh,
////                "MineSweeper",
////                "Medium",
////                8);
////        curUserScore12 = new Score(yaofred,
////                "MineSweeper",
////                "Hard",
////                17);
////        minesweeperScores.addScore(curUserScore9);
////        minesweeperScores.addScore(curUserScore11);
////        minesweeperScores.addScore(curUserScore12);
//        scoreboardFileManager.saveToFile(LeaderboardActivity.POWERS_SAVE_FILENAME, this, powersPlusScore);


        //Change the view and create the buttons
        setContentView(R.layout.activity_gamelauncher);
        addSignUpButtonListener();
        addLoginButtonListener();
    }

    /**
     * Activate the Sign up button.
     */
    private void addSignUpButtonListener() {
        Button signUpButton = findViewById(R.id.SignUpButton);
        //This handles the click of the button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the two text fields and get the username and password from them
                EditText passwordText = findViewById(R.id.passwordText);
                EditText signUpText = findViewById(R.id.userNameText);
                String password = passwordText.getText().toString();
                String username = signUpText.getText().toString();

                //Create the potential new user
                User potentialUser = new User(username, password);

                //Load the database from file
                Activity host = (Activity) v.getContext();
                userManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, host);

                //Check if this user has already signed up
                if (!userManager.hasUser(potentialUser) && !username.isEmpty() && !password.isEmpty()) {
                    //Create a new saved games file for this new user
                    potentialUser.createSavedGames();
                    userManager.addUser(potentialUser);
                    switchToGameSelector(potentialUser);
                } else {
                    //Display errors
                    if (username.isEmpty()){
                        //Blank username
                        makeToastBlankUsername();
                    }else if (password.isEmpty()){
                        //Blank password
                        makeToastBlankPassword();
                    }else{
                        //Displays an error when signing up if the account already exists
                        String message = "This user already exists!";
                        makeToastSignUpErrorText(message);
                    }
                }

                //Save the updated database to file
                userFileManager.saveToFile(USER_SAVE_FILENAME, host, userManager);
            }
        });
    }

    /**
     * Activate the login button.
     */
    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.LogInButton);
        //This handles the click of the button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the two text fields and get the username and password from them
                EditText passwordText = findViewById(R.id.passwordText);
                EditText signUpText = findViewById(R.id.userNameText);
                String username = signUpText.getText().toString();
                String password = passwordText.getText().toString();

                //Create the potential logged in user
                User potentialLogin = new User(username, password);

                //Load the database
                Activity host = (Activity) v.getContext();
                userManager = userFileManager.loadFromFile(USER_SAVE_FILENAME, host);

                if (userManager.hasUser(potentialLogin) && !username.isEmpty() && !password.isEmpty()) {
                    //Compare if passwords are correct
                    if (comparePasswords(potentialLogin)) {
                        //Switches to the game selector if the password entered is correct
                        //The user already exists so no need to create a new saved games file
                        User curUser = userManager.getUser(potentialLogin);

                        //Adds new games to the user if they signed up before those new games were added
                        fixOldUser(curUser);

                        switchToGameSelector(curUser);
                    } else {
                        String message = "Your password is incorrect!";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Display errors
                    if (username.isEmpty()){
                        //Blank username
                        makeToastBlankUsername();
                    }else if (password.isEmpty()){
                        //Blank password
                        makeToastBlankPassword();
                    }else{
                        //Displays an error when signing up and the account already exists
                        String message = "This user does not exist!";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Adds new games to an old user
     *
     * @param curUser The current logged in user
     */
    private void fixOldUser(User curUser) {
        //Checks to see what games are missing if new games are added
        if (curUser.userSavedGames.size() < gamesList.length){
            for (String gameType : gamesList){
                if (!curUser.userSavedGames.containsKey(gameType)){
                    curUser.addSave(gameType);
                }
            }
        }
    }


    /**
     * Compares the password of the current field and the password stored in the database
     * @param user the current user trying to log in
     * @return True or False, depending on if the password matches or not
     */
    private boolean comparePasswords(User user) {
        for (User u : userManager) {
            if (u.getUsername().equals(user.getUsername())) {
                return u.getPassword().equals(user.getPassword());
            }
        }
        return false;
    }

    /**
     * Display an sign up error
     */
    private void makeToastSignUpErrorText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a blank username message
     */
    private void makeToastBlankUsername() {
        Toast.makeText(this, "Please enter a username!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a blank password message
     */
    private void makeToastBlankPassword() {
        Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Switch to the Game selector view to select what game to play.
     */
    private void switchToGameSelector(User currentUser) {
        Intent tmp = new Intent(this, GameHubActivity.class);

        //Store the current user in the next activity
        tmp.putExtra("CurUser", currentUser);

        startActivity(tmp);
    }
}
