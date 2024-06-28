import server_work.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptorDecryptorTest {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    @BeforeAll
    static void setup() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    @Test
    void testEncryptDecrypt() throws Exception {
        Encryptor encryptor = new Encryptor(publicKey);
        Decryptor decryptor = new Decryptor(privateKey, 2);

        Message originalMessage = new Message(1,1,"Hello, World!");

        byte[] encryptedMessage = encryptor.encrypt(originalMessage);
        assertNotNull(encryptedMessage, "Encrypted message should not be null");

        Message decryptedMessage = decryptor.decrypt(encryptedMessage);
        assertNotNull(decryptedMessage, "Decrypted message should not be null");

        assertArrayEquals(originalMessage.toBytes(), decryptedMessage.toBytes(), "Original and decrypted messages should match");

        decryptor.shutdown();
    }

    @Test
    void testEncryptWithNullMessage() throws Exception {
        Encryptor encryptor = new Encryptor(publicKey);
        assertThrows(NullPointerException.class, () -> {
            encryptor.encrypt(null);
        }, "Encrypting a null message should throw NullPointerException");
    }
}