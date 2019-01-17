package fall2018.csc2017.GameCentre.DataManagers;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Contains all the users that have signed up for our game hub
 */
public class UserManager implements Serializable, Iterable<User> {

    /**
     * A list contains all the user.
     */
    private List<User> users = new ArrayList<>();

    /**
     * Return the total number of users.
     *
     * @return the total number of users.
     */
    public int getTotalUsers() {
        return totalUsers;
    }

    /**
     * The total number  of users.
     */
    private int totalUsers;

    /**
     * Add one user to the user list.
     *
     * @param user the user to be added.
     */
    public void addUser(User user) {
        //Adds a user if they are not yet in the database
        if (!hasUser(user)) {
            users.add(user);
            this.totalUsers += 1;
        }
    }

    /**
     * Return if the user list has such a user.
     *
     * @param newUser the user want to check
     * @return true if user is in the list false otherwise.
     */
    public boolean hasUser(User newUser) {
        return users.contains(newUser);
    }

    /**
     * Return the login user object.
     *
     * @param loginUser the user that is logging in.
     * @return user object if there exist such a user, null otherwise
     */
    public User getUser(User loginUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(loginUser.getUsername())) {
                return users.get(i);
            }
        }
        return null;
    }

    /**
     * Replace the user with curUser.
     *
     * @param curUser the user to replace.
     */
    public void replaceUser(User curUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(curUser.getUsername())) {
                users.set(i, curUser);
            }
        }
    }

    /**
     * An iterator to check passwords
     *
     * @return returns a user
     */
    @NonNull
    @Override
    public Iterator<User> iterator() {
        return new UserIterator();
    }

    /**
     * The actual iterator class
     */
    public class UserIterator implements Iterator<User> {
        private int location = 0;

        /**
         * Returns the next user
         *
         * @return Returns the next user
         */
        @Override
        public User next() {
            if (!hasNext()) {
                throw new NoSuchElementException("End of database");
            }
            //Remember the result
            User cur_user = users.get(location);
            //Get ready for the next call to next
            location += 1;
            //Return what we remembered
            return cur_user;
        }

        /**
         * Checks to see if there is a next element
         *
         * @return Returns true if there is a next element
         */
        @Override
        public boolean hasNext() {
            return location < totalUsers;
        }
    }
}
