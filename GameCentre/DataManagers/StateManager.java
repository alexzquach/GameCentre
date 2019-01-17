package fall2018.csc2017.GameCentre.DataManagers;

import java.util.ArrayList;

/**
 * A generic StateManager class that manages all state for a particular game.
 */
public class StateManager<T> {
    /**
     * The stack of GameState object.
     */
    private Stack gameStates = new Stack();

    /**
     * Add a game state to a state stack according to its difficulty
     *
     * @param gameState The game state to save.
     */
    public void save(T gameState) {
        this.gameStates.push(gameState);
    }


    /**
     * Undo a game state by returning the previous game state.
     *
     * @return The previous game state, null if the state stack is empty.
     */
    public T undo() {
        if (this.gameStates.isEmpty()) {
            return null;
        }
        return this.gameStates.pop();
    }

    /**
     * Stack class
     */
    private class Stack {
        /**
         * An Arraylist to store objects.
         */
        private ArrayList<T> stackArray;

        /**
         * Initialize the new stack.
         */
        Stack() {
            stackArray = new ArrayList<>();
        }

        /**
         * Add a new object to the stack.
         *
         * @param state the new object to be added.
         */
        void push(T state) {
            stackArray.add(state);
        }

        /**
         * Return the last added object.
         *
         * @return the latest object to be added.
         */
        T pop() {
            T temp;
            temp = stackArray.get
                    (stackArray.size() - 1);
            stackArray.remove(temp);
            return temp;
        }

        /**
         * Check if the stack is empty.
         *
         * @return true if the stack is empty false otherwise.
         */
        boolean isEmpty() {
            return stackArray.isEmpty();
        }
    }
}

