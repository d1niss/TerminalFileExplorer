package pt.ul.fc;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyType;

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

            File startDir = new File(System.getProperty("user.dir"));
            File homeDir = new File(System.getProperty("user.home"));

            FilePanel leftPanel = new FilePanel("Left panel", startDir, KeyType.ArrowRight);
            FilePanel rightPanel = new FilePanel("Right panel", homeDir, KeyType.ArrowLeft);

            leftPanel.setOtherPanel(rightPanel);
            rightPanel.setOtherPanel(leftPanel);

            columnsPanel.addComponent(leftPanel.getPanel());
            columnsPanel.addComponent(rightPanel.getPanel());

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