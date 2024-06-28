import server_work.CRC16;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CRC16Test {

    @Test
    public void testEmptyArray() {
        byte[] data = {};
        int expectedCRC = 0x0000;
        int actualCRC = CRC16.computeCRC(data);
        assertEquals(expectedCRC, actualCRC);
    }

    @Test
    public void testByte() {
        byte[] data = {0x01};
        int expectedCRC = 0xC0C1;
        int actualCRC = CRC16.computeCRC(data);
        assertEquals(expectedCRC, actualCRC);
    }
}
