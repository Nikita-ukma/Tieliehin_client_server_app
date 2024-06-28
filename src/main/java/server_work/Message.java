package server_work;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Message {
    private final int cType;
    private final int bUserId;
    private final byte[] message;

    public Message(int cType, int bUserId, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] toBytes() {
        int length = 8 + message.length;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(cType);
        buffer.putInt(bUserId);
        buffer.put(message);
        return buffer.array();
    }

    public static Message fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        int cType = buffer.getInt();
        int bUserId = buffer.getInt();
        byte[] messageBytes = new byte[buffer.remaining()];
        buffer.get(messageBytes);
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        return new Message(cType, bUserId, message);
    }
    public int getcType() {
        return cType;
    }

    public int getbUserId() {
        return bUserId;
    }

    public String getMessage() {
        return new String(message, StandardCharsets.UTF_8);
    }
}