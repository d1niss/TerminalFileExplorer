package pt.ul.fc;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
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


            ActionListBox leftList = new ActionListBox(new TerminalSize(40, 20));
            leftList.addItem("Pasta_Esquerda", () -> {});
            leftList.addItem("ficheiro1.txt", () -> {});
            
            ActionListBox rightList = new ActionListBox(new TerminalSize(40, 20));
            rightList.addItem("Pasta_Direita", () -> {});
            rightList.addItem("ficheiro2.txt", () -> {});

            columnsPanel.addComponent(leftList.withBorder(Borders.singleLine("Esquerda")));
            columnsPanel.addComponent(rightList.withBorder(Borders.singleLine("Direita")));

            Panel buttonPanel = new Panel();
            buttonPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            buttonPanel.addComponent(new Button("Sair", window::close));

            mainPanel.addComponent(columnsPanel);
            mainPanel.addComponent(buttonPanel);

            window.setComponent(mainPanel);

            gui.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Garantir que o ecrã fecha corretamente ao sair
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