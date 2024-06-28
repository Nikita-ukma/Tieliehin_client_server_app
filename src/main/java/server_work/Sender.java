package server_work;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Sender {
    private final Encryptor encryptor;
    private final List<byte[]> encryptedPackets;

    public Sender(PublicKey publicKey) {
        this.encryptor = new Encryptor(publicKey);
        this.encryptedPackets = new ArrayList<>();
    }

    public byte[] send(Packet packet) throws Exception {
        if (!packet.isValid()) throw new IllegalArgumentException("Invalid message CRC16");
        byte[] encryptedMessage = encryptor.encrypt(packet.getbMsg());
        Packet encryptedPacket = new Packet(encryptedMessage);
        encryptedPackets.add(encryptedPacket.toBytes());
        return encryptedPacket.toBytes();
    }

    public byte[] encryptMessage(Message message) throws Exception {
        return encryptor.encrypt(message);
    }

    public List<byte[]> getEncryptedPackets() {
        return encryptedPackets;
    }
}