import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PacketReceiverTest {

    private static final byte[] AES_KEY = "1234567890123456".getBytes(StandardCharsets.UTF_8);
    private Key key;
    private PacketReceiver packetReceiver;

    @BeforeEach
    public void setUp() {
        key = new SecretKeySpec(AES_KEY, "AES");
        packetReceiver = new PacketReceiver(AES_KEY);
    }

    @Test
    public void testReceiveAndDecryptPacket() throws Exception {
        byte bSrc = 0x01;
        long bPktId = 123456789L;
        String messageContent = "Test Message";
        Message message = new Message(1, 1, messageContent);

        Packet originalPacket = new Packet(bSrc, bPktId, message);
        byte[] packetBytes = originalPacket.toBytes();

        // Encrypt the message part of the packet
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedMessage = cipher.doFinal(message.toBytes());

        // Replace the plain text message with the encrypted message in the packet bytes
        System.arraycopy(encryptedMessage, 0, packetBytes, 16, encryptedMessage.length);

        Packet decryptedPacket = packetReceiver.receive(packetBytes);

        assertEquals(bSrc, decryptedPacket.getbSrc());
        assertEquals(bPktId, decryptedPacket.getbPktId());
        assertEquals(messageContent, decryptedPacket.getbMsg().getMessage());
        assertTrue(decryptedPacket.isValid());

        assertEquals(1, packetReceiver.getDecryptedPackets().size());
        assertEquals(decryptedPacket, packetReceiver.getDecryptedPackets().get(0));
    }

    @Test
    public void testReceiveInvalidPacket() {
        byte[] invalidPacket = new byte[18];
        Arrays.fill(invalidPacket, (byte) 0xFF);

        assertThrows(IllegalArgumentException.class, () -> packetReceiver.receive(invalidPacket));
    }
    }