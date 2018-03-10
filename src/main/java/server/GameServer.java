package server;

import client.models.Level;
import client.models.Pair;

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
        try {
            while (true) {
                String[] request = ois.readUTF().split(",");
                switch (request[0]) {
                    case "A":
                        oos.writeInt(3);
                        oos.flush();
                        break;
                    case "B":
                        Level level = new Level(String.join("",
                                Collections.nCopies(256, "Q")), 4, Integer.valueOf(request[1]));
                        oos.writeObject(level);
                        oos.flush();
                        break;
                    case "C":
                        oos.writeBoolean(true);
                        oos.flush();
                        break;
                    case "D":
                        oos.flush();
                        break;
                    case "E":
                        ArrayList<Pair<String, Integer>> out = new ArrayList<>();
                        out.add(new Pair<>("sadas", 2));
                        out.add(new Pair<>("sadasdsaas", 212));
                        out.add(new Pair<>("sqwdqadas", 42));
                        out.add(new Pair<>("qqsadas", 2));
                        out.add(new Pair<>("s", 12));
                        oos.writeObject(out);
                        oos.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
