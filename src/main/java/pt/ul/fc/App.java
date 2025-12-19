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
            // 1. Configurar o Terminal e o Screen
            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            // 2. Criar a GUI baseada em janelas
            WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);

            // 3. Criar a janela principal
            BasicWindow window = new BasicWindow("Terminal File Explorer");
            // Define a janela como Full Screen (ecrã completo)
            window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));

            // 4. Criar o painel principal com layout Horizontal (duas colunas)
            Panel mainPanel = new Panel();
            mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

            // 5. Criar as duas colunas (Usando ActionListBox em vez de ListBox)
            // A ActionListBox precisa de uma Runnable (ação), por isso passamos () -> {} (vazio) por enquanto
            
            // Coluna da Esquerda
            ActionListBox leftList = new ActionListBox(new TerminalSize(40, 20));
            leftList.addItem("Pasta_Esquerda", () -> { /* Ação ao entrar na pasta */ });
            leftList.addItem("ficheiro1.txt", () -> { /* Ação ao abrir ficheiro */ });
            
            // Coluna da Direita
            ActionListBox rightList = new ActionListBox(new TerminalSize(40, 20));
            rightList.addItem("Pasta_Direita", () -> { });
            rightList.addItem("ficheiro2.txt", () -> { });

            // Adicionar as listas ao painel principal com bordas
            mainPanel.addComponent(leftList.withBorder(Borders.singleLine("Esquerda")));
            mainPanel.addComponent(rightList.withBorder(Borders.singleLine("Direita")));

            // Adicionar o painel à janela e iniciar a GUI
            window.setComponent(mainPanel);
            
            // Adicionar botão de sair para fechar a aplicação facilmente durante os testes
            mainPanel.addComponent(new Button("Sair", window::close));

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