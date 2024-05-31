package commands;
import Store.Store;

public interface Command {
    void execute(Store store, String[] args);
}
