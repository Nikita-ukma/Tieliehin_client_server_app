import java.util.List;
import java.util.concurrent.*;
import java.util.Map;
import java.util.HashMap;
import Store.Store;
import commands.*;
import models.Message;

public class Processor {
    private final Store store;
    private final Map<Integer, Command> commandMap;
    private final ExecutorService executorService;

    public Processor(Store store) {
        this.store = store;
        this.commandMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(8);
        registerCommands();
    }

    private void registerCommands() {
        commandMap.put(1, new CheckProductQuantityCommand());
        commandMap.put(2, new RemoveProductQuantityCommand());
        commandMap.put(3, new AddProductQuantityCommand());
        commandMap.put(4, new SetProductPriceCommand());
        commandMap.put(5, new AddProductToGroupCommand());
        commandMap.put(6, new AddProductGroupCommand());
    }
    public void processMessages(List<Message> messages) {
        for (Message message : messages) {
            executorService.submit(() -> processMessage(message));
        }
    }
    public void processMessage(Message message) {
        int commandId = message.getcType();
        String[] args = message.getMessage().split(",");
        Command command = commandMap.get(commandId);
        if (command != null) {
            command.execute(store, args);
        } else {
            System.out.println("Unknown command ID: " + commandId);
        }
    }
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        System.out.println("Processor server stopped");
    }
}