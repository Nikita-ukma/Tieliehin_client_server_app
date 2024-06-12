package commands;
import models.Store;
import models.Product;

public class SetProductPriceCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        String productName = args[0];
        double price = Double.parseDouble(args[1]);
        Product product = store.getProduct(productName);
        if (product != null) {
            product.setPrice(price);
            System.out.println("Set price of " + productName + " to " + price);
        } else {
            System.out.println("models.Product not found: " + productName);
        }
    }
}
