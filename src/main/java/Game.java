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

    private void drawFrame() throws IOException {

        graphics.drawLine(0, 0, terminal.getTerminalSize().getColumns(), 0, 'Ж');

        graphics.drawLine(0, 0, 0, terminal.getTerminalSize().getRows(), '*');
    }

    private boolean isSolved(int i, int j) {
        return solved.contains(START_POSITION.withRelative(i, j));
    }

    private TerminalPosition getRelative(int i, int j) {
        return START_POSITION.withRelative(i, j);
    }

    private void drawLevel() throws IOException {
        TerminalPosition position = terminal.getCursorPosition();
        drawFrame();
        for (int i = 0; i < SIDE_LENGTH; i++) {
            for (int j = 0; j < SIDE_LENGTH; j++) {
                if (!isSolved(2 * i + 1, j + 1))
                    graphics.putString(getRelative(2 * i + 1, j + 1),
                            String.valueOf(level.getField().charAt(SIDE_LENGTH * i + j)) + " ");
                else {
                    graphics.setBackgroundColor(TextColor.ANSI.GREEN);
                    if (isSolved(2 * i, j + 1))
                        graphics.putString(getRelative(2 * i, j + 1), " ", SGR.BOLD);
                    if (isSolved(2 * i + 2, j + 1))
                        graphics.putString(getRelative(2 * i + 2, j + 1), " ", SGR.BOLD);
                    graphics.putString(getRelative(2 * i + 1, j + 1),
                            String.valueOf(level.getField().charAt(SIDE_LENGTH * i + j)), SGR.BOLD);
                    graphics.setBackgroundColor(TextColor.ANSI.BLACK);
                }
            }
        }
        graphics.drawRectangle(START_POSITION,
                new TerminalSize(2 * SIDE_LENGTH + 1, SIDE_LENGTH + 2), '#');
        terminal.setCursorPosition(position);
        terminal.flush();
    }

    private void drawInfo() throws IOException {
        TerminalPosition position = terminal.getCursorPosition();
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 5, 0),
                "Состояние: " + state.name());
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 5, 2),
                "Слов осталось: " + level.getWordsToSolve());
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 5, 4),
                "Время: ");

        terminal.setCursorPosition(position);
        terminal.flush();
    }

    private boolean ifNotBorder(TerminalPosition newPosition) {
        return START_POSITION.getColumn() < newPosition.getColumn() &&
                START_POSITION.getRow() < newPosition.getRow() &&
                START_POSITION.getColumn() + 2 * SIDE_LENGTH + 1 > newPosition.getColumn() &&
                START_POSITION.getRow() + SIDE_LENGTH + 1 > newPosition.getRow();
    }

    private void select(TerminalPosition position) {
        graphics.setBackgroundColor(TextColor.ANSI.BLUE);
        graphics.putString(position,
                String.valueOf(graphics.getCharacter(position).getCharacter()), SGR.BOLD);
        selected.add(position);
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private void moveCursor(int di, int dj) throws IOException {
        TerminalPosition position = terminal.getCursorPosition();
        TerminalPosition newPosition = position.withRelative(di, dj);
        if (ifNotBorder(newPosition)) {
            if (state == State.SELECT && !selected.contains(newPosition) && !solved.contains(newPosition)) {
                if (di > 0)
                    select(position.withRelative(1, 0));
                if (di < 0)
                    select(position.withRelative(-1, 0));
                select(position);
                terminal.setCursorPosition(newPosition);
                terminal.flush();
            }
            if (state == State.SEARCH) {
                terminal.setCursorPosition(newPosition);
                terminal.flush();
            }
        }
    }

    public void startLevel() throws IOException {
        terminal.enterPrivateMode();
        drawLevel();
        terminal.setCursorPosition(START_POSITION.withRelative(1, 1));
        terminal.flush();
        drawInfo();
        graphics = terminal.newTextGraphics();
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
                    moveCursor(-2, 0);
                    break;
                case ArrowRight:
                    moveCursor(2, 0);
                    break;
                case Enter:
                    if (state == State.SEARCH && !solved.contains(terminal.getCursorPosition()))
                        state = State.SELECT;
                    else {
                        moveCursor(0, 0);
                        state = State.SEARCH;
                        submit(selectedWord);
                        processReply(getServerReply());
                        selected = new ArrayList<>();
                    }
                    drawInfo();
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

    private void processReply(boolean reply) throws IOException {
        if (reply) {
            level.solveWord();
            markAsSolved();
        }
        drawLevel();
    }

    private void markAsSolved() {
        solved.addAll(selected);
    }
}
