package client.util;

import client.models.Level;
import com.sun.tools.javac.util.Pair;

import java.io.IOException;
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

    public int getLevelCount() throws IOException {
        oos.writeUTF("A");
        return ois.readInt();
    }

    public Level getLevel(int n) throws IOException, ClassNotFoundException {
        oos.writeUTF("B,1");
        return (Level) ois.readObject();
    }

    public boolean submit(String s) throws IOException {
        oos.writeUTF("C," + s);
        return ois.readBoolean();
    }

    public void sendResults(String nick, int score) throws IOException {
        oos.writeUTF("D," + nick + "," + score);
    }

    public ArrayList<Pair<String, Integer>> getScores(int n) throws IOException, ClassNotFoundException {
        oos.writeUTF("E," + n);
        return (ArrayList<Pair<String, Integer>>) ois.readObject();
    }
}
