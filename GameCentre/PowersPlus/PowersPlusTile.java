package fall2018.csc2017.GameCentre.PowersPlus;

/*
This code is an adaptation and elements (such as algorithm and
implementation) which are inspired from the code at:
https://rosettacode.org/wiki/2048#Java

I do not consider this my complete own creation, rather my adaptation
of the original. All rights to the original coder and RosettaCode.
 */

import java.io.Serializable;

/**
 * The tile to be used by the PowersPlus game.  Does not extend Tile because this is a
 * swiping game.
 */
public class PowersPlusTile implements Serializable {

    /**
     * Represents if this tile has been merged or not during the current move.
     */
    private boolean isMerged;

    /**
     * The value that the tile represents
     */
    private int value;

    /**
     * The designated power this tile's value will multiply by.
     */
    private int power;

    /**
     * Returns the power associated with this Tile.
     *
     * @return int: the desired power of the Tile.
     */
    public int getPower() {
        return power;
    }

    /**
     * A PowersPlusTile that has a value and power.
     *
     * @param val:   the value of the Tile
     * @param power: the power to multiply by
     */
    public PowersPlusTile(int val, int power) {
        value = val;
        this.power = power;
    }

    /**
     * Gets the current value of the Tile.
     *
     * @return int: The value of the Tile.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets whether or not the tile has been merged on current move.
     *
     * @param merged: Whether this Tile has been merged or not.
     */
    public void setIsMerged(boolean merged) {
        this.isMerged = merged;
    }

    /**
     * Returns whether the tile has been merged.
     *
     * @return boolean: Whether the tile has been merged.
     */
    public boolean isMerged() {
        return isMerged;
    }

    /**
     * Returns if two tiles are able to merge together. That is, iff they have
     * equal values, both have not been merged yet, and other is not a null object.
     *
     * @param other: The other tile to compare to.
     * @return boolean: Whether other and this Tile can merge.
     */
    public boolean canMerge(PowersPlusTile other) {
        return (other != null) &&
                (!this.isMerged && !other.isMerged) &&
                (this.value == other.getValue());
    }

    /**
     * Merges two tiles together by consuming the other tile and multiplying their
     * values together.
     *
     * @param other: The other tile to merge.
     * @return int: The new value merging will have, -1 if the merge did not occur.
     */
    public int merge(PowersPlusTile other) {
        if (other != null && canMerge(other)) {
            value = value * power; // Get to next value
            isMerged = true;
            return value;
        } else {
            return -1; // Value does not exist for null objects
        }
    }

    /**
     * Compares two PowersPlusTiles for equality.
     *
     * @param o: The other object to compare (Tile or not).
     * @return boolean: Whether the two objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowersPlusTile that = (PowersPlusTile) o;
        return isMerged == that.isMerged &&
                value == that.value &&
                power == that.power;
    }
}
