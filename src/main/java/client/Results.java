package client;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Results {

    private Terminal terminal;
    private TextGraphics graphics;
    private int level;

    public Results(Terminal terminal, int level) throws IOException {
        this.terminal = terminal;
        graphics = terminal.newTextGraphics();
        this.level = level;
    }

    public void show() throws IOException {
        graphics.putString(5, 3, "Результаты для уровня " + level);
        terminal.flush();
        KeyStroke keyStroke = terminal.readInput();

        while (keyStroke.getKeyType() != KeyType.EOF && keyStroke.getKeyType() != KeyType.Escape) {
            keyStroke = terminal.readInput();
        }
    }

}

