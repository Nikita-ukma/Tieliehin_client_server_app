package commands;
import Store.Store;
import models.Product;

public class RemoveProductQuantityCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        String productName = args[0];
        String amount = args[1];
        Product product = store.getProduct(productName);
        if (product != null) {
            try {
                product.removeQuantity(Integer.parseInt(amount));
                System.out.println("Removed " + amount + " from " + productName);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("models.Product not found, we can't remove " + productName);
        }
    }
}
