package client;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import client.util.ServerIO;

import java.io.IOException;

public class Menu {
    private Terminal terminal;
    private TextGraphics graphics;
    private int selection;
    private ServerIO io;

    public Menu(Terminal terminal, ServerIO io) throws IOException {
        this.terminal = terminal;
        graphics = terminal.newTextGraphics();
        selection = 0;
        this.io = io;
    }

    public void setup() throws IOException {
        graphics.putString(new TerminalPosition(7, 3), "Венгерский кроссворд 9000", SGR.BOLD);
        graphics.putString(new TerminalPosition(9, 7), "Старт", SGR.BOLD);
        graphics.putString(new TerminalPosition(9, 9), "Выход");
        terminal.setCursorVisible(false);
    }

    public void start() throws IOException, ClassNotFoundException {
        setup();
        terminal.flush();
        KeyStroke keyStroke = terminal.readInput();

        while (keyStroke.getKeyType() != KeyType.EOF && keyStroke.getKeyType() != KeyType.Escape) {
            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    graphics.putString(new TerminalPosition(9, 7), "Старт", SGR.BOLD);
                    graphics.putString(new TerminalPosition(9, 9), "Выход");
                    selection = 0;
                    break;
                case ArrowDown:
                    graphics.putString(new TerminalPosition(9, 7), "Старт");
                    graphics.putString(new TerminalPosition(9, 9), "Выход", SGR.BOLD);
                    selection = 1;
                    break;
                case Enter:
                    switch (selection) {
                        case 0:
                            ChooseLevel chooseLevel = new ChooseLevel(terminal, io);
                            chooseLevel.start();
//                            Game game = new Game(terminal, new Level(String.join("",
//                                    Collections.nCopies(256, "Q")), 4));
//                            game.startLevel();
                            terminal.clearScreen();
                            setup();
                            break;
                        case 1:
                            terminal.close();
                            break;
                    }
            }
            terminal.flush();
            keyStroke = terminal.readInput();
        }
        terminal.close();
    }
}
