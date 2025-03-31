package ui.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;

import exceptions.InvalidInputException;

import java.util.List;
import java.awt.event.ActionListener;

public abstract class AppPanelUI extends JPanel {
    protected ActionListener handler;
    protected JFrame window;

    //EFFECTS: creates a new AppPanelUI with a handler and window
    public AppPanelUI(ActionListener handler, JFrame window) {
        this.handler = handler;
        this.window = window;
    }

    //EFFECTS: tries to close the panel and returns relevant information the panel contains
    //         throws invalid input exception if the panel cannot get the correct data
    public abstract List<String> closePanel() throws InvalidInputException;

}
