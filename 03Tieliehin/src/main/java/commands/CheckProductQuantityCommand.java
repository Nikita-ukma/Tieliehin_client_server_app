package commands;
import Store.Store;
import models.Product;

public class CheckProductQuantityCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        Product product = store.getProduct(args[0]);
        if (product != null) {
            System.out.println("Quantity of " + product.getName() + ": " + product.getQuantity());
        } else {
            System.out.println("models.Product not found, quantity == 0");
        }
    }
}
