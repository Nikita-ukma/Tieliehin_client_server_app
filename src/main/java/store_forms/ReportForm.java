package store_forms;
import database_connection.ProductDB;
import database_connection.ProductGroupDB;
import entity.ProductGroup;
import front_elements.MainMenuButton;
import front_elements.Switch;
import store_interface.ProductGroupTable;
import store_interface.ProductTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReportForm {
    public static void display(JFrame frame) throws SQLException {
        frame.getContentPane().removeAll();
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setLocation(200, 100);
        frame.setLayout(new BorderLayout());

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        backPanel.setBackground(Color.white);

        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        captionPanel.setBackground(Color.white);

        JLabel captionLabel = new JLabel("Store Report");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
         captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 35));
        captionPanel.add(captionLabel);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        backPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        JLabel totalProductsLabel = new JLabel("Total number of products: " + Objects.requireNonNull(ProductDB.getAll()).size());
        totalProductsLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        mainPanel.add(totalProductsLabel, c);

        c.gridy++;
        JLabel totalValueLabel = new JLabel("Total value of products: " + ProductDB.getTotalValue());
        totalValueLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        mainPanel.add(totalValueLabel, c);

        c.gridy++;
        JLabel totalGroupsLabel = new JLabel("Total number of product groups: " + ProductGroupDB.getMaxId());
        totalGroupsLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        mainPanel.add(totalGroupsLabel, c);

        c.gridy++;
        String[] columnNames = {"Product Group", "Total Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setBackground(Color.white);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#040075"));
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 14));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 40));

        List<ProductGroup> productGroups = ProductGroupDB.getAll();
        for (ProductGroup group : productGroups) {
            double totalValue = ProductDB.getTotalValueInGroup(group.getId());
            model.addRow(new Object[]{group.getName(), totalValue});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.white);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.setBackground(Color.white);
        mainPanel.add(scrollPane, c);

        backPanel.add(mainPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        MainMenuButton closeButton = new MainMenuButton("Close");
        closeButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        closeButton.setPreferredSize(new Dimension(150, 40));
        closeButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.dispose();
            ProductGroupTable.display(frame);
            frame.setSize(920, 600);
            frame.setLocation(200, 100);
            frame.revalidate();
            frame.repaint();
        });
        buttonPanel.add(closeButton);
        backPanel.add(buttonPanel);

        frame.add(backPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
