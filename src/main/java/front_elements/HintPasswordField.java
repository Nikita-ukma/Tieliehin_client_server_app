package front_elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HintPasswordField extends JPasswordField {
    private boolean showingHint;

    public HintPasswordField(final String hint) {
        super(hint);
        this.showingHint = true;
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText("");
                    setForeground(Color.BLACK);
                    showingHint = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText(hint);
                    setForeground(Color.GRAY);
                    showingHint = true;
                }
            }
        });
    }

    @Override
    public char[] getPassword() {
        return showingHint ? new char[0] : super.getPassword();
    }
}