import server_work.StoreServerTCP;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class StoreServerTCPTest {
    private static Connection con;
    @Test
    public void main() throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        KeyPairGenerator keyGen;
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        StoreServerTCP server = new StoreServerTCP();
        new Thread(() -> {
            try {
                server.start(6665, keyPair.getPrivate());
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Невелика затримка для того, щоб сервер встиг запуститися
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}