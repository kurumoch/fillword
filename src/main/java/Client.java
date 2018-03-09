import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import models.Level;

import java.io.IOException;
import java.rmi.dgc.Lease;
import java.util.Collections;
import java.util.Random;

public class Client {

    public static void main(String[] args) {
        try {
            DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
            Terminal terminal = defaultTerminalFactory.createTerminal();
            Game game = new Game(terminal, new Level(String.join("",
                    Collections.nCopies(256, "Q")), 4));
            game.startLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
