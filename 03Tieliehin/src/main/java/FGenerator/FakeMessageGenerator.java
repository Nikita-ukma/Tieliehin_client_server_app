package FGenerator;

import models.Message;
import models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeMessageGenerator {
   public static void addProducts(List<Product> productList) {
        productList.add(new Product("Eggs"));
        productList.add(new Product("Chicken"));
        productList.add(new Product("Cheese"));
        productList.add(new Product("Tomato"));
        productList.add(new Product("Potato"));
       productList.add(new Product("Carrot"));
        productList.add(new Product("Apple"));
        productList.add(new Product("Banana"));
        productList.add(new Product("Orange"));
        productList.add(new Product("Milk"));
        productList.add(new Product("Bread"));
    }

    public static List<Message> generateRandomMessages() {
        List<Message> messages = new ArrayList<>();
        Random random = new Random();
        String[] productNames = {
                "Apple", "Banana", "Carrot", "Potato", "Chicken", "Beef", "Bread", "Milk", "Cheese", "Butter"
        };

        for (int i = 0; i < 20; i++) {
            int commandId = random.nextInt(6) + 1; // Command ID between 1 and 6
            String messageContent = productNames[random.nextInt(productNames.length)];
            if (commandId == 2 || commandId == 3) {
                int quantity = random.nextInt(100) + 1;
                messageContent += "," + quantity;
            } else if (commandId == 4) {
                double price = random.nextDouble() * 100;
                messageContent += "," + price;
            } else if (commandId == 5) {
                String groupName = "Group" + (random.nextInt(3) + 1); // Group names: Group1, Group2, Group3
                messageContent += "," + groupName;
            }

            messages.add(new Message(commandId, random.nextInt(1000) + 1, messageContent));
        }

        return messages;
    }

    public static Message generateRandomPacket(int id) {

        Random random = new Random();
        String[] productNames = {
                "Apple", "Banana", "Carrot", "Potato", "Chicken", "Beef", "Bread", "Milk", "Cheese", "Butter"
        };
            int commandId = random.nextInt(6) + 1; // Command ID between 1 and 6
            String messageContent = productNames[random.nextInt(productNames.length)];
            if (commandId == 2 || commandId == 3) {
                int quantity = random.nextInt(100) + 1;
                messageContent += "," + quantity;
            } else if (commandId == 4) {
                double price = random.nextDouble() * 100;
                messageContent += "," + price;
            } else if (commandId == 5) {
                String groupName = "Group" + (random.nextInt(3) + 1); // Group names: Group1, Group2, Group3
                messageContent += "," + groupName;
            }
            return new Message(commandId, id, messageContent);
    }
}

