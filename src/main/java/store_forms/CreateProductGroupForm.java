package store_forms;

import database_connection.ProductGroupDB;
import front_elements.ErrorChecker;
import front_elements.MainMenuButton;
import front_elements.Switch;
import entity.ProductGroup;
import server_work.Packet;
import server_work.PacketHelper;
import store_interface.ProductGroupTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static javax.swing.SwingConstants.HORIZONTAL;

public class CreateProductGroupForm extends JFrame {

    private JPanel backPanel;
    private JTextField captionField;
    private JTextField descriptionField;
    private static int nextId;

    static {
        try {
            nextId = ProductGroupDB.getMaxId() + 1;
        } catch (Exception e) {
            nextId = 1;
        }
    }

    public CreateProductGroupForm(DefaultTableModel model, JFrame frame) {
        super("Add New Product Group");
        this.setSize(400, 600);
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

        JLabel captionLabel = new JLabel("Create New Product Group");
        captionLabel.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 24));
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionPanel.add(captionLabel);
        captionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        backPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        JLabel groupNameLabel = new JLabel("Group name: ");
        groupNameLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add(groupNameLabel, c);

        captionField = new JTextField(20);
        captionField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridx = 1;
        c.fill = BOTH;
        mainPanel.add(captionField, c);

        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add(descriptionLabel, c);

        descriptionField = new JTextField(20);
        descriptionField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        c.gridx = 1;
        c.fill = BOTH;
        mainPanel.add(descriptionField, c);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.fill = HORIZONTAL;
        c.insets = new Insets(10, 5, 5, 0);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,10,0,10));

        MainMenuButton createButton = new MainMenuButton("Create");
        createButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e -> handleCreateButton(model, frame));
        buttonPanel.add(createButton, c);

        MainMenuButton cancelButton = new MainMenuButton("Cancel");
        cancelButton.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.addActionListener(e -> {
            Switch.switchFramesForProductGroup(frame, model);
            dispose();
        });
        c.fill = HORIZONTAL;
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_END;
        buttonPanel.add(cancelButton, c);

        backPanel.add(mainPanel);
        backPanel.add(buttonPanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Switch.switchFramesForProductGroup(frame, model);
                dispose();
            }
        });
    }

    private void handleCreateButton(DefaultTableModel model, JFrame frame) {
        final int maxLength = 50;
        ErrorChecker.tFields = new ArrayList<>(Collections.singletonList(captionField));
        List<String> errors = ErrorChecker.checkForEmptyErrors();
        if (errors != null) {
            showError(errors.get(0), ErrorChecker.getErrorTextFields(errors));
        } else if (ErrorChecker.checkForLength(maxLength, 0)) {
            showError("Caption is too long. Maximum: 50 symbols.", new JTextField[]{captionField});
        } else if (descriptionField.getText().length() > maxLength) {
            showError("Description is too long. Maximum: 50 symbols.", new JTextField[]{descriptionField});
        } else {
            try {
                sendCreatePacketToServer(model, frame);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dispose();
        }
    }

    private void sendCreatePacketToServer(DefaultTableModel model, JFrame frame) throws Exception {
        ProductGroup productGroup = new ProductGroup(nextId++, captionField.getText(), descriptionField.getText());
        Packet packet = PacketHelper.createProductGroupPacket(productGroup);

        try (Socket socket = new Socket("localhost", 8765);
             OutputStream out = socket.getOutputStream()) {
            out.write(packet.toBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ProductGroupTable.getProductGroupList().add(productGroup);
        Switch.switchFramesForProductGroup(frame, model);
    }

    public void start(DefaultTableModel model, JFrame frame) {
        init(model, frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

    private void showError(String text, JTextField[] fields) {
        for (JTextField field : fields) {
            field.setBackground(Color.red);
        }
        JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
        for (JTextField field : fields) {
            field.setBackground(Color.white);
            field.setText("");
        }
    }
}