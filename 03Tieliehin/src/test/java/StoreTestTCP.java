import client.StoreClientTCP;
import server.StoreServerTCP;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class StoreTestTCP {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen;
                keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                KeyPair keyPair = keyGen.generateKeyPair();
        StoreServerTCP server = new StoreServerTCP();
        new Thread(() -> {
            try {
                server.start(6665, keyPair.getPrivate());
            } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Невелика затримка для того, щоб сервер встиг запуститися
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Тестування клієнта
        StoreClientTCP client = new StoreClientTCP(keyPair.getPublic());
        try {
            client.startConnection("localhost", 6665);
            client.send("Tomato", 6);
            // Відправляємо повідомлення серверу та отримуємо відповідь
            client.stopConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}