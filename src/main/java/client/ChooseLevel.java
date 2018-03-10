package client;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import client.util.ServerIO;

import java.io.IOException;

public class ChooseLevel {
    private Terminal terminal;
    private TextGraphics graphics;
    private int selection;
    private ServerIO io;

    public ChooseLevel(Terminal terminal, ServerIO io) throws IOException {
        this.terminal = terminal;
        graphics = terminal.newTextGraphics();
        selection = 0;
        this.io = io;
    }

    public int getLevelCount() {
        return io.getLevelCount();
    }

    public void redraw() throws IOException {
        terminal.clearScreen();
        graphics.putString(new TerminalPosition(7, 3), "Выбор уровня", SGR.BOLD);
        for (int i = 0; i < getLevelCount(); i++)
            graphics.putString(new TerminalPosition(9, 7 + i * 2), String.valueOf(i));
        graphics.putString(new TerminalPosition(9, 7 + selection * 2),
                String.valueOf(selection), SGR.BOLD);
        terminal.setCursorVisible(false);
    }

    public void start() throws IOException {
        redraw();
        terminal.flush();
        KeyStroke keyStroke = terminal.readInput();

        while (keyStroke.getKeyType() != KeyType.EOF && keyStroke.getKeyType() != KeyType.Escape) {
            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    if (selection > 0)
                        selection--;
                    redraw();
                    break;
                case ArrowDown:
                    if (selection < getLevelCount() - 1)
                        selection++;
                    redraw();
                    break;
                case Enter:
                    Game game = new Game(terminal, selection, io);
                    game.startLevel();
                    terminal.clearScreen();
                    redraw();
            }
            terminal.flush();
            keyStroke = terminal.readInput();
        }
    }
}
