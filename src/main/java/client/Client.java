package client;

import client.util.ServerIO;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import client.models.Level;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;

public class Client {

    public static void main(String[] args) {
        int serverPort = 42422;
        String address = "127.0.0.1";
        boolean flag = true;
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            System.out.printf("Подключение к %s:%d.%n", address, serverPort);
            Socket socket = new Socket(ipAddress, serverPort);
            System.out.println("Подключение установлено!");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
            Terminal terminal = defaultTerminalFactory.createTerminal();
            Menu menu = new Menu(terminal, new ServerIO(in, out));
            menu.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
