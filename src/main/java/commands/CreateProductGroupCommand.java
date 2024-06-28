package commands;
import server_work.Processor;
import entity.ProductGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateProductGroupCommand implements Command {
    private static final String CREATE = "INSERT INTO ProductGroup (group_id, group_name, description) VALUES (?, ?, ?);";

    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        try (PreparedStatement statement = con.prepareStatement(CREATE)) {
            statement.setInt(1, Integer.parseInt(args[0]));
            statement.setString(2, args[1]);
            statement.setString(3, args[2]);
            statement.executeUpdate();
            System.out.println("Product group created successfully.");
        }
    }
}