package store_forms;

import database_connection.ProductDB;
import database_connection.ProductGroupDB;
import entity.Product;
import entity.ProductGroup;
import front_elements.ComboBoxRenderer;
import front_elements.ErrorChecker;
import front_elements.MainMenuButton;
import front_elements.Switch;
import server_work.Packet;
import server_work.PacketHelper;
import store_interface.ProductTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static front_elements.ComboBoxStructure.createCategoriesList;
import static front_elements.ComboBoxStructure.getIdOfSelectedValue;
import static java.awt.GridBagConstraints.*;

public class CreateProductForm extends JFrame {
    private static CreateProductForm instance;

    private JTextField captionField;
    private JTextField descriptionField;
    private JTextField manufacturerField;
    private JTextField amountField;
    private JTextField prizeField;
    private JComboBox<ProductGroup> categoryField;

    private static int nextId;

    static {
        try {
            nextId = ProductDB.getMaxId() + 1;
        } catch (Exception e) {
            nextId = 1;
        }
    }

    public CreateProductForm(DefaultTableModel model, JFrame frame) throws SQLException {
        this.setSize(380, 600);
        this.setLocation(1120, 100);
        this.setUndecorated(true);
        start(model, frame);
    }

    public static synchronized CreateProductForm getInstance(DefaultTableModel model, JFrame parentFrame) throws SQLException {
        if (instance == null || !instance.isVisible()) {
            instance = new CreateProductForm(model, parentFrame);
        }
        return instance;
    }

    private void init(DefaultTableModel model, JFrame frame) throws SQLException {
        final int[] max_length = {50, 100};

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        backPanel.setBackground(Color.white);
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        captionPanel.setBackground(Color.white);

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New product");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 35));
        captionPanel.add(captionLabel);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        backPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backPanel.add(captionPanel);

        JPanel buttonPanel = new JPanel();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridy = 0;
        c.gridx = 0;

        mainPanel.add(groupLabel, c);

        categoryField = new JComboBox<>();
        categoryField.setModel(createCategoriesList(null, false));
        categoryField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        categoryField.setRenderer(new ComboBoxRenderer());
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        mainPanel.setBackground(Color.white);
        mainPanel.add(categoryField, c);

        List<JLabel> labels = new ArrayList<>();
        List<JTextField> fields = new ArrayList<>();

        captionField = new JTextField();
        fields.add(captionField);
        labels.add(new JLabel("Name: "));

        descriptionField = new JTextField();
        fields.add(descriptionField);
        labels.add(new JLabel("Characteristics: "));

        manufacturerField = new JTextField();
        fields.add(manufacturerField);
        labels.add(new JLabel("Manufacturer: "));

        amountField = new JTextField();
        fields.add(amountField);
        labels.add(new JLabel("Amount: "));

        prizeField = new JTextField();
        fields.add(prizeField);
        labels.add(new JLabel("Prize: "));

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
            c.gridy = i + 1;
            c.gridx = 0;
            c.weightx = 0;
            c.ipadx = 0;
            c.fill = NONE;
            mainPanel.add(labels.get(i), c);

            fields.get(i).setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
            c.gridx = 1;
            c.weightx = 2;
            c.fill = BOTH;
            c.ipadx = 300;
            mainPanel.add(fields.get(i), c);
        }
        ErrorChecker.tFields = fields;

        MainMenuButton createButton = new MainMenuButton("Create");
        createButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e -> {
            List<List<JTextField>> fieldsList = new ArrayList<>();
            fieldsList.add(new ArrayList<>(Collections.singletonList(descriptionField)));

            List<String> errors = ErrorChecker.checkForEmptyErrors();
            List<String> errors2 = ErrorChecker.checkForLength(max_length, fieldsList);
            if (errors != null) {
                showError(errors.get(0), ErrorChecker.getErrorTextFields(errors));
            } else if (errors2 != null) {
                showError(errors2.get(0), ErrorChecker.getErrorTextFields(errors2));
            } else {
                try {
                    createNewItem(model, frame);
                } catch (SQLException ex) {
                    showError("Error while creating product: " + ex.getMessage(), new JTextField[]{captionField});
                }
            }
        });
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.fill = HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        buttonPanel.add(createButton, c);
        buttonPanel.setBackground(Color.white);
        backPanel.add(mainPanel);
        backPanel.add(buttonPanel);
        this.setContentPane(backPanel);
    }

    private void start(DefaultTableModel model, JFrame frame) throws SQLException {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        init(model, frame);
        setVisible(true);
    }

    private void showError(String message, JTextField[] textFields) {
        for (JTextField textField : textFields) {
            textField.setBorder(BorderFactory.createLineBorder(Color.red));
        }
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void createNewItem(DefaultTableModel model, JFrame frame) throws SQLException {
        String productName = captionField.getText();
        int selectedCategoryId = getIdOfSelectedValue(categoryField);
        ProductGroup category = ProductGroupDB.getByID(selectedCategoryId);
        if (ProductDB.existsByNameInOtherCategories(productName, selectedCategoryId)) {
            showError("Product name already exists in another category.", new JTextField[]{captionField});
            return;
        }

        int amount = Integer.parseInt(amountField.getText());
        double price = Double.parseDouble(prizeField.getText());
        Product newProduct = new Product(nextId++, productName, descriptionField.getText(), manufacturerField.getText(), amount, price, category);
        Packet packet = null;
        try {
            packet = PacketHelper.createProductPacket(newProduct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Socket socket = new Socket("localhost", 8765);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(packet.toBytes());
            outputStream.flush();
            socket.close();
        } catch (Exception ignored) {
        }
        ProductTable.getProductList().add(newProduct);
        Switch.switchFramesForProduct(frame, model);

        setVisible(false);
        dispose();
    }
}