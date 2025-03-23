package ui.panels;

import java.awt.PageAttributes.MediaType;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import exceptions.InvalidInputException;

public class LogViewingPanel extends AppPanelUI {
    private String confirmID;

    public LogViewingPanel(ActionListener handler, JFrame window, String confirmID, Media m) {
        super(handler, window);
        this.confirmID = confirmID;
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closePanel'");
    }

}
