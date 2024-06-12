package models;

import encryption.Decryptor;
import encryption.PacketHendler;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class Receiver implements PacketHendler{
    private final Decryptor decryptor;
    private final List<Packet> decryptedPackets;
    private final Processor processor;

    public Receiver(PrivateKey privateKey, int threadPoolSize, Processor processor) {
        this.decryptor = new Decryptor(privateKey, threadPoolSize);
        this.decryptedPackets = new ArrayList<>();
        this.processor = processor;
    }

    public void receive(List<byte[]> encryptedPackets) throws Exception {
        for (byte[] encryptedPacket : encryptedPackets) {
            Packet packet = Packet.fromBytes(encryptedPacket);
            receive(packet.toBytes());
        }
    }

    public void receive(byte[] bytePacket) throws Exception {
        byte[] encrypted = new byte[bytePacket.length - 18];
        System.arraycopy(bytePacket, 16, encrypted, 0, encrypted.length);
        byte[] decrypted = decryptor.decrypt(encrypted).toBytes();
        Packet decryptedPacket = new Packet(Message.fromBytes(decrypted));
        if(decryptedPacket.isValid()) throw new IllegalArgumentException("Invalid message CRC16");
        this.decryptedPackets.add(decryptedPacket);
        handle(decryptedPacket);
    }
@Override
    public void handle(Packet packet) throws IOException {
        processor.processPacket(packet);
    }

    public List<Packet> getDecryptedPackets() {
        return decryptedPackets;
    }
}