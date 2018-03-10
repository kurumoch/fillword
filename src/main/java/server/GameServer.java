package server;

import client.models.Level;
import com.sun.tools.javac.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class GameServer {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public GameServer(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    public void start() {
        while (true) {
            try {
                    String[] request = ois.readUTF().split(",");
                    switch (request[0]) {
                        case "A":
                            oos.writeInt(3);
                            break;
                        case "B":
                            Level level = new Level(String.join("",
                                    Collections.nCopies(256, "Q")), 4, Integer.valueOf(request[1]));
                            oos.writeObject(level);
                            break;
                        case "C":
                            oos.writeBoolean(true);
                            break;
                        case "D":
                            break;
                        case "E":
                            ArrayList<Pair<String, Integer>> out = new ArrayList<>();
                            out.add(new Pair<>("sadas", 2));
                            out.add(new Pair<>("sadasdsaas", 212));
                            out.add(new Pair<>("sqwdqadas", 42));
                            out.add(new Pair<>("qqsadas", 2));
                            out.add(new Pair<>("s", 12));
                            oos.writeObject(out);
                            break;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
