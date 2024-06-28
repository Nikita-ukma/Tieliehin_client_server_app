package commands;

import com.google.gson.JsonObject;
import server_work.Processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadProductCommand implements Command {
    private JsonObject productJson;
    @Override
    public void execute(String[] args) throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            String sql = "SELECT * FROM Products WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(args[0]));
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        productJson = new JsonObject();
                        productJson.addProperty("id", rs.getInt("id"));
                        productJson.addProperty("name", rs.getString("name"));
                        productJson.addProperty("description", rs.getString("description"));
                        productJson.addProperty("manufacturer", rs.getString("manufacturer"));
                        productJson.addProperty("quantity", rs.getInt("quantity"));
                        productJson.addProperty("price", rs.getBigDecimal("price"));
                        productJson.addProperty("group_id", rs.getInt("group_id"));
                    } else {
                        throw new SQLException("Product not found");
                    }
                }
            }
        }
    }

    public JsonObject getProductJson() {
        return productJson;
    }
}