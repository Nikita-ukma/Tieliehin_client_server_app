import encryption.Encryptor;
import models.Message;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Sender {
    private final Encryptor encryptor;
    private final List<byte[]> encryptedMessages;

    public Sender(PublicKey publicKey) {
        this.encryptor = new Encryptor(publicKey);
        this.encryptedMessages = new ArrayList<>();
    }

    public byte[] send(Message message) throws Exception {
        byte[] encryptedMessage = encryptor.encrypt(message);
        encryptedMessages.add(encryptedMessage);
        return encryptedMessage;
    }
    public Message send(byte[]  message) throws Exception {
        byte[] encryptedMessage = encryptor.encrypt(Message.fromBytes(message));
        encryptedMessages.add(encryptedMessage);
        return Message.fromBytes(encryptedMessage);
    }
    public List<byte[]> getEncryptedMessages() {
        return new ArrayList<>(encryptedMessages);
    }
}