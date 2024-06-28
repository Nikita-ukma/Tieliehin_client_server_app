package commands;

import server_work.Processor;
import entity.ProductGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProductGroupCommand implements Command {
    private static final String UPDATE = "UPDATE ProductGroup SET group_name=?, description=? WHERE group_id=?;";

    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        try (PreparedStatement statement = con.prepareStatement(UPDATE)) {
            statement.setString(1, args[1]);
            statement.setString(2, args[2]);
            statement.setInt(3, Integer.parseInt(args[0]));
            statement.executeUpdate();
            System.out.println("Product group updated successfully.");
        }
    }
}