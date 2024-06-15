import client.StoreClientUDP;
import server.StoreServerUDP;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTestUDP {

    static StoreServerUDP server;
    static PublicKey key;

    static void setup() throws SocketException, NoSuchAlgorithmException {
        server = new StoreServerUDP();
        key = server.getPublicKey();
        new Thread(server).start();
    }

    static void teardown() {
        server.stopServer();
    }

    void testClientServerCommunication() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            String response =  client.send("М'ясо,Опис молока,Виробник 1,100,20.50,1", 1);
            assertNotNull(response);
            assertEquals("М'ясо,Опис молока,Виробник 1,100,20.50,1", response);
            client.close();
        });
    }

    void testMultipleMessages() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            String response1 = client.send("Tomato", 6);
            assertNotNull(response1);
            assertEquals("Tomato", response1);

            String response2 = client.send("Potato", 6);
            assertNotNull(response2);
            assertEquals("Potato", response2);

            client.close();
        });
    }

    void testConcurrentClients() {
        assertDoesNotThrow(() -> {
            Thread client1 = new Thread(() -> {
                try {
                    StoreClientUDP client = new StoreClientUDP();
                    client.setPublicKey(key);
                    String response = client.send("Tomato", 1);
                    assertNotNull(response);
                    assertEquals("Tomato", response);
                    client.close();
                } catch (Exception e) {
                    fail("Client 1 failed: " + e.getMessage());
                }
            });

            Thread client2 = new Thread(() -> {
                try {
                    StoreClientUDP client = new StoreClientUDP();
                    client.setPublicKey(key);
                    client.startConnection("localhost", 4445);
                    String response = client.send("Potato", 6);
                    assertNotNull(response);
                    assertEquals("Potato", response);
                    client.close();
                } catch (Exception e) {
                    fail("Client 2 failed: " + e.getMessage());
                }
            });

            client1.start();
            client2.start();

            client1.join();
            client2.join();
        });
    }

    void testPacketCache() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            client.startConnection("localhost", 4445);
            for (int i = 0; i < 55; i++) {
                String response = client.send("Product" + i, 6);
                assertNotNull(response);
                assertEquals("ACK", response);
            }
            client.close();
            assertEquals(50, server.getPacketCache().size());
        });
    }
}