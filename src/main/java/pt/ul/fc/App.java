package pt.ul.fc;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        Terminal terminal = null;
        Screen screen = null;

        try {
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);

            BasicWindow window = new BasicWindow("Terminal File Explorer");
            window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));

            Panel mainPanel = new Panel();
            mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            Panel columnsPanel = new Panel();
            columnsPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

            FileBrowser fileBrowser = new FileBrowser();
            File currentDir = new File(System.getProperty("user.dir"));


            ActionListBox leftList = new ActionListBox(new TerminalSize(40, 20));
            fileBrowser.updateList(leftList, currentDir);
            
            ActionListBox rightList = new ActionListBox(new TerminalSize(40, 20));
            fileBrowser.updateList(rightList, currentDir);

            columnsPanel.addComponent(leftList.withBorder(Borders.singleLine("Left Pane")));
            columnsPanel.addComponent(rightList.withBorder(Borders.singleLine("Right Pane")));

            Panel buttonPanel = new Panel();
            buttonPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            buttonPanel.addComponent(new Button("Exit", window::close));

            mainPanel.addComponent(columnsPanel);
            mainPanel.addComponent(buttonPanel);

            window.setComponent(mainPanel);

            gui.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}