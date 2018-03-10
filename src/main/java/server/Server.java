package server;

import com.sun.tools.javac.util.Pair;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 42422;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Ожидание подключения...");
            while (true) {
                Socket socket = ss.accept();
                System.out.println("Подключение установлено!");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                GameServer gameServer = new GameServer(ois, oos);
                new Thread(gameServer::start).start();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
