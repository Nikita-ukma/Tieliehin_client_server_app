package server_work;

import entity.Product;
import entity.ProductGroup;

import java.security.PublicKey;
import java.util.List;

public class PacketHelper {
    private static final PublicKey publicKey = StoreServerTCP.getPublicKey();
    private static final Sender sender = new Sender(publicKey);

    public static Packet createProductPacket(Product product) throws Exception {
        String sb = product.getId() + "," +
                product.getName() + "," +
                product.getDescription() + "," +
                product.getManufacturer() + "," +
                product.getAmount() + "," +
                product.getPrice() + "," +
                product.getProductGroup().getId();
        byte[] encryptedMessage = sender.encryptMessage(new Message(1, 1, sb));
        return new Packet(encryptedMessage);
    }

    public static Packet readProductPacket(int id) throws Exception {
        String str = String.valueOf(id);
        byte[] encryptedMessage = sender.encryptMessage(new Message(2, 1, str));
        return new Packet(encryptedMessage);
    }

    public static Packet updateProductPacket(Product product) throws Exception {
        String sb = product.getId() + "," +
                product.getName() + "," +
                product.getDescription() + "," +
                product.getManufacturer() + "," +
                product.getAmount() + "," +
                product.getPrice() + "," +
                product.getProductGroup().getId();
        byte[] encryptedMessage = sender.encryptMessage(new Message(3, 1, sb));
        return new Packet(encryptedMessage);
    }

    public static Packet deleteProductPacket(int id) throws Exception {
        String str = String.valueOf(id);
        byte[] encryptedMessage = sender.encryptMessage(new Message(4, 1, str));
        return new Packet(encryptedMessage);
    }

    public static Packet listProductsByCriteriaPacket(String criteria) throws Exception {
        byte[] encryptedMessage = sender.encryptMessage(new Message(5, 1, criteria));
        return new Packet(encryptedMessage);
    }

    // ProductGroup commands
    public static Packet createProductGroupPacket(ProductGroup productGroup) throws Exception {
        String sb = productGroup.getId() + "," +
                productGroup.getName() + "," +
                productGroup.getDescription();
        byte[] encryptedMessage = sender.encryptMessage(new Message(6, 1, sb));
        return new Packet(encryptedMessage);
    }

    public static Packet readProductGroupPacket(int id) throws Exception {
        String str = String.valueOf(id);
        byte[] encryptedMessage = sender.encryptMessage(new Message(7, 1, str));
        return new Packet(encryptedMessage);
    }

    public static Packet updateProductGroupPacket(ProductGroup productGroup) throws Exception {
        String sb = productGroup.getId() + "," +
                productGroup.getName() + "," +
                productGroup.getDescription();
        byte[] encryptedMessage = sender.encryptMessage(new Message(8, 1, sb));
        return new Packet(encryptedMessage);
    }

    public static Packet deleteProductGroupPacket(int id) throws Exception {
        String str = String.valueOf(id);
        byte[] encryptedMessage = sender.encryptMessage(new Message(9, 1, str));
        return new Packet(encryptedMessage);
    }
}
