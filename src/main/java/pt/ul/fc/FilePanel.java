package pt.ul.fc;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Panel;

import java.io.File;
import java.util.Arrays;

public class FilePanel {

    private final Panel panel;
    private final ActionListBox listBox;
    private File currentDirectory;

    public FilePanel(String title, File startDir) {
        this.currentDirectory = startDir;
        this.listBox = new ActionListBox(new TerminalSize(40, 20));
        
        this.panel = new Panel();
        this.panel.addComponent(listBox.withBorder(Borders.singleLine(title)));
        
        updateList();
    }

    public Panel getPanel() {
        return panel;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    private void updateList() {
        listBox.clearItems();

        if (currentDirectory == null || !currentDirectory.isDirectory()) {
            return;
        }

        if (currentDirectory.getParentFile() != null) {
            listBox.addItem("..", () -> {
                changeDirectory(currentDirectory.getParentFile());
            });
        }

        File[] files = currentDirectory.listFiles();
        if (files != null) {
            Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) return -1;
                if (!f1.isDirectory() && f2.isDirectory()) return 1;
                return f1.getName().compareToIgnoreCase(f2.getName());
            });

            for (File file : files) {
                String displayName = file.getName() + (file.isDirectory() ? "/" : "");

                listBox.addItem(displayName, () -> {
                    if (file.isDirectory()) {
                        changeDirectory(file);
                    } else {
                    }
                });
            }
        }
    }

    private void changeDirectory(File newDir) {
        if (newDir != null && newDir.isDirectory()) {
            this.currentDirectory = newDir;
            updateList();
        }
    }
}