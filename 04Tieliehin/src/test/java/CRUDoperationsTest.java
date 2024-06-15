import commands.*;
import models.Processor;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class CRUDoperationsTest {
    @BeforeEach
    public void setupDatabase() throws SQLException {
        try (Connection connection = Processor.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                String createTable = "CREATE TABLE IF NOT EXISTS Products (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(255), " +
                        "description VARCHAR(255), " +
                        "manufacturer VARCHAR(255), " +
                        "quantity_in_stock INT, " +
                        "price_per_unit DECIMAL(10, 2), " +
                        "group_id INT)";
                stmt.execute(createTable);
                stmt.execute("DELETE FROM Products");
            }
        }
    }

    @Test
    public void createProductTest() throws SQLException {
        String[] args = {"Milk", "Milk Description", "Manufacturer A", "100", "1.99", "1"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(args);

        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE name = 'Milk'")) {
            assertTrue(rs.next(), "Product should be created.");
            assertEquals("Milk", rs.getString("name"));
            assertEquals("Milk Description", rs.getString("description"));
            assertEquals("Manufacturer A", rs.getString("manufacturer"));
            assertEquals(100, rs.getInt("quantity_in_stock"));
            assertEquals(new BigDecimal("1.99"), rs.getBigDecimal("price_per_unit"));
            assertEquals(1, rs.getInt("group_id"));
        }
    }

    @Test
    public void readProductTest() throws SQLException {
        // Спочатку створимо продукт для читання
        String[] createArgs = {"Milk", "Milk Description", "Manufacturer A", "100", "1.99", "1"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(createArgs);

        // Читання продукту за ID
        String[] readArgs = {"1"};
        ReadProductCommand readCommand = new ReadProductCommand();
        readCommand.execute(readArgs);
    }

    @Test
    public void updateProductTest() throws SQLException {
        String[] createArgs = {"Milk", "Milk Description", "Manufacturer A", "100", "1.99", "1"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(createArgs);
        String[] updateArgs = {"Updated Milk", "Updated Description", "Updated Manufacturer", "200", "2.99", "2", "Milk"};
        UpdateProductCommand updateCommand = new UpdateProductCommand();
        updateCommand.execute(updateArgs);
        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE name = \"Updated Milk\"")) {
            assertTrue(rs.next(), "Product should exist after update.");
            assertEquals("Updated Milk", rs.getString("name"));
            assertEquals("Updated Description", rs.getString("description"));
            assertEquals("Updated Manufacturer", rs.getString("manufacturer"));
            assertEquals(200, rs.getInt("quantity_in_stock"));
            assertEquals(new BigDecimal("2.99"), rs.getBigDecimal("price_per_unit"));
            assertEquals(2, rs.getInt("group_id"));
        }
    }

    @Test
    public void deleteProductTest() throws SQLException {
        // Спочатку створимо продукт для видалення
        String[] createArgs = {"Milk", "Milk Description", "Manufacturer A", "100", "1.99", "1"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(createArgs);

        // Видалення продукту за ID
        String[] deleteArgs = {"1"};
        DeleteProductCommand deleteCommand = new DeleteProductCommand();
        deleteCommand.execute(deleteArgs);

        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE id = 1")) {
            assertFalse(rs.next(), "Product should be deleted.");
        }
    }

    @Test
    public void deleteAllProductsTest() throws SQLException {
        // Створення декількох продуктів
        String[] createArgs1 = {"Milk", "Milk Description", "Manufacturer A", "100", "1.99", "1"};
        String[] createArgs2 = {"Cheese", "Cheese Description", "Manufacturer B", "50", "3.99", "2"};
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.execute(createArgs1);
        createCommand.execute(createArgs2);

        // Видалення всіх продуктів
        DeleteAllProductsCommand deleteAllCommand = new DeleteAllProductsCommand();
        deleteAllCommand.execute(new String[]{});

        try (Connection connection = Processor.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Products")) {
            rs.next();
            int count = rs.getInt(1);
            assertEquals(0, count, "All products should be deleted.");
        }
    }
}

