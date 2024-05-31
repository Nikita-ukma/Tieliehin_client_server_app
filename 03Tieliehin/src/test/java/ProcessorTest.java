import encryption.Decryptor;
import models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import Store.Store;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessorTest {
    private Store store;
    private Processor processor;
    private Receiver receiver;
    private Sender sender;

    @BeforeEach
    public void setUp() throws Exception {
        store = new Store();
        processor = new Processor(store);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        receiver = new Receiver(privateKey, 10, processor);
        sender = new Sender(pair.getPublic());
    }

    @Test
    public void testConcurrentProcessing() throws Exception {
        List<byte[]> encryptedMessages = new ArrayList<>();
        encryptedMessages.add(sender.send(new Message(1, 1001, "Apple")));
        encryptedMessages.add(sender.send(new Message(2, 1002, "Banana,10")));
        encryptedMessages.add(sender.send(new Message(3, 1003, "Carrot,20")));
        encryptedMessages.add(sender.send(new Message(4, 1004, "Potato,1.50")));
        receiver.receive((encryptedMessages));
        processor.shutdown();

        assertTrue(store.hasProduct("Apple"));
        assertTrue(store.hasProduct("Banana"));
        assertTrue(store.hasProduct("Carrot"));
        assertTrue(store.hasProduct("Potato"));
    }
}