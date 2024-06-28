package commands;

import server_work.Processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            String sql = "UPDATE product SET product_name = ?, description = ?, manufacturer = ?, amount = ?, price = ?, group_id = ? WHERE id_product = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, args[1]);
                pstmt.setString(2, args[2]);
                pstmt.setString(3, args[3]);
                pstmt.setInt(4, Integer.parseInt(args[4]));
                pstmt.setBigDecimal(5, new BigDecimal(args[5]));
                pstmt.setInt(6, Integer.parseInt(args[6]));
                pstmt.setInt(7, Integer.parseInt(args[0]));
                pstmt.executeUpdate();
                System.out.println("Update successfully");
            }
        }
    }
}