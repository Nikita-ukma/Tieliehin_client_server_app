package models;

import commands.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Processor {
    private final Map<Integer, Command> commandMap;
    private final ExecutorService executorService;
    static Connection con;

    public Processor() {
        this.commandMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(8);
        registerCommands();
    }

    private void registerCommands() {
        commandMap.put(1, new CreateProductCommand());
        commandMap.put(2, new ReadProductCommand());
        commandMap.put(3, new UpdateProductCommand());
        commandMap.put(4, new DeleteProductCommand());
        commandMap.put(5, new ListProductsByCriteriaCommand());
        commandMap.put(6, new DeleteAllProductsCommand());
    }

    public void processPackets(List<Packet> packets) {
        for (Packet packet : packets) {
            executorService.submit(() -> {
                try {
                    processPacket(packet);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void processPacket(Packet packet) throws IOException, SQLException {
        int commandId = packet.getbMsg().getcType();
        if (commandId > 6) throw new IOException("Invalid command type");
        if (commandId == 5) {
            String check = packet.getbMsg().getMessage();
            if (!check.contains(" ")) throw new IOException("Invalid criteria! Input spaces!");
            if (!check.contains("<") && !check.contains("=") && !check.contains(">"))
                throw new IOException("Invalid criteria! Input = or < or >!");
            String[] args = packet.getbMsg().getMessage().split(" ");
            Command command = commandMap.get(commandId);
            command.execute(args);
        } else {
            String[] args = packet.getbMsg().getMessage().split(",");
            Command command = commandMap.get(commandId);
            command.execute(args);
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

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + "StoreDB");
            PreparedStatement st = con.prepareStatement("create table if not exists 'test' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text);");
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
        System.out.println("All bad");
        return con;
    }
}