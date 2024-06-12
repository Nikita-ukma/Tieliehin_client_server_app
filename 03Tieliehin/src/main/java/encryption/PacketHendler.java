package encryption;

import models.Packet;

import java.io.IOException;

public interface PacketHendler {
    void handle(Packet packet) throws IOException;
}