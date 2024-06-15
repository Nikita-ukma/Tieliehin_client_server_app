import models.Message;
import models.Packet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PacketTest {
    @Test
    public void testPacketToBytesAndBack() {
        byte bSrc = 0x01;
        String messageContent = "Test Message";
        Message message = new Message(1, 1, messageContent);
        Packet packet = new Packet(message);

        byte[] packetBytes = packet.toBytes();
        Packet deserializedPacket = Packet.fromBytes(packetBytes);

        assertEquals(bSrc, deserializedPacket.getbSrc());
        assertEquals(6, deserializedPacket.getbPktId());
        assertEquals(messageContent, deserializedPacket.getbMsg().getMessage());
        assertFalse(deserializedPacket.isValid());
    }

    @Test
    public void testEmptyMessage() {
        byte bSrc = 0x01;
        String messageContent = "";
        Message message = new Message(1, 1, messageContent);
        Packet packet = new Packet(message);

        byte[] packetBytes = packet.toBytes();
        Packet deserializedPacket = Packet.fromBytes(packetBytes);

        assertEquals(bSrc, deserializedPacket.getbSrc());
        assertEquals(8, deserializedPacket.getbPktId());
        assertEquals(messageContent, deserializedPacket.getbMsg().getMessage());
        assertFalse(deserializedPacket.isValid());
    }
    @Test
    public void testNonAsciiCharacters() {
        byte bSrc = 0x01;
        String messageContent = "Привіт, світ!";
        Message message = new Message(1, 1, messageContent);
        Packet packet = new Packet(message);

        byte[] packetBytes = packet.toBytes();
        Packet deserializedPacket = Packet.fromBytes(packetBytes);

        assertEquals(bSrc, deserializedPacket.getbSrc());
        assertEquals(9, deserializedPacket.getbPktId());
        assertEquals(messageContent, deserializedPacket.getbMsg().getMessage());
        assertFalse(deserializedPacket.isValid());
    }

    @Test
    public void testLongMessage() {
        byte bSrc = 0x01;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String messageContent = sb.toString();
        Message message = new Message(1, 1, messageContent);
        Packet packet = new Packet(message);

        byte[] packetBytes = packet.toBytes();
        Packet deserializedPacket = Packet.fromBytes(packetBytes);

        assertEquals(bSrc, deserializedPacket.getbSrc());
        assertEquals(messageContent, deserializedPacket.getbMsg().getMessage());
        assertFalse(deserializedPacket.isValid());
    }

    @Test
    public void testInvalidCRC() {
        byte bSrc = 0x01;
        long bPktId = 123456789L;
        String messageContent = "Test Message";
        Message message = new Message(1, 1, messageContent);
        Packet packet = new Packet(message);

        byte[] packetBytes = packet.toBytes();
        packetBytes[10] = 0x00; // Modifying the packet to create invalid CRC
        Packet deserializedPacket = Packet.fromBytes(packetBytes);

        assertFalse(deserializedPacket.isValid());
    }
}
