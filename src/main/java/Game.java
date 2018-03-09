import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import models.Level;

import java.io.IOException;

public class Game {

    private Terminal terminal;

    public Game(Terminal terminal) {
        this.terminal = terminal;
    }

    public void startLevel(Level level) throws IOException {
        KeyStroke keyStroke = terminal.readInput();
        while (keyStroke.getKeyType() != KeyType.Escape || keyStroke.getKeyType() != KeyType.EOF) {
            TerminalPosition position = terminal.getCursorPosition();
            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    terminal.setCursorPosition(position.withRelative(0, -1));
                    break;
                case ArrowDown:
                    terminal.setCursorPosition(position.withRelative(0, 1));
                    break;
                case ArrowLeft:
                    terminal.setCursorPosition(position.withRelative(-1, 0));
                    break;
                case ArrowRight:
                    terminal.setCursorPosition(position.withRelative(1, 0));
                    break;
                case Enter:
                    break;
                default:
                    break;
            }
            terminal.flush();
            keyStroke = terminal.readInput();
        }
    }
}
