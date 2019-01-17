package fall2018.csc2017.GameCentre.SuperClasses;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import fall2018.csc2017.GameCentre.DataManagers.FileManager;
import fall2018.csc2017.GameCentre.DataManagers.User;
import fall2018.csc2017.GameCentre.DataManagers.UserManager;

public abstract class StartingActivity extends AppCompatActivity {
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";
    /**
     * The current logged in user that is being passed from activity to activity
     */
    protected User curUser;
    /**
     * The user database that we will load from file
     */
    protected UserManager userManager;
    /**
     * Handles the files for the user database
     */
    protected FileManager<UserManager> userFileManager = new FileManager<>();

    protected abstract void addStartButtonListener();

    protected abstract void addLoadButtonListener();

    /**
     * Display that a game was loaded successfully.
     */
    protected void makeToastLoadedText(){
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    };

    protected abstract void switchToGame();
}
