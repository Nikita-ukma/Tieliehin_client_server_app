package client;

import models.Message;
import models.Packet;
import models.Sender;

import java.net.*;
import java.security.PublicKey;

public class StoreClientUDP {
    private DatagramSocket socket;
    private InetAddress address;
    private final int id;
    private static int freeId = 0;
    private byte[] buf;
    private PublicKey key;
    private static final int TIMEOUT = 2000; // Тайм-аут у мілісекундах
    private static final int MAX_RETRIES = 5; // Максимальна кількість повторних спроб

    public StoreClientUDP() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        this.id = freeId++;
        socket.setSoTimeout(TIMEOUT); // Встановлення тайм-ауту для очікування відповіді
    }

    public void setPublicKey(PublicKey mykey) {
        key = mykey;
    }

    public String send(String product, int cType) throws Exception {
        if (key == null) throw new Exception("Encryption key is null!");
        Sender sender = new Sender(key);
        Message message = new Message(cType, id, product);
        Packet myPacket = new Packet(message);
        buf = sender.send(myPacket);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        //  int retries = 0;
        //while (retries < MAX_RETRIES) {
        try {
            socket.send(packet);
            byte[] responseBuf = new byte[274];
            DatagramPacket responsePacket = new DatagramPacket(responseBuf, responseBuf.length);
            socket.receive(responsePacket);

            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            if (response.equals("ACK")) {
                return new String(product + " " + "received");
            }
        } catch (SocketTimeoutException e) {
            //    retries++;
          //  System.out.println("No response from server, retrying... (" + retries + "/" + MAX_RETRIES + ")");
        }
        return product;
    }
       // throw new Exception("Failed to receive acknowledgment from server after " + MAX_RETRIES + " attempts");

    public void startConnection(String ip, int port) throws Exception {
        socket = new DatagramSocket();
        address = InetAddress.getByName(ip);
    }
    public void close() {
        socket.close();
    }

}
