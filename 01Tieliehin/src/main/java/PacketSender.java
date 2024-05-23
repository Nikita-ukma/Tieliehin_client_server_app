import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class PacketSender {
    private static List<Packet> encryptedPackets;
    private static Key key;
    public PacketSender() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        key = keygen.generateKey();
        encryptedPackets = new ArrayList<>();
    }
    public static Packet send(byte[] bytePacket) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Packet myPack = Packet.fromBytes(bytePacket);
        if(!myPack.isValid()) throw new IllegalArgumentException("Invalid message CRC16");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(myPack.getbMsg().toBytes());
        Packet encPacket = new Packet(myPack.getbSrc(), myPack.getbSrc(), encrypted);
        encryptedPackets.add(encPacket);
        return encPacket;
    }
    public List<Packet> getEncryptedPackets() {
        return new ArrayList<>(encryptedPackets);
    }
    public byte[] getKey() {
        return key.getEncoded();
    }
}
