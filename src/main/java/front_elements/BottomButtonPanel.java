package front_elements;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.PAGE_END;

public class BottomButtonPanel extends JPanel {

    private final JButton editButton;
    private final JButton saveButton;
    private JButton deleteButton;
    private final JButton cancelButton;

    public BottomButtonPanel(boolean shouldIncludeDelete){
        this.setLayout(new GridBagLayout());
        editButton = new JButton("Edit");
        editButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        GridBagConstraints c=new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.fill=HORIZONTAL;
        c.insets = new Insets(10,0,2,0);
        editButton.setBackground(Color.green);
        this.add(editButton,c);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        c.gridx = 1;
        saveButton.setBackground(Color.lightGray);
        saveButton.setEnabled(false);
        this.add(saveButton,c);
        if(shouldIncludeDelete){
            deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            c.gridx = 2;
            deleteButton.setBackground(Color.red);
            this.add(deleteButton,c);
        }

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        c.gridx = 3;
        c.anchor= PAGE_END;
        c.insets = new Insets(10,50,2,0);
        cancelButton.setBackground(Color.cyan);

        this.add(cancelButton,c);
    }
    public void setEnabled(boolean condition){
        saveButton.setBackground(condition ? Color.green : Color.lightGray);
        saveButton.setEnabled(condition);
        editButton.setEnabled(!condition);
        editButton.setBackground(condition ? Color.lightGray : Color.green);
    }
    public JButton getDeleteButton() {
        return deleteButton;
    }
    public JButton getEditButton() {
        return editButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
    public JButton getSaveButton() {
        return saveButton;
    }




}
