package commands;

import database_connection.ProductDB;
import server_work.Processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        Connection con = Processor.getConnection();
        String checkSql = "SELECT COUNT(*) FROM product WHERE product_name = ? AND description = ? AND manufacturer = ? AND amount = ? AND price = ? AND group_id = ?";
        String insertSql = "INSERT INTO product (id_product, product_name, description, manufacturer, amount, price, group_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
            checkStmt.setString(1, args[1]);
            checkStmt.setString(2, args[2]);
            checkStmt.setString(3, args[3]);
            checkStmt.setInt(4, Integer.parseInt(args[4]));
            checkStmt.setBigDecimal(5, new BigDecimal(args[5]));
            checkStmt.setInt(6, Integer.parseInt(args[6]));

            try (ResultSet rs = checkStmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                if (count > 0) {
                    System.out.println("Product with the same attributes already exists.");
                } else {
                    try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, Integer.parseInt(args[0]));
                        insertStmt.setString(2, args[1]);
                        insertStmt.setString(3, args[2]);
                        insertStmt.setString(4, args[3]);
                        insertStmt.setInt(5, Integer.parseInt(args[4]));
                        insertStmt.setBigDecimal(6, new BigDecimal(args[5]));
                        insertStmt.setInt(7, Integer.parseInt(args[6]));

                        insertStmt.executeUpdate();
                        System.out.println("Product added successfully.");
                    }
                }
            }
        }
    }
}