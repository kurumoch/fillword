package client;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import client.models.Level;
import client.util.ServerIO;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


//можно было бы отделить вью от контроллера
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
    private Instant start;
    private Timer timer;
    private ServerIO io;

    public Game(Terminal terminal, int num, ServerIO io) throws IOException, ClassNotFoundException {
        this.terminal = terminal;
        this.io = io;
        this.level = io.getLevel(num);
        graphics = terminal.newTextGraphics();
        selectedWord = "";
        solved = new ArrayList<>();
        selected = new ArrayList<>();
        state = State.SEARCH;
        terminal.setCursorVisible(true);
        start = Instant.now();

    }

    private void drawFrame() throws IOException {
//        graphics.setBackgroundColor(TextColor.ANSI.GREEN);
//        graphics.setForegroundColor(TextColor.ANSI.RED);
        graphics.drawLine(42, 0,
                42, terminal.getTerminalSize().getRows() - 1, 'ß');
//        graphics.setForegroundColor(TextColor.ANSI.WHITE);
//        graphics.setBackgroundColor(TextColor.ANSI.BLUE);
        graphics.drawLine(41, 0,
                41, terminal.getTerminalSize().getRows() - 1, '¶');
//        graphics.setBackgroundColor(TextColor.ANSI.BLUE);
        graphics.drawLine(0, 0, terminal.getTerminalSize().getColumns(), 0, 'Ж');
//        graphics.setBackgroundColor(TextColor.ANSI.YELLOW);
        graphics.drawLine(0, 1, terminal.getTerminalSize().getColumns(), 1, 'Ф');
//        graphics.setBackgroundColor(TextColor.ANSI.WHITE);
//        graphics.setForegroundColor(TextColor.ANSI.BLUE);
        graphics.drawLine(0, 0, 0, terminal.getTerminalSize().getRows(), '@');
//        graphics.setForegroundColor(TextColor.ANSI.YELLOW);
//        graphics.setBackgroundColor(TextColor.ANSI.CYAN);
        graphics.drawLine(1, 0, 1, terminal.getTerminalSize().getRows(), '*');
//        graphics.setForegroundColor(TextColor.ANSI.WHITE);
//        graphics.setBackgroundColor(TextColor.ANSI.RED);
        graphics.drawLine(terminal.getTerminalSize().getColumns() - 1, 0,
                terminal.getTerminalSize().getColumns() - 1, terminal.getTerminalSize().getRows(), '%');
//        graphics.setBackgroundColor(TextColor.ANSI.GREEN);
//        graphics.setForegroundColor(TextColor.ANSI.MAGENTA);
        graphics.drawLine(terminal.getTerminalSize().getColumns() - 2, 0,
                terminal.getTerminalSize().getColumns() - 2, terminal.getTerminalSize().getRows(), '∆');
//        graphics.setForegroundColor(TextColor.ANSI.WHITE);
//        graphics.setBackgroundColor(TextColor.ANSI.CYAN);
        graphics.drawLine(0, terminal.getTerminalSize().getRows() - 1,
                terminal.getTerminalSize().getColumns() - 1, terminal.getTerminalSize().getRows() - 1, 'Ω');
//        graphics.setBackgroundColor(TextColor.ANSI.MAGENTA);
//        graphics.setForegroundColor(TextColor.ANSI.BLACK);
        graphics.drawLine(0, terminal.getTerminalSize().getRows() - 2,
                terminal.getTerminalSize().getColumns() - 1, terminal.getTerminalSize().getRows() - 2, '∑');
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
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
                            String.valueOf(level.getField().charAt(SIDE_LENGTH * i + j)) + " ", SGR.BOLD);
                else {
                    graphics.setBackgroundColor(TextColor.ANSI.GREEN);
                    if (isSolved(2 * i, j + 1))
                        graphics.putString(getRelative(2 * i, j + 1), " ");
                    if (isSolved(2 * i + 2, j + 1))
                        graphics.putString(getRelative(2 * i + 2, j + 1), " ");
                    graphics.putString(getRelative(2 * i + 1, j + 1),
                            String.valueOf(level.getField().charAt(SIDE_LENGTH * i + j)));
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
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 0),
                "Состояние: " + state.name(), SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 2),
                "Слов осталось: " + level.getWordsToSolve(), SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 4),
                "Время: " + Duration.between(start, Instant.now()).getSeconds(), SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 6),
                "Выбраное слово: " + selectedWord, SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 10),
                "Esc — выход в меню", SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 12),
                "Enter — режим выбора слова", SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 14),
                "Для выхода из режима выбора", SGR.BOLD);
        graphics.putString(START_POSITION.withRelative(2 * SIDE_LENGTH + 9, 15),
                "слова нажмите Enter еще раз", SGR.BOLD);
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
                if (selectedWord.contains(" "))
                    selectedWord = "";
                selectedWord += graphics.getCharacter(position).getCharacter();
                terminal.setCursorPosition(newPosition);
                terminal.flush();
            }
            if (state == State.SEARCH) {
                terminal.setCursorPosition(newPosition);
                terminal.flush();
            }
        }
    }

    public void startLevel() throws IOException, ClassNotFoundException {
        terminal.enterPrivateMode();
        drawLevel();
        terminal.setCursorPosition(START_POSITION.withRelative(1, 1));
        terminal.flush();
        drawInfo();
        timer = new Timer();
        timer.schedule(new InfoUpdater(), 0, 200);
        graphics = terminal.newTextGraphics();
        KeyStroke keyStroke;
        do {
            keyStroke = terminal.readInput();
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
                        boolean reply = checkWord(selectedWord);
                        processReply(reply);
                    }
                    drawInfo();
                    break;
                default:
                    break;
            }
            terminal.flush();
        }
        while (keyStroke.getKeyType() != KeyType.Escape &&
                keyStroke.getKeyType() != KeyType.EOF && level.getWordsToSolve() != 0);
        timer.cancel();
        if (keyStroke.getKeyType() == KeyType.Enter) {
            Results results = new Results(terminal, level.getNumber(), countResults(), io);
            results.show();
        }
    }

    public int countResults() {
        return 300 - Math.round(Duration.between(start, Instant.now()).getSeconds());
    }

    private boolean checkWord(String s) throws IOException {
        return io.submit(s);
    }

    private void processReply(boolean reply) throws IOException {
        if (reply) {
            level.solveWord();
            markAsSolved();
        }
        selected = new ArrayList<>();
        selectedWord = "                ";
        drawLevel();
    }

    private void markAsSolved() {
        solved.addAll(selected);
    }


    private class InfoUpdater extends TimerTask {
        @Override
        public void run() {
            try {
                drawInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
