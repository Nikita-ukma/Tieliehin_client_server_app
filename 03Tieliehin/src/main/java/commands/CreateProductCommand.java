package commands;

import models.Processor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        String checkSql = "SELECT COUNT(*) FROM Products WHERE name = ? AND description = ? AND manufacturer = ? AND quantity_in_stock = ? AND price_per_unit = ? AND group_id = ?";
        String insertSql = "INSERT INTO Products (name, description, manufacturer, quantity_in_stock, price_per_unit, group_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
            checkStmt.setString(1, args[0]);
            checkStmt.setString(2, args[1]);
            checkStmt.setString(3, args[2]);
            checkStmt.setInt(4, Integer.parseInt(args[3]));
            checkStmt.setBigDecimal(5, new BigDecimal(args[4]));
            checkStmt.setInt(6, Integer.parseInt(args[5]));

            try (ResultSet rs = checkStmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                if (count > 0) {
                    System.out.println("Product with the same attributes already exists.");
                } else {
                    try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                        insertStmt.setString(1, args[0]);
                        insertStmt.setString(2, args[1]);
                        insertStmt.setString(3, args[2]);
                        insertStmt.setInt(4, Integer.parseInt(args[3]));
                        insertStmt.setBigDecimal(5, new BigDecimal(args[4]));
                        insertStmt.setInt(6, Integer.parseInt(args[5]));

                        insertStmt.executeUpdate();
                        System.out.println("Product added successfully.");
                    }
                }
            }
        }
    }
}