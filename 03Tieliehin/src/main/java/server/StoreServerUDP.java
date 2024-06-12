package server;

import models.Processor;
import models.Receiver;
import models.Store;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class StoreServerUDP extends Thread {
    private static DatagramSocket socket;
    private final byte[] buf = new byte[274];
    private final KeyPair keyPair;
    private final Receiver receiver;
    private final ExecutorService executorService;
    private final LinkedBlockingQueue<DatagramPacket> packetCache;
    private static final int CACHE_SIZE = 50;
    private volatile boolean running = true;

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public StoreServerUDP() throws SocketException, NoSuchAlgorithmException {
        socket = new DatagramSocket(4445);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
        Store store = new Store();
        Processor processor = new Processor(store);
        receiver = new Receiver(keyPair.getPrivate(), 4, processor);
        executorService = Executors.newFixedThreadPool(10);
        packetCache = new LinkedBlockingQueue<>(CACHE_SIZE);
    }

    public void run() {
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                executorService.submit(() -> handlePacket(packet));
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
        executorService.shutdown();
    }

    private void handlePacket(DatagramPacket packet) {
        try {
            byte[] received = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, received, 0, packet.getLength());
            receiver.receive(received);

            // Add packet to cache
            if (packetCache.size() >= CACHE_SIZE) {
                packetCache.poll(); // Remove oldest packet if cache is full
            }
            packetCache.offer(packet);

            String responseMessage = "ACK"; // Підтвердження отримання повідомлення
            byte[] response = responseMessage.getBytes();

            DatagramPacket responsePacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
            socket.send(responsePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        socket.close();
    }

    public LinkedBlockingQueue<DatagramPacket> getPacketCache() {
        return packetCache;
    }
}