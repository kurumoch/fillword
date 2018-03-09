import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import models.Level;

import java.io.IOException;

public class Game {

    private Terminal terminal;
    private Level level;
    private TextGraphics graphics;
    private static final TerminalPosition START_POSITION = new TerminalPosition(5, 3);
    private static final int SIDE_LENGTH = 16;

    public Game(Terminal terminal, Level level) throws IOException {
        this.terminal = terminal;
        this.level = level;
        graphics = terminal.newTextGraphics();
    }

    private void drawLevel() throws IOException {
        graphics.drawRectangle(START_POSITION, new TerminalSize(SIDE_LENGTH + 2, SIDE_LENGTH + 2), '#');
        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                graphics.setCharacter(START_POSITION.withRelative(i + 1, j + 1),
                        level.getField().charAt(SIDE_LENGTH * i + j));
            }
        }
        terminal.flush();
    }

    private void moveCursor(int di, int dj) throws IOException {
        TerminalPosition position = terminal.getCursorPosition();
        TerminalPosition newPosition = position.withRelative(di, dj);
        if (START_POSITION.getColumn() < newPosition.getColumn() &&
                START_POSITION.getRow() < newPosition.getRow() &&
                START_POSITION.getColumn() + SIDE_LENGTH > newPosition.getColumn() &&
                START_POSITION.getRow() + SIDE_LENGTH > newPosition.getRow())
            terminal.setCursorPosition(newPosition);
    }

    public void startLevel() throws IOException {
        terminal.enterPrivateMode();
        drawLevel();
        terminal.setCursorPosition(START_POSITION.withRelative(1, 1));
        terminal.flush();
        graphics = terminal.newTextGraphics();
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);

        KeyStroke keyStroke = terminal.readInput();
        while (keyStroke.getKeyType() != KeyType.Escape && keyStroke.getKeyType() != KeyType.EOF) {

            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    moveCursor(0, -1);
                    break;
                case ArrowDown:
                    moveCursor(0, 1);
                    break;
                case ArrowLeft:
                    moveCursor(-1, 0);
                    break;
                case ArrowRight:
                    moveCursor(1, 0);
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
