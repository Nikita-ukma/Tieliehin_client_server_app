import commands.CreateProductCommand;
import commands.DeleteProductCommand;
import commands.UpdateProductCommand;
import server_work.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CRUDoperationsTest {
    @BeforeEach
    public void setupDatabase() throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
            }
        }
    }

    @Test
    public void createProductTest() throws SQLException {
        String[] args = {"1111","Milked", "Milk Description", "Manufacturer A", "100", "1.99", "5"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(args);

        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE product_name = 'Milked'")) {
            assertTrue(rs.next(), "Product should be created.");
            assertEquals("Milked", rs.getString("product_name"));
            assertEquals("Milk Description", rs.getString("description"));
            assertEquals("Manufacturer A", rs.getString("manufacturer"));
            assertEquals(100, rs.getInt("amount"));
            assertEquals(new BigDecimal("1.99"), rs.getBigDecimal("price"));
            assertEquals(5, rs.getInt("group_id"));
        }
    }
    @Test
    public void updateProductTest() throws SQLException {

        String[] updateArgs = {"1111","Updated Milk", "Updated Description", "Updated Manufacturer", "200", "2.99", "2"};
        UpdateProductCommand updateCommand = new UpdateProductCommand();
        updateCommand.execute(updateArgs);
        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE product_name = 'Updated Milk'")) {
            assertTrue(rs.next(), "Product should exist after update.");
            assertEquals("Updated Milk", rs.getString("product_name"));
            assertEquals("Updated Description", rs.getString("description"));
            assertEquals("Updated Manufacturer", rs.getString("manufacturer"));
            assertEquals(200, rs.getInt("amount"));
            assertEquals(new BigDecimal("2.99"), rs.getBigDecimal("price"));
            assertEquals(2, rs.getInt("group_id"));
        }
    }

    @Test
    public void deleteProductTest() throws SQLException {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String[] deleteArgs = {"1111"};
        DeleteProductCommand deleteCommand = new DeleteProductCommand();
        deleteCommand.execute(deleteArgs);

        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE id_product = 1111")) {
            assertFalse(rs.next(), "Product should be deleted.");
        }
    }
}

