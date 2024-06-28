package commands;
import java.sql.SQLException;

public interface Command {
    void execute(String[] args) throws SQLException;
}
