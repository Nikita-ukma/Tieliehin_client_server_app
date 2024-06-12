import client.StoreClientUDP;
import server.StoreServerUDP;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTestUDP {

    static StoreServerUDP server;
    static PublicKey key;

    @BeforeAll
    static void setup() throws SocketException, NoSuchAlgorithmException {
        server = new StoreServerUDP();
        key = server.getPublicKey();
        new Thread(server).start();
    }

    @AfterAll
    static void teardown() {
        server.stopServer();
    }

    @Test
    void testClientServerCommunication() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            String response = client.send("Tomato", 6);
            assertNotNull(response);
            assertEquals("Tomato received", response);
            client.close();
        });
    }

    @Test
    void testMultipleMessages() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            String response1 = client.send("Tomato", 6);
            assertNotNull(response1);
            assertEquals("Tomato received", response1);

            String response2 = client.send("Potato", 6);
            assertNotNull(response2);
            assertEquals("Potato received", response2);

            client.close();
        });
    }

    @Test
    void testConcurrentClients() {
        assertDoesNotThrow(() -> {
            Thread client1 = new Thread(() -> {
                try {
                    StoreClientUDP client = new StoreClientUDP();
                    client.setPublicKey(key);
                    String response = client.send("Tomato", 6);
                    assertNotNull(response);
                    assertEquals("Tomato received", response);
                    client.close();
                } catch (Exception e) {
                    fail("Client 1 failed: " + e.getMessage());
                }
            });

            Thread client2 = new Thread(() -> {
                try {
                    StoreClientUDP client = new StoreClientUDP();
                    client.setPublicKey(key);
                    String response = client.send("Potato", 6);
                    assertNotNull(response);
                    assertEquals("Potato received", response);
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

    @Test
    void testPacketCache() {
        assertDoesNotThrow(() -> {
            StoreClientUDP client = new StoreClientUDP();
            client.setPublicKey(key);
            for (int i = 0; i < 55; i++) {
                String response = client.send("Product" + i, 6);
                assertNotNull(response);
                assertEquals("Product" + i + " received", response);
            }
            client.close();
            assertEquals(50, server.getPacketCache().size());
        });
    }
}