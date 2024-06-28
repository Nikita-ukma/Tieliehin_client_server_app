package store_interface;

import front_elements.plusminusButton;
import store_forms.*;
import front_elements.MainMenuButton;
import entity.ProductGroup;
import front_elements.HintTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static database_connection.ProductGroupDB.getAll;

public class ProductGroupTable {

    static List<ProductGroup> ProductGroupList;

    public static List<ProductGroup> getProductGroupList() {
        return ProductGroupList;
    }

    static {
        try {
            ProductGroupList = getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void display(JFrame frame) {
        frame.setSize(920, 600);
        frame.setLocation(200, 100);
        frame.setLayout(new BorderLayout());
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(820, 55));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));

        Dimension buttonSize = new Dimension(120, 40);

        HintTextField nameField = new HintTextField("Enter Product Group name");
        nameField.setPreferredSize(new Dimension(240, 40));

        MainMenuButton searchButton = new MainMenuButton("Search");
        searchButton.setPreferredSize(buttonSize);

        MainMenuButton sortLabel = new MainMenuButton("Sort");
        sortLabel.setPreferredSize(buttonSize);
        sortLabel.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 16));
        MainMenuButton addNewProductGroupButton = new MainMenuButton("Add new Product Group");
        sortLabel.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 14));
        addNewProductGroupButton.setPreferredSize(new Dimension(200,40));
        MainMenuButton reportButton = new MainMenuButton("Report");
        reportButton.setPreferredSize(buttonSize);
        topPanel.add(reportButton);

        reportButton.addActionListener(e -> {
            try {
                ReportForm.display(frame);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        plusminusButton updateButton = new plusminusButton("U");
        updateButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        updateButton.setForeground(Color.decode("#040075"));
        updateButton.setPreferredSize(new Dimension(40, 40));



        topPanel.add(nameField);
        topPanel.add(searchButton);
        topPanel.add(sortLabel);
        topPanel.add(addNewProductGroupButton);
        topPanel.add(reportButton);
        topPanel.add(updateButton);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.white);

        JTable table = new JTable();
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 12));
        table.setForeground(Color.BLACK);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#040075"));
        tableHeader.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 14));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 40));

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);

        for (ProductGroup productGroup : ProductGroupList) {
            model.addRow(new Object[]{productGroup.getId(), productGroup.getName(), productGroup.getDescription()});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(400, 500));

        frame.add(tablePanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.setBackground(Color.decode("#F7943C"));

        JLabel returnLabel = new JLabel("Log out");
        returnLabel.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 16));
        returnLabel.setForeground(Color.WHITE);
        returnLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(returnLabel, BorderLayout.WEST);

        JLabel productLabel = new JLabel("Product menu...");
        productLabel.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 16));
        productLabel.setForeground(Color.WHITE);
        productLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottomPanel.add(productLabel, BorderLayout.EAST);


        addNewProductGroupButton.addActionListener(e -> {
            frame.setEnabled(true);
            CreateProductGroupForm createProductGroupForm = new CreateProductGroupForm(model, frame);
            frame.repaint();
        });

        CreateProductGroupForm createProductGroupForm = new CreateProductGroupForm(model, frame);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        productLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createProductGroupForm.getContentPane().removeAll();
                createProductGroupForm.dispose();
                frame.getContentPane().removeAll();
                frame.dispose();

                try {
                    ProductTable.display(frame);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                frame.revalidate();
                frame.repaint();
            }
        });

        AtomicBoolean lexicSort = new AtomicBoolean(true);
        sortLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lexicSort.get()) {
                    ProductGroupList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                    lexicSort.set(false);
                } else {
                    ProductGroupList.sort((c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
                    lexicSort.set(true);
                }
                updateTableModel(model, ProductGroupList);
            }
        });


        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Enter Product Group name")) {
                    nameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Enter Product Group name");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        frame.setEnabled(false);
                        ActionProductGroupForm categoryActionForm = new ActionProductGroupForm(getProductGroupList().get(row), model, frame);
                    }
                }
        });

        searchButton.addActionListener(e -> {
            String searchText = nameField.getText().trim();
            List<ProductGroup> filteredList = ProductGroupList.stream()
                    .filter(pg -> pg.getName().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            updateTableModel(model, filteredList);
        });

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

        updateButton.addActionListener(e -> {
            // Open ProductMinusForm
            frame.getContentPane().removeAll();
            frame.dispose();
            ProductGroupTable.display(frame);
        });
    }

    private static void updateTableModel(DefaultTableModel model, List<ProductGroup> productGroupList) {
        model.setRowCount(0);
        for (ProductGroup productGroup : productGroupList) {
            model.addRow(new Object[]{productGroup.getId(), productGroup.getName(), productGroup.getDescription()});
        }
    }
}