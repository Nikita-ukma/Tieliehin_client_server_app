package commands;

import server_work.Processor;
import entity.ProductGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadProductGroupCommand implements Command {
    private static final String GET_BY_ID = "SELECT * FROM ProductGroup WHERE group_id=?;";

    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        try (PreparedStatement statement = con.prepareStatement(GET_BY_ID)) {
            statement.setInt(1, Integer.parseInt(args[0]));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ProductGroup productGroup = new ProductGroup(
                        resultSet.getInt("group_id"),
                        resultSet.getString("group_name"),
                        resultSet.getString("description")
                );
                System.out.println("Product group found: " + productGroup);
            } else {
                System.out.println("Product group not found.");
            }
        }
    }
}
