import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PacketSenderTest {

    private PacketSender packetSender;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        packetSender = new PacketSender();
    }

    @Test
    public void testSendAndEncryptPacket() throws Exception {
        byte bSrc = 0x01;
        long bPktId = 123456789L;
        String messageContent = "Test Message";
        Message message = new Message(1, 1, messageContent);

        Packet originalPacket = new Packet(bSrc, bPktId, message);
        Packet encryptedPacket = PacketSender.send(originalPacket.toBytes());

        assertNotEquals(originalPacket.toBytes(), encryptedPacket.toBytes());
        assertTrue(encryptedPacket.isValid());

        List<Packet> encryptedPackets = packetSender.getEncryptedPackets();
        assertEquals(1, encryptedPackets.size());
        assertEquals(encryptedPacket, encryptedPackets.get(0));
    }

    @Test
    public void testSendInvalidPacket() {
        byte bSrc = 0x01;
        long bPktId = 123456789L;
        byte[] invalidMessage = new byte[8]; // Too short for a valid message
        Packet invalidPacket = new Packet(bSrc, bPktId, invalidMessage);

        assertThrows(IllegalArgumentException.class, () -> PacketSender.send(invalidPacket.toBytes()));
    }

    @Test
    public void testGetKey() {
        byte[] key = packetSender.getKey();
        assertNotNull(key);
        assertEquals(32, key.length); // 256 bits = 32 bytes
    }
}