package commands;

import models.Processor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            String sql = "SELECT * FROM Products WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(args[0]));
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Product ID: " + rs.getInt("id"));
                        System.out.println("Name: " + ((ResultSet) rs).getString("name"));
                        System.out.println("Description: " + rs.getString("description"));
                        System.out.println("Manufacturer: " + rs.getString("manufacturer"));
                        System.out.println("Quantity in stock: " + rs.getInt("quantity_in_stock"));
                        System.out.println("Price per unit: " + rs.getBigDecimal("price_per_unit"));
                        System.out.println("Group ID: " + rs.getInt("group_id"));
                    } else {
                        System.out.println("Product not found");
                    }
                }
            }
        }
    }
}
