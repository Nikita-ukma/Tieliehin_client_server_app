
import entity.Product;
import entity.ProductGroup;
import org.junit.jupiter.api.Test;
import server_work.Packet;
import server_work.PacketHelper;

import static org.junit.jupiter.api.Assertions.*;

public class PacketHelperTest {

    @Test
    public void testCreateProductPacket() {
        ProductGroup group = new ProductGroup(1, "Beverages", "Drinks and liquids");
        Product product = new Product(101, "Coca Cola", "Soda Drink", "Coca Cola Company", 100, 1.99, group);

        Packet packet = null;
        try {
            packet = PacketHelper.createProductPacket(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("101,Coca Cola,Soda Drink,Coca Cola Company,100,1.99,1", packet.getbMsg().getMessage());
        assertEquals(1, packet.getbMsg().getcType());
        assertEquals(1, packet.getbMsg().getbUserId());
    }

    @Test
    public void testUpdateProductPacket() {
        ProductGroup group = new ProductGroup(1, "Beverages", "Drinks and liquids");
        Product product = new Product(101, "Coca Cola", "Soda Drink", "Coca Cola Company", 100, 2.49, group);

        Packet packet = null;
        try {
            packet = PacketHelper.updateProductPacket(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("101,Coca Cola,Soda Drink,Coca Cola Company,100,2.49,1", packet.getbMsg().getMessage());
        assertEquals(3, packet.getbMsg().getcType());
        assertEquals(1, packet.getbMsg().getbUserId());
    }

    @Test
    public void testDeleteProductPacket() {
        Packet packet = null;
        try {
            packet = PacketHelper.deleteProductPacket(101);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("101", packet.getbMsg().getMessage());
        assertEquals(4, packet.getbMsg().getcType());
        assertEquals(1, packet.getbMsg().getbUserId());
    }
}
