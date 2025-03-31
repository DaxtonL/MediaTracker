package ui.panels;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.InvalidInputException;

public class YesNoPanel extends AppPanelUI {
    // EFFECTS: creates a new yes no panel with a handler, window, question, yesID and noID    
    public YesNoPanel(ActionListener handler, JFrame window, String question, String yesID, String noID) {
        super(handler, window);
        add(new JLabel(question));
        JButton noButton = new JButton("No");
        noButton.setActionCommand(noID);
        noButton.addActionListener(handler);
        add(noButton);

        JButton yesButton = new JButton("Yes");
        yesButton.setActionCommand(yesID);
        yesButton.addActionListener(handler);
        add(yesButton);
    }

    @Override
    // EFFECTS: the this panel does not return any data so returns invalid input exception if called
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No return data");
    }
}
