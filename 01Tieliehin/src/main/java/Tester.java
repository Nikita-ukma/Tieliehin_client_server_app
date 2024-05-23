
import java.util.List;

public class Tester {
    public static void main(String[] args) {
        try {
            // Create three messages
            Message message1 = new Message(1, 1001, "Hello, this is message 1");
            Message message2 = new Message(2, 1002, "Hello, this is message 2");
            Message message3 = new Message(3, 1003, "Hello, this is message 3");

            // Convert messages to packets
            Packet packet1 = new Packet((byte) 1, 1L, message1);
            Packet packet2 = new Packet((byte) 2, 2L, message2);
            Packet packet3 = new Packet((byte) 3, 3L, message3);

            // Create PacketSender and PacketReceiver
            PacketSender sender = new PacketSender();
            PacketReceiver receiver = new PacketReceiver(sender.getKey());

            // Send and receive the packets
            Packet encryptedPacket1 = PacketSender.send(packet1.toBytes());
            Packet encryptedPacket2 = PacketSender.send(packet2.toBytes());
            Packet encryptedPacket3 = PacketSender.send(packet3.toBytes());

            Packet decryptedPacket1 = receiver.receive(encryptedPacket1.toBytes());
            Packet decryptedPacket2 = receiver.receive(encryptedPacket2.toBytes());
            Packet decryptedPacket3 = receiver.receive(encryptedPacket3.toBytes());

            Packet.printPacketDetails(decryptedPacket1);
            Packet.printPacketDetails(decryptedPacket2);
            Packet.printPacketDetails(decryptedPacket3);
            //In the end let test memory:
            List<Packet> encryptedPackets = sender.getEncryptedPackets();
            System.out.println("Encrypted Packets:");
            for (Packet packet : encryptedPackets) {
                Packet.printPacketDetails(packet);
            }

            // Check decrypted packets in PacketReceiver
            List<Packet> decryptedPackets = receiver.getDecryptedPackets();
            System.out.println("Decrypted Packets:");
            for (Packet packet : decryptedPackets) {
                Message.printMessageDetails(packet.getbMsg());
            }

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}