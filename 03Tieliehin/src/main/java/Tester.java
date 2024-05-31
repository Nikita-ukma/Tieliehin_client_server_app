import FGenerator.FakeMessageGenerator;
import Store.Store;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class Tester {
    public static void main(String[] args) throws Exception {
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
//        for (int i = 0; i < 20; i++) {
//            Message.printMessageDetails(messages.get(i));
//        }
        List<byte[]> encryptedMessages = new ArrayList<>();
        for (Message message : messages) {
            encryptedMessages.add(sender.send(message));
        }
        receiver.receive(encryptedMessages);
        processor.shutdown();
    }
}
