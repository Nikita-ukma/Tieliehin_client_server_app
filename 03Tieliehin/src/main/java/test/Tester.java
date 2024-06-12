package test;

import FGenerator.FakeMessageGenerator;
import models.Processor;
import models.Store;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class Tester {
    public static void main(String[] args) throws Exception {
testStore();
    }

    private static void testStore() throws Exception {
        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        // Create store and add some products
        Store store = new Store();
        Processor processor = new Processor(store);
        Receiver receiver = new Receiver(keyPair.getPrivate(), 4, processor);
        Sender sender = new Sender(keyPair.getPublic());
        List<Message> messages = FakeMessageGenerator.generateRandomMessages();
        List<byte[]> encryptedPackets = new ArrayList<>();
        for (Message message : messages) {
            Packet packet = new Packet(message);
            encryptedPackets.add(sender.send(packet));
        }
        receiver.receive(encryptedPackets);
        processor.shutdown();
    }
}
