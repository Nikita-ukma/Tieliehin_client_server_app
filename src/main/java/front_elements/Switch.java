package front_elements;

import entity.*;
import store_interface.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Switch {
    public static void switchFramesForProduct(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (Product sp : ProductTable.getProductList()) {
            model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getDescription(), sp.getManufacturer(), sp.getAmount(), sp.getPrice(), sp.getProductGroup().getName()});
        }
        frame.setEnabled(true);
       }

    public static void switchFramesForProductGroup(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (ProductGroup ProductGroup : ProductGroupTable.getProductGroupList()) {
            model.addRow(new Object[]{ProductGroup.getId(), ProductGroup.getName(), ProductGroup.getDescription()});
        }
        frame.setEnabled(true);
    }
}
