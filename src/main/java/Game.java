import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import models.Level;

import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private Terminal terminal;
    private Level level;
    private TextGraphics graphics;
    private static final TerminalPosition START_POSITION = new TerminalPosition(5, 3);
    private static final int SIDE_LENGTH = 16;
    private State state;
    private String selectedWord;
    private ArrayList<TerminalPosition> solved;
    private ArrayList<TerminalPosition> selected;

    public Game(Terminal terminal, Level level) throws IOException {
        this.terminal = terminal;
        this.level = level;
        graphics = terminal.newTextGraphics();
        selectedWord = "";
        solved = new ArrayList<>();
        selected = new ArrayList<>();
        state = State.SEARCH;
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
                START_POSITION.getRow() + SIDE_LENGTH > newPosition.getRow()) {
            if (state == State.SELECT) {

                graphics.setBackgroundColor(TextColor.ANSI.BLUE);
                graphics.putString(position,
                        String.valueOf(graphics.getCharacter(position).getCharacter()), SGR.BOLD);
            }
            terminal.setCursorPosition(newPosition);
            terminal.flush();
        }
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
                    if (state == State.SEARCH)
                        state = State.SELECT;
                    else {
                        moveCursor(0, 0);
                        submit(selectedWord);
                        state = State.SEARCH;
                        processReply(getServerReply());
                    }
                    break;
                default:
                    break;
            }
            terminal.flush();
            keyStroke = terminal.readInput();
        }
    }

    private void submit(String s) {

    }

    private boolean getServerReply() {
        return true;
    }

    private void processReply(boolean reply) {
        if (reply) {
            level.solve();
            markAsSolved();
        }
        redrawLevel();
    }

    private void markAsSolved() {
        solved.addAll(selected);
    }

    private void redrawLevel() {

    }
}
