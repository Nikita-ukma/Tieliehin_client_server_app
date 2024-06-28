package front_elements;

import database_connection.ProductGroupDB;
import entity.Product;
import entity.ProductGroup;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class ComboBoxStructure {


    public static int getIdOfSelectedValue(JComboBox comboBox) {    // Метод для отримання id вибраного значення з ComboBox
        int id = -1;
        if (comboBox.getSelectedItem() instanceof Object[]) {
            Object[] rowData = (Object[]) comboBox.getSelectedItem();
            id = Integer.valueOf(rowData[0].toString());
        }
        return id;
    }


    public static DefaultComboBoxModel createComboBoxModelForProduct(List<Product> productList) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Product product : productList) {
            Object[] rowData = new Object[2];
            rowData[0] = product.getId();
            rowData[1] = product.getName();
            model.addElement(rowData);
        }
        return model;
    }

    public static List<ProductGroup> getCategoriesArrayList() throws SQLException {
        return ProductGroupDB.getAll();
    }
    public static DefaultComboBoxModel createCategoriesList(ProductGroup selectedProductGroup, boolean shouldIncludeSelected) throws SQLException {
        List<ProductGroup> productGroupList = getCategoriesArrayList();

        DefaultComboBoxModel model = new DefaultComboBoxModel();

        if (productGroupList == null || productGroupList.isEmpty()) {
            return model;
        }

        if (shouldIncludeSelected && selectedProductGroup != null && !productGroupList.contains(selectedProductGroup)) {
            productGroupList.add(selectedProductGroup);
        }

        for (ProductGroup productGroup : productGroupList) {
            Object[] rowData = new Object[2];
            rowData[0] = productGroup.getId();
            rowData[1] = productGroup.getName();
            model.addElement(rowData);
            if (selectedProductGroup != null && productGroup.equals(selectedProductGroup)) {
                model.setSelectedItem(rowData);
            }
        }
        return model;
    }
}