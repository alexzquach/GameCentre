package fall2018.csc2017.GameCentre.DataManagers;

import android.app.Activity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * A File Manager class that makes the handling of files for our application as generic as possible
 */
public class FileManager<T> {

    /**
     * Load the generic T from fileName.
     *
     * @param fileName the name of the file
     */
    //We suppress unchecked assignment warnings here because we are loading a generic object that
    //we need to type case from file and android studio doesn't like that for some reason
    @SuppressWarnings("unchecked")
    public T loadFromFile(String fileName, Activity currentActivity) {
        T thingToLoad;
        try {
            InputStream inputStream = currentActivity.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                //This is where the "Unchecked Assignment" warning is being suppressed"
                thingToLoad = (T) input.readObject();
                inputStream.close();
                return thingToLoad;
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        //Returns null if the file is empty
        return null;
    }

    /**
     * Save the generic T to fileName.
     *
     * @param fileName the name of the file
     */
    //We suppress this warning because this is a general method so we don't know what
    //activity we are passing in so we cannot use the actual class when
    //calling .MODE_PRIVATE
    @SuppressWarnings("static-access")
    public void saveToFile(String fileName, Activity currentActivity, T thingToSave) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    //The warning we are suppressing occurs here
                    currentActivity.openFileOutput(fileName, currentActivity.MODE_PRIVATE));
            outputStream.writeObject(thingToSave);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
