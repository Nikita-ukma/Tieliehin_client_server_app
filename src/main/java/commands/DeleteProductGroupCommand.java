package commands;

import server_work.Processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteProductGroupCommand implements Command {
    private static final String DELETE = "DELETE FROM ProductGroup WHERE group_id=?;";

    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        try (PreparedStatement statement = con.prepareStatement(DELETE)) {
            statement.setInt(1, Integer.parseInt(args[0]));
            statement.executeUpdate();
            System.out.println("Product group deleted successfully.");
        }
    }
}
