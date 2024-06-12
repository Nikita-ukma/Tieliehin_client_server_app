package commands;

import Store.Store;
import models.Product;

public class AddProductToGroupCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        String productName = args[0];
        String groupName = args[1];
        if (productName == null || groupName == null) System.out.println("unknown product or group");
        else {
            for (int i = 0; i<store.getProducts().size(); i++) {
                if(store.getProducts().get(i).getName().equals(productName)) store.getProducts().get(i).setGroup(groupName);
            }
            System.out.println("Added " + productName + " to group " + groupName);
        }
    }
}