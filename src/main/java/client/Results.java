package client;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.sun.tools.javac.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Results {

    private Terminal terminal;
    private TextGraphics graphics;
    private int level;
    private int results;

    public Results(Terminal terminal, int level, int results) throws IOException {
        this.terminal = terminal;
        graphics = terminal.newTextGraphics();
        this.level = level;
        this.results = results;
    }

    public void show() throws IOException {
        terminal.clearScreen();
        graphics.putString(5, 3, "Ваш счет: " + results, SGR.BOLD);
        graphics.putString(5, 5, "Введите имя", SGR.BOLD);
        terminal.setCursorVisible(true);
        terminal.setCursorPosition(7, 7);
        terminal.flush();
        KeyStroke keyStroke = terminal.readInput();
        StringBuilder nickname = new StringBuilder();
        while (keyStroke.getKeyType() != KeyType.Enter) {
            switch (keyStroke.getKeyType()) {
                case Character:
                    nickname.append(keyStroke.getCharacter());
                    graphics.setCharacter(terminal.getCursorPosition(), keyStroke.getCharacter());
//                    terminal.setCursorPosition(terminal.getCursorPosition().withRelative(1, 0));
                    terminal.flush();
                    break;
                case Backspace:
                    graphics.setCharacter(terminal.getCursorPosition()
                            .withRelative(-1, 0), ' ');
                    terminal.setCursorPosition(terminal.getCursorPosition().withRelative(-1, 0));
                    nickname.deleteCharAt(nickname.length() - 1);
                    terminal.flush();
                    break;
            }

            keyStroke = terminal.readInput();
        }

        sendResults();
        terminal.clearScreen();
        terminal.setCursorVisible(false);
        ArrayList<Pair<String, Integer>> scores = loadScores();
        graphics.putString(5, 3, "Результаты для уровня " + level, SGR.BOLD);
        for (int i = 0; i < scores.size(); i++) {
            graphics.putString(7, 5 + i, String.valueOf(i) + ". " + scores.get(i).fst + " - " + scores.get(i).snd);
        }
        terminal.flush();


        while (keyStroke.getKeyType() != KeyType.EOF &&
                keyStroke.getKeyType() != KeyType.Escape && keyStroke.getKeyType() != KeyType.Enter) {
            keyStroke = terminal.readInput();
        }
    }

    public void sendResults() {
//         Math.round(Duration.between(start, Instant.now()).getSeconds());
    }

    public ArrayList<Pair<String, Integer>> loadScores() {
        ArrayList<Pair<String, Integer>> out = new ArrayList<>();
        out.add(new Pair<>("sadas", 2));
        out.add(new Pair<>("sadasdsaas", 212));
        out.add(new Pair<>("sqwdqadas", 42));
        out.add(new Pair<>("qqsadas", 2));
        out.add(new Pair<>("s", 12));
        return out;
    }


}

