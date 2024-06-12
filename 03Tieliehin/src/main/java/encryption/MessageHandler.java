package encryption;
import models.Message;

public interface MessageHandler {
    void handle(Message message);
}