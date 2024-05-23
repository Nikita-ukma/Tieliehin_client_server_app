import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class Packet {
    private static final byte MAGIC_BYTE = 0x13;

    private byte bMagic;
    private final byte bSrc;
    private final long bPktId;
    private final int wLen;
    private int wCrc16Header;
    private final byte[] bMsg;
    private int wCrc16Msg;

    public Packet(byte bSrc, long bPktId, byte[] bMsg) {
        this.bMagic = MAGIC_BYTE;
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsg = bMsg;
        this.wLen = bMsg.length+18;
        this.wCrc16Header = computeCRC16Header();
        this.wCrc16Msg = computeCRC16Msg();
    }
    public Packet(byte bSrc, long bPktId, Message bMsg) {
        this.bMagic = MAGIC_BYTE;
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsg = bMsg.toBytes();
        this.wLen = bMsg.toBytes().length+18;
        this.wCrc16Header = computeCRC16Header();
        this.wCrc16Msg = computeCRC16Msg();
    }

    private int computeCRC16Header() {
        ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bMagic);
        buffer.put(bSrc);
        buffer.putLong(bPktId);
        buffer.putInt(wLen);
        return CRC16.computeCRC(buffer.array());
    }

    private int computeCRC16Msg() {
        return CRC16.computeCRC(bMsg);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(wLen);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bMagic);
        buffer.put(bSrc);
        buffer.putLong(bPktId);
        buffer.putInt(wLen);
        buffer.putShort((short) wCrc16Header);
        buffer.put(bMsg);
        buffer.putShort((short) wCrc16Msg);
        return buffer.array();
    }
    public static Packet fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        byte bMagic = buffer.get();
        byte bSrc = buffer.get();
        long bPktId = buffer.getLong();
        int wLen = buffer.getInt();
        int wCrc16Header = buffer.getShort() & 0xFFFF;
        byte[] bMsg = new byte[wLen-18];
        buffer.get(bMsg);
        int wCrc16Msg = buffer.getShort() & 0xFFFF;
        Packet packet = new Packet(bSrc, bPktId, bMsg);
        packet.bMagic = bMagic;
        packet.wCrc16Header = wCrc16Header;
        packet.wCrc16Msg = wCrc16Msg;
        return packet;
    }

    public static void printPacketDetails(Packet packet) {
        System.out.println("Packet Details:");
        System.out.println("bMagic: " + packet.getbMagic());
        System.out.println("bSrc: " + packet.getbSrc());
        System.out.println("bPktId: " + packet.getbPktId());
        System.out.println("wLen: " + packet.getwLen());
        System.out.println("wCrc16Header: " + packet.getwCrc16Header());
        System.out.println("wCrc16Msg: " + packet.getwCrc16Msg());
        System.out.println("Message: " + packet.getbMsg().getMessage());
        System.out.println("Command Type: " + packet.getbMsg().getcType());
        System.out.println("User ID: " + packet.getbMsg().getbUserId());
        System.out.println();
    }
    public boolean isValid() {
        return wCrc16Header == computeCRC16Header() && wCrc16Msg == computeCRC16Msg();
    }

    public byte getbMagic() {
        return bMagic;
    }

    public byte getbSrc() {
        return bSrc;
    }

    public long getbPktId() {
        return bPktId;
    }

    public int getwLen() {
        return wLen;
    }

    public int getwCrc16Header() {
        return wCrc16Header;
    }

    public Message getbMsg() {
        return Message.fromBytes(bMsg);
    }

    public int getwCrc16Msg() {
        return wCrc16Msg;
    }
}