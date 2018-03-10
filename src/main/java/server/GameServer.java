package server;

import client.models.Level;
import client.models.Pair;

import java.util.*;

import server.util.DBUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import static server.util.DBUtil.*;

public class GameServer {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private List<String> words;

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
                        oos.writeInt(getLevelCount());
                        oos.flush();
                        break;
                    case "B":
                        int n = Integer.valueOf(request[1]);
                        Pair<String, String> pair = getLevelInfo(n);
                        words = new LinkedList<>(Arrays.asList(pair.snd.split(",")));
                        words.replaceAll(String::trim);
                        oos.writeObject(new Level(pair.fst, words.size(), n));
                        oos.flush();
                        break;
                    case "C":
                        oos.writeBoolean(words.contains(request[1]));
                        words.remove(request[1]);
                        oos.flush();
                        break;
                    case "D":
                        writeResult(request[1], Integer.valueOf(request[2]), Integer.valueOf(request[3]));
                        oos.flush();
                        break;
                    case "E":
                        oos.writeObject(getScores(Integer.valueOf(request[1])));
                        oos.flush();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
