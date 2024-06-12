package commands;
import models.Store;

public class AddProductGroupCommand implements Command {
    @Override
    public void execute(Store store, String[] args) {
        String groupName = args[0];
        store.addProductGroup(groupName);
        System.out.println("Added product group: " + groupName);
    }
}
