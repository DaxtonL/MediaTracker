package ui.panels;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.InvalidInputException;

public class YesNoPanel extends AppPanelUI {    
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
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No return data");
    }
}
