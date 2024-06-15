import client.StoreClientTCP;
import models.Message;
import models.Packet;
import org.junit.jupiter.api.Test;
import server.StoreServerTCP;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StoreTestTCP {
    private static Connection con;
    @Test
    public void main() throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        KeyPairGenerator keyGen;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + "StoreDB");
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
            client.send("Молоко,Опис молока,Виробник 1,100,20.50,1", 1);
            // Відправляємо повідомлення серверу та отримуємо відповідь
            client.stopConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}