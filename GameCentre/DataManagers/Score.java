package fall2018.csc2017.GameCentre.DataManagers;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Score class with comparable score and some other properties
 */
public class Score implements Comparable<Score>, Serializable {

    /**
     * The user who owns the score.
     */
    private User user;

    /**
     * The game type of this score (a string).
     */
    private String gameType;

    /**
     * The difficulty of this score (also a string).
     */
    private String difficulty;

    /**
     * The initial value of the score, we set it to be zero here.
     */
    private int value;

    /**
     * The constructor which takes the following parameter and initialize the value
     *
     * @param user       the user owns the score
     * @param gameType   the type of the game
     * @param difficulty the difficulty of the game
     * @param value      the integer value of the score
     */
    public Score(User user, String gameType, String difficulty, int value) {
        this.user = user;
        this.gameType = gameType;
        this.difficulty = difficulty;
        this.value = value;
    }

    /**
     * Gets the user
     *
     * @return Returns the user object
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the game type
     *
     * @return Returns the game type
     */
    private String getGameType() {
        return gameType;
    }

    /**
     * Get the difficulty
     *
     * @return Returns the game difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Get the score value
     *
     * @return Returns the score value
     */
    public int getValue() {
        return value;
    }

    /**
     * The equals method, overrides
     *
     * @param o the object we're comparing to
     * @return returns true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;
        Score score = (Score) o;
        return getValue() == score.getValue() &&
                Objects.equals(getUser(), score.getUser()) &&
                Objects.equals(getGameType(), score.getGameType()) &&
                Objects.equals(getDifficulty(), score.getDifficulty());
    }

    /**
     * Enables the .sort method for lists
     *
     * @param o the object we're comparing to
     * @return returns an int based on the comparison
     */
    @Override
    public int compareTo(@NonNull Score o) {
        return Integer.compare(this.value, o.value);
    }

    /**
     * A string representation of score
     *
     * @return Returns the string representation for score
     */
    @Override
    public String toString() {
        return "Score{" +
                "value=" + value +
                '}';
    }
}
