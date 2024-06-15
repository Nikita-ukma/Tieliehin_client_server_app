package client;

import models.Message;
import models.Packet;
import models.Sender;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class StoreClientTCP {
    private final models.Sender sender;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final int id;
    private static int freeId = 0;

    public StoreClientTCP(PublicKey aPublic) {
        this.id = freeId++;
        sender = new Sender(aPublic);
    }

    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void send(String product, int cType) throws Exception {
        Message message = new Message(cType, id, product);
        Packet packet = new Packet(message);
        byte[] sended = sender.send(packet);
        try (Socket socket = new Socket("localhost", 6665);
             OutputStream out = socket.getOutputStream()) {
            out.write(sended);
            out.flush();
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
    }


