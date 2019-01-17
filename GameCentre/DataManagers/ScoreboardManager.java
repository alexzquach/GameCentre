package fall2018.csc2017.GameCentre.DataManagers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * A generic ScoreboardManager that manages all the scores of a particular gameType.
 */
public class ScoreboardManager implements Serializable {

    /**
     * The list of Score object according to their difficulty.
     */
    private List<Score> easyScores = new ArrayList<>();
    private List<Score> mediumScores = new ArrayList<>();
    private List<Score> hardScores = new ArrayList<>();

    /**
     * Determines if the scores are to be sorted from high to low or not.
     */
    private boolean isHighToLow;

    public ScoreboardManager(boolean isHighToLow) {
        this.isHighToLow = isHighToLow;
    }

    /**
     * Return the list contains all score(Score object) with easy difficulty for this game
     *
     * @return easyScores list contains all score with easy difficulty for all users
     */
    public List<Score> getEasyScores() {
        return easyScores;
    }

    /**
     * Return the list contains all score(Score object) with medium difficulty for this game
     *
     * @return easyScores list contains all score with medium difficulty for all users
     */
    public List<Score> getMediumScores() {
        return mediumScores;
    }

    /**
     * Return the list contains all score(Score object) with hard difficulty for this game
     *
     * @return easyScores list contains all score with hard difficulty for all users
     */
    public List<Score> getHardScores() {
        return hardScores;
    }


    /**
     * Return the SHOWLIMIT for how many score will be shown in the Leaderboard.
     *
     * @return the SHOWLIMIT.
     */
    public static int getSHOWLIMIT() {
        return SHOWLIMIT;
    }

    /**
     * The number of top scores to display.
     */
    private static final int SHOWLIMIT = 10;

    /**
     * Add score to a score list based off of it's difficulty.
     *
     * @param score the score to add to respective list.
     */
    public void addScore(Score score) {
        //Adds the score depending on difficulty
        switch (score.getDifficulty()) {
            case "Easy":
                easyScores.add(score);
                break;
            case "Medium":
                mediumScores.add(score);
                break;
            default:
                hardScores.add(score);
                break;
        }
    }

    /**
     * Sort a given list in the order from highest to lowest
     *
     * @param lst the list to sort
     */
    private List<Score> sortScoresFromHighToLow(List<Score> lst) {
        //Sort the list, then reverse it
        Collections.sort(lst);
        List<Score> tempLst = new ArrayList<>();
        for (int i = lst.size() - 1; i >= 0; i--) {
            tempLst.add(lst.get(i));
        }
        lst = tempLst;
        return lst;
    }

    /**
     * Return the list of the top scores within SHOWLIMIT given a difficulty and sorting order.
     *
     * @param difficulty  The difficulty as a string.
     * @return The sorted list.
     */
    public List<Score> getTopScoresByDifficulty(String difficulty) {
        //Get the list of scores depending on difficulty
        List<Score> lst;
        switch (difficulty) {
            case "Easy":
                lst = easyScores;
                break;
            case "Medium":
                lst = mediumScores;
                break;
            default:
                lst = hardScores;
                break;
        }
        //Does the appropriate sorting depending on game
        if (isHighToLow) {
            lst = sortScoresFromHighToLow(lst);
        } else {
            Collections.sort(lst);
        }
        if (lst.size() < SHOWLIMIT) {
            return lst;
        }
        return lst.subList(0, SHOWLIMIT);
    }

    /**
     * Return the list of all scores given a user, and the difficulty from lowest to highest.
     *
     * @param lst  the difficulty list to access.
     * @param user the owner of the scores.
     * @return the top scores of the user from lowest to highest.
     */
    private List<Score> getUserScoresByScoreListLowToHigh(List<Score> lst, User user) {
        //Populate the list
        ArrayList<Score> return_list = new ArrayList<>();
        for (Score score : lst) {
            if (score.getUser().equals(user)) {
                return_list.add(score);
            }
        }
        //Sort the list
        Collections.sort(return_list);
        if (return_list.size() > SHOWLIMIT) {
            //Return the appropriate slice
            return return_list.subList(0, SHOWLIMIT);
        }
        return return_list;
    }

    /**
     * Return the list of all scores given a user and a difficulty from highest to lowest.
     *
     * @param lst  the list to sort.
     * @param user the owner of the scores.
     * @return the top scores of the user from highest to lowest.
     */
    private List<Score> getUserScoresByScoreListHighToLow(List<Score> lst, User user) {
        lst = getUserScoresByScoreListLowToHigh(lst, user);
        return sortScoresFromHighToLow(lst);
    }

    /**
     * Return the list of the top scores given a user, and difficulty from lowest to highest.
     *
     * @param user        the user to get the top scores of.
     * @param difficulty  the difficulty of the scores.
     * @return the score list from lowest to highest.
     */
    public List<Score> getTopScoresByUser(User user, String difficulty) {
        List<Score> lst;
        //Get the appropriate scores depending on difficulty
        switch (difficulty) {
            case "Easy":
                lst = easyScores;
                break;
            case "Medium":
                lst = mediumScores;
                break;
            default:
                lst = hardScores;
                break;
        }
        //Returns the specific user's scores
        if (isHighToLow) {
            return getUserScoresByScoreListHighToLow(lst, user);
        }
        return getUserScoresByScoreListLowToHigh(lst, user);
    }
}

