import encryption.*;
import models.Message;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class Receiver implements MessageHandler {
    private final Decryptor decryptor;
    private final List<Message> decryptedMessages;
    private final Processor processor;

    public Receiver(PrivateKey privateKey, int threadPoolSize, Processor processor) {
        this.decryptor = new Decryptor(privateKey, threadPoolSize, this);
        this.decryptedMessages = new ArrayList<>();
        this.processor = processor;
    }

    public void receive(List<byte[]> encryptedMessages) {
        decryptor.decryptMessages(encryptedMessages);
    }

    @Override
    public void handle(Message message) {
        decryptedMessages.add(message);
        processor.processMessage(message);
    }

    public List<Message> getDecryptedMessages() {
        return new ArrayList<>(decryptedMessages);
    }
}