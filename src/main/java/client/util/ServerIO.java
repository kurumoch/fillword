package client.util;

import client.models.Level;
import com.sun.tools.javac.util.Pair;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ServerIO {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ServerIO(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    public int getLevelCount() {
        return 5;
    }

    public Level getLevel(int n) {
        return new Level(String.join("",
                Collections.nCopies(256, "Q")), 4, 1);
    }

    public boolean submit(String s) {
        return true;
    }

    public void sendResults(String nick, int score) {

    }

    public ArrayList<Pair<String, Integer>> getScores(){
        ArrayList<Pair<String, Integer>> out = new ArrayList<>();
        out.add(new Pair<>("sadas", 2));
        out.add(new Pair<>("sadasdsaas", 212));
        out.add(new Pair<>("sqwdqadas", 42));
        out.add(new Pair<>("qqsadas", 2));
        out.add(new Pair<>("s", 12));
        return out;
    }
}
