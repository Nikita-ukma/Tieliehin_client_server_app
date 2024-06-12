import models.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @Test
    public void testMessageToBytesAndBack() {
        int cType = 1234;
        int bUserId = 5678;
        String messageContent = "Hello, world!";

        Message message = new Message(cType, bUserId, messageContent);
        byte[] messageBytes = message.toBytes();
        Message deserializedMessage = Message.fromBytes(messageBytes);

        assertEquals(cType, deserializedMessage.getcType());
        assertEquals(bUserId, deserializedMessage.getbUserId());
        assertEquals(messageContent, deserializedMessage.getMessage());
    }

    @Test
    public void testEmptyMessage() {
        int cType = 1234;
        int bUserId = 5678;
        String messageContent = "";

        Message message = new Message(cType, bUserId, messageContent);
        byte[] messageBytes = message.toBytes();
        Message deserializedMessage = Message.fromBytes(messageBytes);

        assertEquals(cType, deserializedMessage.getcType());
        assertEquals(bUserId, deserializedMessage.getbUserId());
        assertEquals(messageContent, deserializedMessage.getMessage());
    }

    @Test
    public void testNonAsciiCharacters() {
        int cType = 1234;
        int bUserId = 5678;
        String messageContent = "Привіт, світ!";

        Message message = new Message(cType, bUserId, messageContent);
        byte[] messageBytes = message.toBytes();
        Message deserializedMessage = Message.fromBytes(messageBytes);

        assertEquals(cType, deserializedMessage.getcType());
        assertEquals(bUserId, deserializedMessage.getbUserId());
        assertEquals(messageContent, deserializedMessage.getMessage());
    }

    @Test
    public void testLongMessage() {
        int cType = 1234;
        int bUserId = 5678;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String messageContent = sb.toString();

        Message message = new Message(cType, bUserId, messageContent);
        byte[] messageBytes = message.toBytes();
        Message deserializedMessage = Message.fromBytes(messageBytes);

        assertEquals(cType, deserializedMessage.getcType());
        assertEquals(bUserId, deserializedMessage.getbUserId());
        assertEquals(messageContent, deserializedMessage.getMessage());
    }
    }