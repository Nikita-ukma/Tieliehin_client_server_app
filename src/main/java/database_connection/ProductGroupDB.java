package database_connection;

import entity.ProductGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ProductGroupDB {
    private static Connection connection;

    private static final String ID = "group_id";
    private static final String NAME = "group_name";

    private static final String DESCRIPTION = "description";
    private static final String GET_BY_ID = "SELECT * FROM ProductGroup WHERE "+ID+"=?;";
    private static final String GET_ALL = "SELECT * FROM ProductGroup;";
    private static final String GET_MAX_ID = "SELECT MAX(" + ID + ") AS max_id FROM ProductGroup;";

    public static void setConnection(Connection con){
        connection = con;
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
    public static entity.ProductGroup getByID(int id){
        try{
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet =  statement.executeQuery();
            ProductGroup ProductGroup = null;
            while(resultSet.next()) {
                ProductGroup = new ProductGroup(resultSet.getInt(ID), resultSet.getString(NAME), resultSet.getString(DESCRIPTION));
            }
            return ProductGroup;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ArrayList<ProductGroup> getAll() throws SQLException {
            setConnection(connection);
            PreparedStatement statement = connection.prepareStatement(GET_ALL);

            ResultSet resultSet =  statement.executeQuery();
            ArrayList<ProductGroup> categories = new ArrayList<>();
            while(resultSet.next()) {
                categories.add(new ProductGroup(resultSet.getInt(ID), resultSet.getString(NAME), resultSet.getString(DESCRIPTION)));
            }
            return categories;
    }
}
