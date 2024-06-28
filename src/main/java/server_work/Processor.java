package server_work;

import commands.*;
import database_connection.ProductDB;
import database_connection.ProductGroupDB;

import java.io.IOException;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Processor {
    private final Map<Integer, Command> commandMap;
    private final ExecutorService executorService;
    private final Decryptor decryptor;

    public Processor(PrivateKey privateKey, int threadPoolSize) {
        this.commandMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.decryptor = new Decryptor(privateKey, threadPoolSize);
        registerCommands();
    }

    private void registerCommands() {
        commandMap.put(1, new CreateProductCommand());
        commandMap.put(2, new ReadProductCommand());
        commandMap.put(3, new UpdateProductCommand());
        commandMap.put(4, new DeleteProductCommand());
        commandMap.put(5, new ListProductsByCriteriaCommand());
        commandMap.put(6, new CreateProductGroupCommand());
        commandMap.put(7, new ReadProductGroupCommand());
        commandMap.put(8, new UpdateProductGroupCommand());
        commandMap.put(9, new DeleteProductGroupCommand());
    }

    public void processEncryptedPackets(List<byte[]> encryptedPackets) {
        for (byte[] encryptedPacket : encryptedPackets) {
            executorService.submit(() -> {
                try {
                    processEncryptedPacket(encryptedPacket);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public void processPackets(List<byte[]> encryptedPackets) {
        for (byte[] encryptedPacket : encryptedPackets) {
            executorService.submit(() -> {
                try {
                    processNoCryptPacket(encryptedPacket);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    private void processNoCryptPacket(byte[] encryptedPacket) throws Exception {
        Packet packet = Packet.fromBytes(encryptedPacket);
        processPacket(packet);
    }

    private void processEncryptedPacket(byte[] bytePacket) throws Exception {
        byte[] encrypted = new byte[bytePacket.length - 18];
        System.arraycopy(bytePacket, 16, encrypted, 0, encrypted.length);
        byte[] decrypted = decryptor.decrypt(encrypted).toBytes();
        Packet decryptedPacket = new Packet(Message.fromBytes(decrypted));
        if (decryptedPacket.isValid()) throw new IllegalArgumentException("Invalid message CRC16");
        processPacket(decryptedPacket);
    }

    public void processPacket(Packet packet) throws IOException, SQLException {
        int commandId = packet.getbMsg().getcType();
        if (commandId < 1 || commandId > 9) throw new IOException("Invalid command type");
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
            getConnection();
        }
    }

    public void shutdown() {
        executorService.shutdown();
        decryptor.shutdown();
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
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/StoreDB",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException | SQLException ignored) {

        }
        ProductGroupDB.setConnection(connection);
        ProductDB.setConnection(connection);
        return connection;
    }
}