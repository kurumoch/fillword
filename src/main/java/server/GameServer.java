package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameServer {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public GameServer(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    public void start() throws IOException, ClassNotFoundException {
        Object o = ois.readObject();
    }
}
