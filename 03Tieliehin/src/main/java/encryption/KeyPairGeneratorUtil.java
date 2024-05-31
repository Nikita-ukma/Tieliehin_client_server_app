package encryption;

import models.Product;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class KeyPairGeneratorUtil {
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static class Inventory {
        private final Map<Product, Integer> stock;
        private final Map<Product, Double> prices;

        public Inventory() {
            this.stock = new HashMap<>();
            this.prices = new HashMap<>();
        }

        public int getStock(Product itemName) {
            return stock.getOrDefault(itemName, 0);
        }

        public void addStock(Product itemName, int quantity) {
            stock.put(itemName, getStock(itemName) + quantity);
        }

        public void removeStock(Product itemName, int quantity) {
            stock.put(itemName, getStock(itemName) - quantity);
        }

        public void setPrice(Product itemName, double price) {
            prices.put(itemName, price);
        }

        public double getPrice(Product itemName) {
            return prices.getOrDefault(itemName, 0.0);
        }
    }
}