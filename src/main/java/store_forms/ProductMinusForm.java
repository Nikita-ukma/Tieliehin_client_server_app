package store_forms;

import database_connection.ProductDB;
import front_elements.ComboBoxRenderer;
import front_elements.MainMenuButton;
import front_elements.Switch;
import entity.Product;
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
import java.util.List;

import static front_elements.ComboBoxStructure.createComboBoxModelForProduct;
import static front_elements.ComboBoxStructure.getIdOfSelectedValue;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;

public class ProductMinusForm extends JFrame {

    private JPanel backPanel;
    private JComboBox<Product> productField;
    private JTextField amountField;

    public ProductMinusForm(DefaultTableModel model, JFrame frame) throws SQLException {
        super("Remove Product from Stock");
        this.setSize(400, 300);
        this.setLocation(1120, 100);
        this.setUndecorated(true);
        start(model, frame);
    }

    private void init(DefaultTableModel model, JFrame frame) {
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        backPanel.setBackground(Color.white);

        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        captionPanel.setBackground(Color.white);

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Remove Product from Stock");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 20));
        captionPanel.add(captionLabel);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        backPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backPanel.add(captionPanel);

        JPanel buttonPanel = new JPanel();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JLabel productLabel = new JLabel("Product: ");
        productLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(productLabel, c);

        productField = new JComboBox<>();
        List<Product> productList = ProductDB.getAll();
        assert productList != null;
        productField.setModel(createComboBoxModelForProduct(productList));
        productField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        productField.setRenderer(new ComboBoxRenderer());
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        mainPanel.setBackground(Color.white);
        mainPanel.add(productField, c);

        JLabel amountLabel = new JLabel("Amount: ");
        amountLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridy = 1;
        c.gridx = 0;
        mainPanel.add(amountLabel, c);

        amountField = new JTextField();
        amountField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        mainPanel.add(amountField, c);

        MainMenuButton acceptButton = new MainMenuButton("Accept");
        acceptButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        acceptButton.setPreferredSize(new Dimension(150, 40));
        acceptButton.addActionListener(e -> {
            try {
                removeProductFromStock(frame);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error updating product: " + ex.getMessage());
            }
        });

        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.fill = HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,10,0,10));
        buttonPanel.add(acceptButton, c);
        buttonPanel.setBackground(Color.white);

        MainMenuButton cancelButton = new MainMenuButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        cancelButton.addActionListener(e -> {
            Switch.switchFramesForProduct(frame, model);
            dispose();
        });
        c.gridx = 1;
        c.insets = new Insets(5, 10, 5, 8);
        buttonPanel.add(cancelButton, c);

        c.gridy = 2;
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

    private void removeProductFromStock(JFrame frame) throws SQLException {
        int productId = getIdOfSelectedValue(productField);
        int amount = Integer.parseInt(amountField.getText());
        Product product = ProductDB.getByID(productId);

        assert product != null;
        if (product.getAmount() < amount) {
            JOptionPane.showMessageDialog(null, "Not enough stock available.");
            return;
        }
        product.setAmount(product.getAmount() - amount);
        Packet packet = null;
        try {
            packet = PacketHelper.updateProductPacket(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sendPacketToServer(packet);
        try {
            Thread.sleep(250);
            frame.repaint();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        frame.getContentPane().removeAll();
        ProductTable.display(frame);
        frame.repaint();
        dispose();
    }

    private void sendPacketToServer(Packet packet) {
        try (Socket socket = new Socket("localhost", 8765);
             OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(packet.toBytes());
            outputStream.flush();
        } catch (Exception ignored) {
        }
    }
    private void updateTableModel(DefaultTableModel model, Product product) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int) model.getValueAt(i, 0) == product.getId()) {
                model.setValueAt(product.getAmount(), i, 4); // assuming the amount is in the fifth column
                break;
            }
        }
    }

    public void start(DefaultTableModel model, JFrame frame) {
        init(model, frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}