package models;
import commands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    public void processPackets(List<Packet> packets) {
        for (Packet packet : packets) {
            executorService.submit(() -> {
                try {
                    processPacket(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public void processPacket(Packet packet) throws IOException {
        int commandId = packet.getbMsg().getcType();
        if (commandId>6) throw new IOException("Invalid command type");
        String[] args = packet.getbMsg().getMessage().split(",");
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
        System.out.println("models.Processor server stopped");
    }
}