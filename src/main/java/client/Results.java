package client;

import client.util.ServerIO;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.sun.tools.javac.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class Results {

    private Terminal terminal;
    private TextGraphics graphics;
    private int levelNum;
    private int results;
    private ServerIO io;

    public Results(Terminal terminal, int levelNum, int results, ServerIO io) throws IOException {
        this.terminal = terminal;
        graphics = terminal.newTextGraphics();
        this.levelNum = levelNum;
        this.results = results;
        this.io = io;
    }

    public void show() throws IOException, ClassNotFoundException {
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
        io.sendResults(nickname.toString(), results);
        terminal.clearScreen();
        terminal.setCursorVisible(false);
        ArrayList<Pair<String, Integer>> scores = io.getScores(levelNum);
        graphics.putString(5, 3, "Результаты для уровня " + levelNum, SGR.BOLD);
        for (int i = 0; i < scores.size(); i++) {
            graphics.putString(7, 5 + i * 2, String.valueOf(i) + ". " + scores.get(i).fst + " - " + scores.get(i).snd);
        }
        terminal.flush();

        keyStroke = terminal.readInput();
        while (keyStroke.getKeyType() != KeyType.EOF &&
                keyStroke.getKeyType() != KeyType.Escape && keyStroke.getKeyType() != KeyType.Enter) {
            keyStroke = terminal.readInput();
        }
    }


}

