package front_elements;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HintTextField extends JTextField {
    private boolean showingHint;

    public HintTextField(final String hint) {
        super(hint);
        this.showingHint = true;
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("");
                    setForeground(Color.BLACK);
                    showingHint = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hint);
                    setForeground(Color.GRAY);
                    showingHint = true;
                }
            }
        });
    }

    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}
