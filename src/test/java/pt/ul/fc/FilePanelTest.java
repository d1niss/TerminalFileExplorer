package pt.ul.fc;

import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Container;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilePanelTest {

    @TempDir
    Path tempDir;

    private FilePanel filePanel;
    private File testDir;

    @BeforeEach
    void setUp() {
        testDir = tempDir.toFile();
        filePanel = new FilePanel("Test Panel", testDir);
    }

    @Test
    void testInitializationListsFiles() throws IOException {
        // Create dummy files
        createFile("file1.txt");
        createFile("file2.txt");
        createDir("subdir");

        // Refresh the panel (normally done by constructor, but we added files after)
        // Since updateList is private, we recreate the panel or trigger an update if possible.
        // In your current code, updateList is private and only called in constructor or delete/nav.
        // So we should recreate the panel to pick up the new files.
        filePanel = new FilePanel("Test Panel", testDir);

        ActionListBox listBox = extractListBox(filePanel);
        List<String> items = getListBoxItemLabels(listBox);

        // Verify items (sorted: directories first, then files)
        assertTrue(items.contains("subdir/"));
        assertTrue(items.contains("file1.txt"));
        assertTrue(items.contains("file2.txt"));
    }

    @Test
    void testNavigationIntoDirectory() throws IOException {
        // Create a subdirectory
        File subDir = createDir("subdir");
        createFile("subdir/inner.txt");

        // Re-init to load files
        filePanel = new FilePanel("Test Panel", testDir);
        ActionListBox listBox = extractListBox(filePanel);

        // Find the Runnable item for the subdirectory and run it (simulates hitting Enter)
        runItemWithLabel(listBox, "subdir/");

        // Assert that the Current Directory of the panel has changed
        assertEquals(subDir.getAbsolutePath(), filePanel.getCurrentDirectory().getAbsolutePath());

        // Assert that the list now shows the content of the subdirectory
        List<String> items = getListBoxItemLabels(listBox);
        assertTrue(items.contains("..")); // Should have parent link
        assertTrue(items.contains("inner.txt"));
    }

    @Test
    void testNavigationUpDirectory() throws IOException {
        // Create a subdirectory
        File subDir = createDir("subdir");

        // Initialize panel INSIDE the subdirectory
        filePanel = new FilePanel("Test Panel", subDir);
        ActionListBox listBox = extractListBox(filePanel);

        // Find the ".." item and run it
        runItemWithLabel(listBox, "..");

        // Assert we are back at the root temp dir
        assertEquals(testDir.getAbsolutePath(), filePanel.getCurrentDirectory().getAbsolutePath());
    }

    @Test
    void testDeleteFile() throws IOException {
        File fileToDelete = createFile("deleteMe.txt");
        
        // Re-init to pick up file
        filePanel = new FilePanel("Test Panel", testDir);
        ActionListBox listBox = extractListBox(filePanel);

        // Select the file
        selectItemWithLabel(listBox, "deleteMe.txt");

        // Manually trigger the input filter with a DELETE key stroke
        // We have to access the input filter attached to the list box
        // Your code: listBox.setInputFilter(...)
        // Since we can't easily get the filter, we can rely on the fact that ActionListBox handles keys.
        // However, your specific delete logic is in a custom InputFilter.
        // We can simulate the input on the ListBox itself if it delegates, 
        // OR we just grab the filter if Lanterna exposed it. 
        // Lanterna 3.1.1 Component doesn't expose getInputFilter easily, 
        // but we can pass the keystroke to handleInput if the component supports it.
        // Your code uses setInputFilter, which intercepts calls.
        
        // We will assume standard event processing:
        // Ideally we'd call `listBox.handleInput(new KeyStroke(KeyType.Delete))` but that returns boolean.
        // A robust way given the private nature of the filter is to check if we can fire it.
        // Actually, Lanterna's `AbstractInteractableComponent` has `handleInput` which calls the filter.
        
        listBox.handleInput(new KeyStroke(KeyType.Delete));

        // Check if file is deleted from filesystem
        assertFalse(fileToDelete.exists(), "File should have been deleted from disk");

        // Check if file is removed from GUI list
        List<String> items = getListBoxItemLabels(listBox);
        assertFalse(items.contains("deleteMe.txt"), "File should be removed from the list");
    }

    // --- Helper Methods ---

    private File createFile(String name) throws IOException {
        File f = new File(testDir, name);
        f.createNewFile();
        return f;
    }

    private File createDir(String name) {
        File f = new File(testDir, name);
        f.mkdir();
        return f;
    }

    /**
     * Helper to dig into the Panel -> Border -> ActionListBox structure
     */
    private ActionListBox extractListBox(FilePanel filePanel) {
        // filePanel.getPanel() returns the outer Panel
        // Outer Panel contains a Border (from withBorder)
        // Border contains the ActionListBox
        
        Component border = filePanel.getPanel().getChildren().iterator().next();
        if (border instanceof Container) {
             Component listBox = ((Container) border).getChildren().iterator().next();
             if (listBox instanceof ActionListBox) {
                 return (ActionListBox) listBox;
             }
        }
        fail("Could not find ActionListBox inside FilePanel structure");
        return null;
    }

    private List<String> getListBoxItemLabels(ActionListBox listBox) {
        return listBox.getItems().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private void runItemWithLabel(ActionListBox listBox, String label) {
        for (Runnable item : listBox.getItems()) {
            if (item.toString().equals(label)) {
                item.run();
                return;
            }
        }
        fail("Item with label " + label + " not found");
    }
    
    private void selectItemWithLabel(ActionListBox listBox, String label) {
        for (int i = 0; i < listBox.getItemCount(); i++) {
            if (listBox.getItemAt(i).toString().equals(label)) {
                listBox.setSelectedIndex(i);
                return;
            }
        }
        fail("Item with label " + label + " not found");
    }
}