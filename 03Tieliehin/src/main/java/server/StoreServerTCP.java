package server;

import models.Processor;
import models.Receiver;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.SQLException;

public class StoreServerTCP {
    private ServerSocket serverSocket;
    private Receiver receiver;

    private Processor processor;

    public StoreServerTCP() throws SQLException {
        this.processor = new Processor();
    }

    public void start(int port, PrivateKey key) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        serverSocket = new ServerSocket(port);
        receiver = new Receiver(key, 4, processor);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    handleClient(socket);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
        }
    }

    private void handleClient(Socket socket) throws Exception {
         InputStream in = socket.getInputStream();
            Processor.getConnection();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                byte[] receivedData = new byte[read];
                System.arraycopy(buffer, 0, receivedData, 0, read);
                receiver.receive(receivedData);
            }
        }

    public void stopServer() throws IOException {
        serverSocket.close();
    }
}

