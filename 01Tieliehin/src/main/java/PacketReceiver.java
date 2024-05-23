import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class PacketReceiver {
    private final Key key;
    private final List<Packet> decryptedPackets;

    public PacketReceiver(byte[] keyBytes) {
        this.key = new SecretKeySpec(keyBytes, "AES");
        this.decryptedPackets = new ArrayList<>();
    }
    public Packet receive(byte[] bytePacket) throws Exception {
        Packet myPack = Packet.fromBytes(bytePacket);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encrypted = new byte[bytePacket.length-18];
        System.arraycopy(bytePacket, 16, encrypted, 0, encrypted.length);
        byte[] decrypted = cipher.doFinal(encrypted);
        Packet decryptedMessage = new Packet(myPack.getbSrc(), myPack.getbPktId(), decrypted);
        this.decryptedPackets.add(decryptedMessage);
        return decryptedMessage;
    }
    public List<Packet> getDecryptedPackets() {
        return new ArrayList<>(decryptedPackets);
    }
}