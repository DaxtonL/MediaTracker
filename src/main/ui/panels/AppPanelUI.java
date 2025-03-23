package ui.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;

import exceptions.InvalidInputException;

import java.util.List;
import java.awt.event.ActionListener;

public abstract class AppPanelUI extends JPanel {
    protected ActionListener handler;
    protected JFrame window;


    public AppPanelUI(ActionListener handler, JFrame window) {
        this.handler = handler;
        this.window = window;
    }

    public abstract List<String> closePanel() throws InvalidInputException;

}
