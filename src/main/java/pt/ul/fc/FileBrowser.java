package pt.ul.fc;

import com.googlecode.lanterna.gui2.ActionListBox;
import java.io.File;
import java.util.Arrays;

public class FileBrowser {

    /**
     * Updates the ActionListBox with the files from the provided directory.
     * @param listBox The GUI list to update
     * @param directory The folder on the computer to read
     */
    public void updateList(ActionListBox listBox, File directory) {
        // Clear old items from the visual list
        listBox.clearItems();

        // Check if the folder exists and is valid
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        // Add ".." option to go back (if not at the root)
        if (directory.getParentFile() != null) {
            listBox.addItem("..", () -> {
                // Back action will be implemented later
            });
        }

        // Read files from disk
        File[] files = directory.listFiles();

        if (files != null) {
            // Sort by name to make it easier to find
            Arrays.sort(files);

            for (File file : files) {
                // Prepare the name (add / if it's a folder for visual distinction)
                String displayName = file.getName() + (file.isDirectory() ? "/" : "");

                listBox.addItem(displayName, () -> {
                    // Action when pressing Enter on the file will be implemented later
                });
            }
        }
    }
}