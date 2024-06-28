package store_forms;

import database_connection.ProductGroupDB;
import entity.ProductGroup;
import java.awt.*;
import java.awt.event.*;
import front_elements.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import entity.Product;
import server_work.Packet;
import server_work.PacketHelper;
import store_interface.ProductTable;

import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.sql.SQLException;
import static front_elements.ComboBoxStructure.createCategoriesList;
import static front_elements.ComboBoxStructure.getIdOfSelectedValue;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class ActionProductForm extends JFrame {

    private JPanel backPanel;
    private JTextField captionField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField priceField;
    private JTextField manufacturerField;
    private JComboBox<ProductGroup> productGroupField;
    private Product product;

    public ActionProductForm(Product product, DefaultTableModel model, JFrame frame) throws SQLException {
        super("Edit product");
        this.product = product;
        this.setSize(600, 350);
        this.setLocation(420, 100);
        this.setUndecorated(true);
        start(model, frame);
    }

    private void init(DefaultTableModel model, JFrame frame) throws SQLException {
        final int[] max_length = {50, 100, 10, 10, 50};
        List<JTextField> fields = new ArrayList<>();

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        backPanel.setBackground(Color.white);
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        captionPanel.setBackground(Color.white);

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Edit Product");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
         captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 35));
        captionPanel.add(captionLabel);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        backPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel groupLabel = new JLabel("Product Group: ");
        groupLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(groupLabel, c);

        productGroupField = new JComboBox<>();
        productGroupField.setModel(createCategoriesList(product.getProductGroup(), false));
        productGroupField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        productGroupField.setRenderer(new ComboBoxRenderer());
        productGroupField.setEnabled(false);
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        mainPanel.setBackground(Color.white);
        mainPanel.add(productGroupField, c);

        List<JLabel> labels = new ArrayList<>();

        captionField = new JTextField();
        captionField.setText(product.getName());
        fields.add(captionField);
        labels.add(new JLabel("Name: "));

        descriptionField = new JTextField();
        descriptionField.setText(product.getDescription());
        fields.add(descriptionField);
        labels.add(new JLabel("Description: "));

        amountField = new JTextField();
        amountField.setText(String.valueOf(product.getAmount()));
        fields.add(amountField);
        labels.add(new JLabel("Amount: "));

        priceField = new JTextField();
        priceField.setText(String.valueOf(product.getPrice()));
        fields.add(priceField);
        labels.add(new JLabel("Price: "));

        manufacturerField = new JTextField();
        manufacturerField.setText(product.getManufacturer());
        fields.add(manufacturerField);
        labels.add(new JLabel("Manufacturer: "));

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
            c.gridy = i + 1;
            c.gridx = 0;
            c.weightx = 0;
            c.ipadx = 0;
            c.fill = NONE;
            mainPanel.add(labels.get(i), c);

            fields.get(i).setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
            fields.get(i).setEditable(false);
            c.gridx = 1;
            c.weightx = 2;
            c.fill = BOTH;
            c.ipadx = 300;
            mainPanel.add(fields.get(i), c);
        }

        ErrorChecker.tFields = fields;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,10,0,10));
        buttonPanel.setBackground(Color.white);

        MainMenuButton editButton = new MainMenuButton("Edit");
        editButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        editButton.setPreferredSize(new Dimension(120, 40));
        editButton.addActionListener(e -> {
            for (JTextField field : fields) {
                field.setEditable(true);
            }
            productGroupField.setEnabled(true);
        });
        buttonPanel.add(editButton);

        MainMenuButton saveButton = new MainMenuButton("Save");
        saveButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveChanges(fields, max_length));
        buttonPanel.add(saveButton);

        MainMenuButton deleteButton = new MainMenuButton("Delete");
        deleteButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        deleteButton.addActionListener(e -> deleteProduct(model, frame));
        buttonPanel.add(deleteButton);

        MainMenuButton cancelButton = new MainMenuButton("Cancel");
        cancelButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> {
            Switch.switchFramesForProduct(frame, model);
            dispose();
        });
        buttonPanel.add(cancelButton);

        c.gridy = labels.size() + 1;
        c.gridx = 0;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel, c);

        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Switch.switchFramesForProduct(frame, model);
                dispose();
            }
        });
    }

    private void saveChanges(List<JTextField> fields, int[] max_length) {
        List<List<JTextField>> fieldsList = new ArrayList<>();
        fieldsList.add(new ArrayList<>(Collections.singletonList(captionField)));
        fieldsList.add(new ArrayList<>(Collections.singletonList(descriptionField)));
        fieldsList.add(new ArrayList<>(Collections.singletonList(amountField)));
        fieldsList.add(new ArrayList<>(Collections.singletonList(priceField)));
        fieldsList.add(new ArrayList<>(Collections.singletonList(manufacturerField)));

        // Перевіряємо, чи кількість довжин відповідає кількості груп полів
        if (fieldsList.size() != max_length.length) {
            System.out.println(fieldsList.size());
            System.out.println(max_length.length);
            throw new IllegalArgumentException("The length of max_length does not match the number of field groups");
        }

        List<String> errors = ErrorChecker.checkForEmptyErrors();
        List<String> errors2 = ErrorChecker.checkForLength(max_length, fieldsList);
        if (errors != null) {
            showError(errors.get(0), ErrorChecker.getErrorTextFields(errors));
        } else if (errors2 != null) {
            showError(errors2.get(0), ErrorChecker.getErrorTextFields(errors2));
        } else {
            updateItem();
            for (JTextField field : fields) {
                field.setEditable(false);
            }
        }
    }

    private void deleteProduct(DefaultTableModel model, JFrame frame) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure? Delete this product?", "Delete Product",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Packet packet = null;
            try {
                packet = PacketHelper.deleteProductPacket(product.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            sendPacketToServer(packet);

            ProductTable.getProductList().remove(product);
            Switch.switchFramesForProduct(frame, model);
            dispose();
        }
    }
    private void sendPacketToServer(Packet packet) {
        try (Socket socket = new Socket("localhost", 8765);
             OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(packet.toBytes());
            outputStream.flush();
        } catch (Exception ignored) {
        }
    }

    private void updateItem() {
        int catId = getIdOfSelectedValue(productGroupField);
        ProductGroup productGroup = ProductGroupDB.getByID(catId);
        Product temp = new Product(product.getId(), captionField.getText(), descriptionField.getText(), manufacturerField.getText(),
                Integer.parseInt(amountField.getText()), Double.parseDouble(priceField.getText()), productGroup);
        if (!temp.equals(product)) {
            Packet packet = null;
            try {
                packet = PacketHelper.updateProductPacket(temp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Відправляємо пакет до сервера
            sendPacketToServer(packet);

            ProductTable.getProductList().remove(product);
            ProductTable.getProductList().add(temp);
            product = temp;
        }
    }

    private void showError(String text, JTextField[] fields) {
        for (JTextField field : fields) {
            field.setBackground(Color.red);
        }
        JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
        for (JTextField field : fields) {
            field.setBackground(Color.white);
            if (field == captionField)
                captionField.setText(product.getName());
            else if (field == descriptionField)
                descriptionField.setText(product.getDescription());
            else if (field == amountField)
                amountField.setText(String.valueOf(product.getAmount()));
            else if (field == priceField)
                priceField.setText(String.valueOf(product.getPrice()));
            else if (field == manufacturerField)
                manufacturerField.setText(product.getManufacturer());
        }
        try {
            productGroupField.setModel(createCategoriesList(product.getProductGroup(), true));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(DefaultTableModel model, JFrame frame) throws SQLException {
        init(model, frame);
        add(backPanel);
        this.setVisible(true);
    }
}