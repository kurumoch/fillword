package client.util;

import client.models.Level;
import client.models.Pair;

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
        oos.flush();
        int c = ois.readInt();
        return c;
    }

    public Level getLevel(int n) throws IOException, ClassNotFoundException {
        oos.writeUTF("B," + n);
        oos.flush();
        return (Level) ois.readObject();
    }

    public boolean submit(String s) throws IOException {
        oos.writeUTF("C," + s);
        oos.flush();
        return ois.readBoolean();
    }

    public void sendResults(String nick, int score, int n) throws IOException {
        oos.writeUTF("D," + nick + "," + score + "," + n);
        oos.flush();
    }

    public ArrayList<Pair<String, Integer>> getScores(int n) throws IOException, ClassNotFoundException {
        oos.writeUTF("E," + n);
        oos.flush();
        return (ArrayList<Pair<String, Integer>>) ois.readObject();
    }
}
