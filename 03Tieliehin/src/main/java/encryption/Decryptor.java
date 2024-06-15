package encryption;
import models.Message;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Decryptor {
    private final PrivateKey privateKey;
    private final ExecutorService executorService;

    public Decryptor(PrivateKey privateKey, int threadPoolSize) {
        this.privateKey = privateKey;
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void decryptMessages(List<byte[]> encryptedMessages) {
        for (byte[] encryptedMessage : encryptedMessages) {
            executorService.submit(() -> {
                try {
                    Message decryptedMessage = decrypt(encryptedMessage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public Message decrypt(byte[] encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
        return Message.fromBytes(decryptedBytes);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}