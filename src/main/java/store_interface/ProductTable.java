package store_interface;

import database_connection.ProductDB;
import entity.Product;
import front_elements.HintTextField;
import front_elements.MainMenuButton;
import front_elements.plusminusButton;
import store_forms.CreateProductForm;
import store_forms.ActionProductForm;
import store_forms.ProductMinusForm;
import store_forms.ProductPlusForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductTable {

    static List<Product> productList;

    public static List<Product> getProductList() {
        return productList;
    }

    public static void display(JFrame frame) throws SQLException {
        productList = ProductDB.getAllOrderName(true);

        frame.setPreferredSize(new Dimension(940, 600));
        frame.setLocation(180, 100);
        frame.setLayout(new BorderLayout());
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(940, 55));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));

        Dimension buttonSize = new Dimension(150, 40);

        MainMenuButton searchProductButton = new MainMenuButton("Search");
        searchProductButton.setPreferredSize(new Dimension(80, 40));

        HintTextField nameField = new HintTextField("Write product name");
        nameField.setPreferredSize(new Dimension(160, 40));

        MainMenuButton searchProductGroupButton = new MainMenuButton("Search");
        searchProductGroupButton.setPreferredSize(new Dimension(80, 40));

        HintTextField productGroupField = new HintTextField("Write product group name");
        productGroupField.setPreferredSize(new Dimension(160, 40));

        MainMenuButton sortButton = new MainMenuButton("Sort");
        sortButton.setPreferredSize(new Dimension(80, 40));

        MainMenuButton addNewProductButton = new MainMenuButton("Add new product");
        addNewProductButton.setPreferredSize(buttonSize);

        plusminusButton plusProduct = new plusminusButton("+");
        plusProduct.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        plusProduct.setForeground(Color.decode("#040075"));
        plusProduct.setPreferredSize(new Dimension(40, 40));

        plusminusButton minusProduct = new plusminusButton("-");
        minusProduct.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        minusProduct.setPreferredSize(new Dimension(40, 40));
        minusProduct.setForeground(Color.decode("#040075"));

        plusminusButton updateButton = new plusminusButton("U");
        updateButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        updateButton.setForeground(Color.decode("#040075"));
        updateButton.setPreferredSize(new Dimension(40, 40));
        
        topPanel.add(nameField);
        topPanel.add(searchProductButton);
        topPanel.add(productGroupField);
        topPanel.add(searchProductGroupButton);
        topPanel.add(sortButton);
        topPanel.add(addNewProductButton);
        topPanel.add(plusProduct);
        topPanel.add(minusProduct);
        topPanel.add(updateButton);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.white);

        JTable table = new JTable();
        table.setBackground(Color.WHITE);
        table.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        table.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        table.setForeground(Color.BLACK);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#040075"));
        tableHeader.setFont(new Font("Century Gothic", Font.BOLD, 14));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 40));

        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "ID", "Name", "Description", "Manufacturer", "Amount", "Price", "Group"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);

        // Додавання даних до таблиці
        for (Product pr : productList) {
            model.addRow(new Object[]{
                    pr.getId(), pr.getName(), pr.getDescription(),
                    pr.getManufacturer(), pr.getAmount(), pr.getPrice(),
                    pr.getProductGroup() != null ? pr.getProductGroup().getName() : "N/A",
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(600, 500));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.setBackground(Color.decode("#F7943C"));

        JLabel productGroupLabel = new JLabel("Product Groups");
        productGroupLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        productGroupLabel.setForeground(Color.WHITE);
        productGroupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel returnLabel = new JLabel("Log out");
        returnLabel.setFont(new Font("Century Gothic", Font.BOLD, 16));
        returnLabel.setForeground(Color.WHITE);
        returnLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.getContentPane().removeAll();
                frame.setVisible(false);
                LoginMenu a = new LoginMenu();
                a.setBounds(500, 300, 400, 300);
                a.setVisible(true);
                frame.revalidate();
                frame.repaint();
            }
        });

        bottomPanel.add(productGroupLabel, BorderLayout.EAST);
        bottomPanel.add(returnLabel, BorderLayout.WEST);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        CreateProductForm createProductForm = CreateProductForm.getInstance(model, frame);
        frame.pack();
        frame.setVisible(true);

        AtomicBoolean lexicSort = new AtomicBoolean(true);
        sortButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lexicSort.get()) {
                    productList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                    lexicSort.set(false);
                } else {
                    productList.sort((c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
                    lexicSort.set(true);
                }
                updateTableModel(model, productList);
            }
        });

        addNewProductButton.addActionListener(e -> {
            try {
                CreateProductForm form = CreateProductForm.getInstance(model, frame);
                form.setVisible(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        searchProductButton.addActionListener(e -> {
            String searchText = nameField.getText().trim();
            List<Product> filteredList = productList.stream()
                    .filter(pg -> pg.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            updateTableModel(model, filteredList);
        });

        searchProductGroupButton.addActionListener(e -> {
            String searchText = productGroupField.getText().trim();
            List<Product> filteredList = productList.stream()
                    .filter(pg -> pg.getProductGroup() != null && pg.getProductGroup().getName().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            updateTableModel(model, filteredList);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    frame.setEnabled(false);
                    try {
                        ActionProductForm actionProductForm = new ActionProductForm(productList.get(row), model, frame);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        productGroupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.getContentPane().removeAll();
                frame.dispose();
                createProductForm.dispose();
                ProductGroupTable.display(frame);
                frame.revalidate();
                frame.repaint();
            }
        });

        plusProduct.addActionListener(e -> {
            // Open ProductPlusForm
            try {
                new ProductPlusForm(model, frame);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        minusProduct.addActionListener(e -> {
            // Open ProductMinusForm
            try {
                new ProductMinusForm(model, frame);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateButton.addActionListener(e -> {
            // Open ProductMinusForm
            try {
                frame.getContentPane().removeAll();
                frame.dispose();
                ProductTable.display(frame);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private static void updateTableModel(DefaultTableModel model, List<Product> productList) {
        model.setRowCount(0);
        for (Product sp : productList) {
            model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getDescription(), sp.getManufacturer(), sp.getAmount(), sp.getPrice(), sp.getProductGroup().getName()});
        }
    }
}