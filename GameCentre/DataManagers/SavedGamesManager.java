package fall2018.csc2017.GameCentre.DataManagers;

import java.io.Serializable;

/**
 * Generic class for saved games
 */
public class SavedGamesManager<T> implements Serializable {

    /**
     * The easy difficulty save
     */
    private T easySave;

    /**
     * The medium difficulty save
     */
    private T mediumSave;

    /**
     * The hard difficulty save
     */
    private T hardSave;

    /**
     * Gets the save depending on the difficulty chosen
     *
     * @param diff The difficulty chosen
     * @return Returns the appropriate save
     */
    public T getSave(String diff){
        switch (diff){
            case "Easy":
                return easySave;
            case "Medium":
                return mediumSave;
            default:
                return hardSave;
        }
    }

    /**
     * Sets the save depending on the difficulty chosen
     *
     * @param diff The difficulty chosen
     * @param save Sets the appropriate save
     */
    public void setSave(String diff, T save){
        switch (diff){
            case "Easy":
                this.easySave = save;
                break;
            case "Medium":
                this.mediumSave = save;
                break;
            case "Hard":
                this.hardSave = save;
                break;
        }
    }
}
