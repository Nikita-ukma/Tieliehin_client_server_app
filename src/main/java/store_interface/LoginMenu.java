package store_interface;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import database_connection.ProductDB;
import database_connection.ProductGroupDB;
import front_elements.HintPasswordField;
import front_elements.HintTextField;
import front_elements.MainMenuButton;
import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JFrame {
    JPanel loginPanel;
    JFrame loginFrame;

    public LoginMenu() {
        loginFrame = this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init(this);
        URL imageUrl = getClass().getResource("logo.png");
        if (imageUrl != null) {
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(imageUrl));
        }
    }

    private void init(JFrame frame) {
        frame.setLayout(new BorderLayout());
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setUndecorated(true);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(20, 0, 0, 0)));
        loginPanel.setBackground(Color.WHITE);
        frame.add(loginPanel, BorderLayout.CENTER);

        JLabel panelName = new JLabel("Enter your username and password", SwingConstants.CENTER);
        panelName.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 18));
        panelName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        JLabel panelWelcome = new JLabel("Welcome!", SwingConstants.CENTER);
        panelWelcome.setForeground(Color.decode("#040075"));
        panelWelcome.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 24));
        panelWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(panelWelcome);
        headerPanel.add(panelName);
        loginPanel.add(headerPanel);
        JPanel LoginPanel = new JPanel();
        LoginPanel.setLayout(new BoxLayout(LoginPanel, BoxLayout.Y_AXIS));
        LoginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        LoginPanel.setBackground(Color.WHITE);

        Dimension fieldSize = new Dimension(300, 40);

        HintTextField userField = new HintTextField("Username");
        userField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        userField.setPreferredSize(fieldSize);
        userField.setMaximumSize(fieldSize);
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));


        LoginPanel.add(userField);

        LoginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        HintPasswordField passwordField = new HintPasswordField("Password");
        passwordField.setFont(new Font("Sitka Subheading Semibold", Font.PLAIN, 20));
        passwordField.setPreferredSize(fieldSize);
        passwordField.setMaximumSize(fieldSize);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        LoginPanel.add(passwordField);
        loginPanel.add(LoginPanel);
        Dimension fieldSize1 = new Dimension(288, 50);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 0));
        buttonPanel.setBackground(Color.WHITE);
        MainMenuButton loginButton = new MainMenuButton("Sign in");



        loginButton.setFont(new Font("Sitka Subheading Semibold", Font.BOLD, 20));
        loginButton.setPreferredSize(fieldSize1);
        loginButton.setMaximumSize(fieldSize);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                logIn(userField.getText(), new String(passwordField.getPassword()));
            }
        });
        buttonPanel.add(loginButton);
        loginPanel.add(buttonPanel);
    }

    private void logIn(String username, String password) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/storedb",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException | SQLException ignored) {
        }
        ProductGroupDB.setConnection(connection);
        ProductDB.setConnection(connection);
        if (checkPassword(username, password)) {
            loginFrame.getContentPane().removeAll();
            loginFrame.dispose();
            try {
                ProductTable.display(loginFrame);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            loginFrame.revalidate();
            loginFrame.repaint();
        }
    }

    private boolean checkPassword(String username, String password) {
        String usernameHash = hashMD5(username);
        String passwordHash = hashMD5(password);

        String expectedUsernameHash = "21232f297a57a5a743894a0e4a801fc3";
        String expectedPasswordHash = "96e79218965eb72c92a549dd5a330112";

        if (!usernameHash.equals(expectedUsernameHash)) {
            JOptionPane.showMessageDialog(new JFrame(), "Wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!passwordHash.equals(expectedPasswordHash)) {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid password");
            return false;
        } else {
            return true;
        }
    }

    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

