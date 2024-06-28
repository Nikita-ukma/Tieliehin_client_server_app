package store_forms;

import entity.Product;
import front_elements.BottomButtonPanel;
import front_elements.*;
import entity.ProductGroup;
import server_work.Packet;
import server_work.PacketHelper;
import store_interface.ProductGroupTable;
import database_connection.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ActionProductGroupForm extends JFrame {

    private JPanel backPanel;
    private ProductGroup category;
    private JTextField captionField;
    private JTextField descriptionField;

    public ActionProductGroupForm(ProductGroup category, DefaultTableModel model, JFrame frame) {
        this.setSize(600, 500);
        this.category = category;
        this.setLocation(300, 100);
        start(model, frame);
    }

    private void init(DefaultTableModel model, JFrame frame) {
        final int[] max_lengths = {50, 100}; // Максимальні довжини для полів caption і description
        List<JTextField> fields = new ArrayList<>();
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        backPanel.setBackground(Color.white);
        JPanel captionPanel = new JPanel();
        captionPanel.setBackground(Color.white);
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        BottomButtonPanel buttonsPanel = new BottomButtonPanel(true);
        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Product Group");
         captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 35));
        Border border = captionLabel.getBorder();
        Border margin = new EmptyBorder(0, 0, 5, 0);
        captionLabel.setBorder(new CompoundBorder(border, margin));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.white);
        JLabel getCaptionLabel = new JLabel("Caption: ");
        getCaptionLabel.setBackground(Color.white);
        getCaptionLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(getCaptionLabel, c);

        captionField = new JTextField();
        captionField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        captionField.setBackground(Color.white);
        captionField.setEditable(false);
        captionField.setText(category.getName());
        c.gridx = 1;
        c.weightx = 2;
        c.ipadx = 200;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(captionField, c);

        JLabel getDescriptionLabel = new JLabel("Description: ");
        getDescriptionLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        getDescriptionLabel.setBackground(Color.white);
        c.gridy = 1;
        c.gridx = 0;
        mainPanel.add(getDescriptionLabel, c);

        descriptionField = new JTextField();
        descriptionField.setBackground(Color.white);
        descriptionField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        descriptionField.setEditable(false);
        descriptionField.setText(category.getDescription());
        c.gridx = 1;
        c.weightx = 2;
        c.ipadx = 200;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(descriptionField, c);

        fields.add(captionField);
        fields.add(descriptionField);
        ErrorChecker.tFields = fields;

        JPanel buttonPanel = createButtonPanel(model, frame, max_lengths, buttonsPanel);
        buttonPanel.setBackground(Color.white);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        mainPanel.add(buttonPanel, gridBagConstraints);

        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Switch.switchFramesForProductGroup(frame, model);
                dispose();
            }
        });
    }

    public void EditButton(BottomButtonPanel buttonPanel) {
        buttonPanel.setEnabled(true);
        captionField.setEditable(true);
        descriptionField.setEditable(true);
    }

    public void SaveButton(BottomButtonPanel buttonPanel, int[] max_lengths) {
        List<JTextField> textFields = new ArrayList<>();
        textFields.add(captionField);
        textFields.add(descriptionField);
        ErrorChecker.tFields = textFields;
        List<String> errors = ErrorChecker.checkForEmptyErrors();
        if (errors != null) {
            showError(errors.get(0), ErrorChecker.getErrorTextFields(errors));
        } else if (ErrorChecker.checkForLength(max_lengths[0], 0)) {
            showError("Caption is too long. Maximum: 50 symbols.", new JTextField[]{captionField});
        } else if (ErrorChecker.checkForLength(max_lengths[1], 1)) {
            showError("Description is too long. Maximum: 100 symbols.", new JTextField[]{descriptionField});
        } else {
            editItem();
            buttonPanel.setEnabled(false);
            captionField.setEditable(false);
            descriptionField.setEditable(false);
        }
    }

    public void DeleteButton(DefaultTableModel model, JFrame frame) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure? Delete this category?", "Delete category",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            deleteItem(model, frame);
        }
    }

    public void CancelButton(DefaultTableModel model, JFrame frame) {
        Switch.switchFramesForProductGroup(frame, model);
        dispose();
    }

    private JPanel createButtonPanel(DefaultTableModel model, JFrame frame, int[] max_lengths, BottomButtonPanel buttonsPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JLabel editLabel = createClickableLabel("Edit", Color.decode("#040075"), () -> EditButton(buttonsPanel));
        JLabel saveLabel = createClickableLabel("Save", Color.decode("#040075"), () -> SaveButton(buttonsPanel, max_lengths));
        JLabel deleteLabel = createClickableLabel("Delete", Color.decode("#040075"), () -> DeleteButton(model, frame));
        JLabel cancelLabel = createClickableLabel("Cancel", Color.decode("#040075"), () -> CancelButton(model, frame));

        buttonPanel.add(editLabel);
        buttonPanel.add(saveLabel);
        buttonPanel.add(deleteLabel);
        buttonPanel.add(cancelLabel);

        return buttonPanel;
    }

    private JLabel createClickableLabel(String text, Color backgroundColor, Runnable onClickAction) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.white);
        label.setForeground(backgroundColor);
        label.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 24));
        label.setPreferredSize(new Dimension(150, 40));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });
        return label;
    }

    private void deleteItem(DefaultTableModel model, JFrame frame) {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure? Delete this category and all its products?", "Delete category",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            List<Product> products = ProductDB.getAllInCategory(true, category);
            for (Product product : products) {
                Packet deleteProductPacket = null;
                try {
                    deleteProductPacket = PacketHelper.deleteProductPacket(product.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                sendPacketToServer(deleteProductPacket);
            }
            Packet deleteGroupPacket = null;
            try {
                deleteGroupPacket = PacketHelper.deleteProductGroupPacket(category.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            sendPacketToServer(deleteGroupPacket);
            ProductGroupTable.getProductGroupList().remove(category);
            Switch.switchFramesForProductGroup(frame, model);
            dispose();
        }
    }

    private void editItem() {
        if (!category.getName().equals(captionField.getText()) || !category.getDescription().equals(descriptionField.getText())) {
            ProductGroup temp = new ProductGroup(category.getId(), captionField.getText(), descriptionField.getText());
            ProductGroupTable.getProductGroupList().set(ProductGroupTable.getProductGroupList().indexOf(category), temp);
            category = temp;
            Packet packet = null;
            try {
                packet = PacketHelper.updateProductGroupPacket(category);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            sendPacketToServer(packet);
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

    public void start(DefaultTableModel model, JFrame frame) {
        init(model, frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

    private void showError(String text, JTextField[] fields) {
        for (JTextField field : fields) {
            field.setForeground(Color.red);
        }
        JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
        for (JTextField field : fields) {
            field.setForeground(Color.black);
        }
        for (JTextField field : fields) {
            if (field == captionField) {
                captionField.setText(category.getName());
            } else if (field == descriptionField) {
                descriptionField.setText(category.getDescription());
            }
        }
    }
}