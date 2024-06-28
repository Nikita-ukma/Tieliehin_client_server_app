package front_elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class plusminusButton extends JButton {

    private final Color hoverBackgroundColor;
    private final Color normalBackgroundColor;

    public plusminusButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(12, 0, 10, 0));
        normalBackgroundColor = Color.WHITE;
        hoverBackgroundColor = Color.decode("#F5F5F5");
        setBackground(normalBackgroundColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.RED);
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.decode("#040075"));
                setBackground(normalBackgroundColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - 1, height - 1, height, height);
        g2.setColor(Color.decode("#040075"));
        g2.drawRoundRect(0, 0, width - 1, height - 1, height, height);
        g2.drawRoundRect(0, 0, width - 1, height - 1, height - 1, height - 1);
        super.paintComponent(graphics);
    }
}