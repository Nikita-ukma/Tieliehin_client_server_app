package server_work;

import store_interface.LoginMenu;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;

public class StoreServerTCP {
    private Processor processor;
    private static PublicKey publicKey;

    public void start(int port, PrivateKey key) throws IOException, NoSuchAlgorithmException {
        ServerSocket serverSocket = new ServerSocket(port);
        processor = new Processor(key, 4);
        while (true) {
            Socket socket = serverSocket.accept();
            handleClient(socket);
        }
    }

    private void handleClient(Socket socket) {
        try (InputStream in = socket.getInputStream()) {
            byte[] buffer = new byte[274];
            int read;
            while ((read = in.read(buffer)) != -1) {
                byte[] receivedData = new byte[read];
                System.arraycopy(buffer, 0, receivedData, 0, read);
                processor.processEncryptedPackets(Collections.singletonList(receivedData));
            }
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            StoreServerTCP server = new StoreServerTCP();
            new Thread(() -> {
                try {
                    System.out.println("Server started");
                    server.start(8765, privateKey);
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            LoginMenu loginMenu = new LoginMenu();
            loginMenu.setBounds(510, 290, 405, 310);
            loginMenu.setVisible(true);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }
}