package server;

import models.Processor;
import models.Receiver;
import models.Store;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
public class StoreServerTCP {
    private ServerSocket serverSocket;
    Receiver receiver;
    Store store = new Store();
    Processor processor = new Processor(store);
    public void start(int port, PrivateKey key) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        serverSocket = new ServerSocket(port);
        receiver = new Receiver(key, 4, processor);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

private void handleClient(Socket socket) {
    try (InputStream in = socket.getInputStream()) {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            byte[] receivedData = new byte[read];
            System.arraycopy(buffer, 0, receivedData, 0, read);
            receiver.receive(receivedData);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
//        private void sendKey(PublicKey key) throws NoSuchAlgorithmException, InvalidKeySpecException {
//            String str = new String(key.getEncoded(), StandardCharsets.UTF_8);
//            System.out.println("! !  !   !     !");
//            System.out.println("! !  !   !     !" + str);
//
//            out.write(str);
//            out.flush();
//        }

