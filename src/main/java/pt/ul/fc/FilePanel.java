package pt.ul.fc;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.InputFilter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.File;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;;
import java.nio.file.StandardCopyOption;


public class FilePanel {

    private final Panel panel;
    private final ActionListBox listBox;
    private File currentDirectory;

    private FilePanel otherPanel;
    private final KeyType movekey;

    public FilePanel(String title, File startDir, KeyType movekey) {
        this.currentDirectory = startDir;
        this.movekey = movekey;
        this.listBox = new ActionListBox(new TerminalSize(40, 20));

        this.listBox.setInputFilter(new InputFilter() {
            @Override
            public boolean onInput(com.googlecode.lanterna.gui2.Interactable interactable, com.googlecode.lanterna.input.KeyStroke keyStroke) {
                if (keyStroke.getKeyType() == KeyType.Delete || keyStroke.getKeyType() == KeyType.Backspace) {
                    deleteSelectedFile();
                    return false;
                }else if(keyStroke.getKeyType() == movekey){
                    moveSelectedFile();
                    return false;
                }
                return true;
            }
        });
        
        this.panel = new Panel();
        this.panel.addComponent(listBox.withBorder(Borders.singleLine(title)));
        
        updateList();
    }

    public void setOtherPanel(FilePanel otherPanel) {
        this.otherPanel = otherPanel;
    }

    public Panel getPanel() {
        return panel;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    private void deleteSelectedFile() {
        Runnable selectedItem = listBox.getSelectedItem();
        if (selectedItem instanceof FileListItem) {
            FileListItem fileItem = (FileListItem) selectedItem;
            File fileToDelete = fileItem.getFile();
            if (fileToDelete.delete()) {
                updateList();
            }
        }
    }

    private void moveSelectedFile(){
        if (otherPanel == null) {
            return;
        }
        Runnable selectedItem = listBox.getSelectedItem();
        if(selectedItem instanceof FileListItem){
            FileListItem fileItem = (FileListItem) selectedItem;
            File sourceFile = fileItem.getFile();
            File targetDir = otherPanel.getCurrentDirectory();

            if (sourceFile != null && targetDir != null && targetDir.isDirectory()) {
                File targetFile = new File(targetDir, sourceFile.getName());
                try {
                    Files.move(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    this.updateList();
                    otherPanel.updateList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateList() {
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
                listBox.addItem(new FileListItem(displayName, file));
            }
        }
    }

    private void changeDirectory(File newDir) {
        if (newDir != null && newDir.isDirectory()) {
            this.currentDirectory = newDir;
            updateList();
        }
    }

    private class FileListItem implements Runnable {
        private final String displayName;
        private final File file;

        public FileListItem(String displayName, File file) {
            this.displayName = displayName;
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        @Override
        public void run() {
            if (file.isDirectory()) {
                changeDirectory(file);

            }
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}