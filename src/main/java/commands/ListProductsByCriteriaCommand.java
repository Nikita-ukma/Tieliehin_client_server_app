package commands;

import server_work.Processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListProductsByCriteriaCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Insufficient criteria provided.");
        }

        String attribute = args[0];
        String operator = args[1];
        String value = args[2];
        String sql = "SELECT * FROM Products WHERE " + attribute + " " + operator + " ?";

        try (Connection connection = Processor.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                switch (attribute) {
                    case "name":
                    case "description":
                    case "manufacturer":
                        pstmt.setString(1, value);
                        break;
                    case "quantity":
                    case "group_id":
                        pstmt.setInt(1, Integer.parseInt(value));
                        break;
                    case "price":
                        pstmt.setBigDecimal(1, new BigDecimal(value));
                        break;
                    case "id":
                        pstmt.setInt(1, Integer.parseInt(value));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid attribute provided.");
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean found = false;
                    System.out.println("Result list: (criteria: " + args[0] +" "+args[1]+" " +args[2] + ")");
                    while (rs.next()) {
                        found = true;;
                        System.out.println(rs.getInt("id")+"    |"+ rs.getString("name")+"|"+ rs.getString("description")
                   +"|"     + rs.getString("manufacturer")+"|"+ rs.getInt("quantity")
                     + "|"  + rs.getBigDecimal("price")+"|"+ rs.getInt("group_id"));
                    }
                    if (!found) {
                        System.out.println("No products found matching the criteria.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

