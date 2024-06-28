package commands;

import server_work.Processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteProductCommand implements Command {
    @Override
    public void execute(String[] args) throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            String checkSql;
            String deleteSql;

            if (isNumeric(args[0])) {
                checkSql = "SELECT COUNT(*) FROM product WHERE id_product = ?";
                deleteSql = "DELETE FROM product WHERE id_product = ?";
            } else {
                // Видалення за ім'ям
                checkSql = "SELECT COUNT(*) FROM product WHERE product_name = ?";
                deleteSql = "DELETE FROM product WHERE product_name = ?";
            }

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                setPreparedStatementParameter(checkStmt, args[0]);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);

                    if (count > 0) {
                        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                            setPreparedStatementParameter(deleteStmt, args[0]);
                            deleteStmt.executeUpdate();
                            System.out.println("Product deleted successfully.");
                        }
                    } else {
                        System.out.println("Product not found. Deletion not possible.");
                    }
                }
            }
        }
    }

    private void setPreparedStatementParameter(PreparedStatement stmt, String param) throws SQLException {
        if (isNumeric(param)) {
            stmt.setInt(1, Integer.parseInt(param));
        } else {
            stmt.setString(1, param);
        }
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}