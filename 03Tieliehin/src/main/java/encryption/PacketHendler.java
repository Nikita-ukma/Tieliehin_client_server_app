package encryption;

import models.Packet;

import java.io.IOException;
import java.sql.SQLException;

public interface PacketHendler {
    void handle(Packet packet) throws IOException, SQLException;
}