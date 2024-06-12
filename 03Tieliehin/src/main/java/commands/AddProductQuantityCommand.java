package commands;
import models.Store;
import models.Product;

public class AddProductQuantityCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        String productName = args[0];
        int amount = Integer.parseInt(args[1]);
        if (store.getProduct(productName)== null) {
            store.addProduct(new Product(productName));
        }
        Product product = store.getProduct(productName);
            product.addQuantity(amount);
            System.out.println("Added " + amount + " to " + productName);
        }
    }
