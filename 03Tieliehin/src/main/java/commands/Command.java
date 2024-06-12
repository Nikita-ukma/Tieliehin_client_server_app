package commands;
import models.Store;

public interface Command {
    void execute(Store store, String[] args);
}
