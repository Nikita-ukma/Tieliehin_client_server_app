package commands;

import commands.Command;
import models.Processor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            String sql = "UPDATE Products SET name = ?, description = ?, manufacturer = ?, quantity_in_stock = ?, price_per_unit = ?, group_id = ? WHERE name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, args[0]);
                pstmt.setString(2, args[1]);
                pstmt.setString(3, args[2]);
                pstmt.setInt(4, Integer.parseInt(args[3]));
                pstmt.setBigDecimal(5, new BigDecimal(args[4]));
                pstmt.setInt(6, Integer.parseInt(args[5]));
                pstmt.setString(7, (args[6]));
                pstmt.executeUpdate();
            }
        }
    }
}