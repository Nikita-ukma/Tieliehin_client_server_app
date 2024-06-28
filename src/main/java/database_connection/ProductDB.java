package database_connection;

import entity.Product;
import entity.ProductGroup;

import java.sql.*;
import java.util.ArrayList;

public class ProductDB {
    private static Connection connection;
    private static final String ID = "id_product";
    private static final String NAME = "product_name";
    private static final String DESCRIPTION = "description";
    private static final String MANUFACTURER = "manufacturer";
    private static final String AMOUNT = "amount";
    private static final String PRICE = "price";
    private static final String GROUP_ID = "group_id";
    private static final String GET_BY_ID = "SELECT * FROM product WHERE " + ID + "=?;";
    private static final String GET_MAX_ID = "SELECT MAX(" + ID + ") AS max_id FROM Product;";
    private static final String GET_ALL = "SELECT * FROM product;";
    private static final String GET_ALL_ORDER_NAME = "SELECT * FROM product ORDER BY " + NAME + " ";
    private static final String GET_ALL_IN_GROUP = "SELECT * FROM product WHERE " + GROUP_ID + "=? ORDER BY " + NAME + " ";
    private static final String GET_TOTAL_VALUE = "SELECT SUM(amount * price) AS total_value FROM product;";
    private static final String GET_TOTAL_VALUE_IN_GROUP = "SELECT SUM(amount * price) AS total_value FROM product WHERE group_id = ?;";
    private static final String CHECK_NAME_IN_OTHER_CATEGORIES = "SELECT COUNT(*) AS count FROM product WHERE " + NAME + "=? AND " + GROUP_ID + "!=?;";

    public static void setConnection(Connection con) {
        connection = con;
    }

    public static boolean existsByNameInOtherCategories(String name, int categoryId) {
        try {
            PreparedStatement statement = connection.prepareStatement(CHECK_NAME_IN_OTHER_CATEGORIES);
            statement.setString(1, name);
            statement.setInt(2, categoryId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Product getByID(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            Product product = null;
            if (resultSet.next()) {
                product = new Product(
                        resultSet.getInt(ID),
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getString(MANUFACTURER),
                        resultSet.getInt(AMOUNT),
                        resultSet.getDouble(PRICE),
                        ProductGroupDB.getByID(resultSet.getInt(GROUP_ID))
                );
            }
            return product;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<Product> getAll() {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt(ID),
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getString(MANUFACTURER),
                        resultSet.getInt(AMOUNT),
                        resultSet.getDouble(PRICE),
                        ProductGroupDB.getByID(resultSet.getInt(GROUP_ID))
                ));
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static ArrayList<Product> getAllInCategory(boolean ascending, ProductGroup category) {
        try {
            String order;
            if (ascending) {
                order = "ASC";
            } else {
                order = "DESC";
            }
            PreparedStatement statement = connection.prepareStatement(GET_ALL_IN_GROUP+order+";");
            statement.setInt(1, category.getId());

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt(ID),
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getString(MANUFACTURER),
                        resultSet.getInt(AMOUNT),
                        resultSet.getDouble(PRICE),
                        ProductGroupDB.getByID(resultSet.getInt(GROUP_ID))
                ));
            }
            return products;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }

    }

    public static ArrayList<Product> getAllOrderName(boolean ascending) {
        try {
            String order = ascending ? "ASC" : "DESC";
            PreparedStatement statement = connection.prepareStatement(GET_ALL_ORDER_NAME + order + ";");

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt(ID),
                        resultSet.getString(NAME),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getString(MANUFACTURER),
                        resultSet.getInt(AMOUNT),
                        resultSet.getDouble(PRICE),
                        ProductGroupDB.getByID(resultSet.getInt(GROUP_ID))
                ));
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static double getTotalValue() {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_TOTAL_VALUE);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_value");
            }
            return 0.0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0.0;
        }
    }

    public static double getTotalValueInGroup(int groupId) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_TOTAL_VALUE_IN_GROUP);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("total_value");
            }
            return 0.0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0.0;
        }
    }

    public static int getMaxId() {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_MAX_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}