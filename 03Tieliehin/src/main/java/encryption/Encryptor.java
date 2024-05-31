package encryption;
import models.Message;
import javax.crypto.Cipher;
import java.security.PublicKey;

public class Encryptor {
    private final PublicKey publicKey;

    public Encryptor(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] encrypt(Message message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.toBytes());
    }
}