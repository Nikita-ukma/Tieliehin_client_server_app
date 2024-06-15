package commands;

import models.Processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteAllProductsCommand implements Command {

    @Override
    public void execute(String[] args) throws SQLException {
//        System.out.println("Do you really want to delete all data in the Product table? Write 'Yes' to confirm:");
//        Scanner scanner = new Scanner(System.in);
//        String confirm = scanner.nextLine();
//        if (!confirm.equalsIgnoreCase("Yes")) {
//            System.out.println("Operation cancelled.");
//            return;
//        }

        try (Connection connection = Processor.getConnection()) {
            String deleteSql = "DELETE FROM Products";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                int affectedRows = deleteStmt.executeUpdate();
                System.out.println("Products deleted successfully. Rows affected: " + affectedRows);
            }
        }
    }
}
