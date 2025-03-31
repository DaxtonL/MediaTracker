package ui.panels;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import exceptions.InvalidInputException;

public class TextFieldPanel extends AppPanelUI {
    private JTextField field;

    //EFFECTS: creates a textfieldPanel with hanlder, window, question and confirmID
    public TextFieldPanel(ActionListener handler, JFrame window, String question, String confirmID) {
        super(handler, window);
        add(new JLabel(question));
        field = new JTextField(20);
        add(field);
        JButton confirmButton = new JButton("Done");
        confirmButton.setActionCommand(confirmID);
        confirmButton.addActionListener(handler);
        add(confirmButton);
    }

    @Override
    //EFFECTS: returns the contents of the text field
    public List<String> closePanel() throws InvalidInputException {
        List<String> l = new ArrayList<String>();
        l.add(field.getText());
        return l;
    }
}
