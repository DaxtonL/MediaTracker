package ui.panels;

import model.Media;

import javax.swing.*;

import exceptions.InvalidInputException;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MediaPanel extends AppPanelUI {
    private Label name;
    private Label type;
    private Label status;
    private Label progress;
    private Label priority;
    private Label rating;

    private JButton edit;
    private JButton log;

    private Media media;

    // Creates a media panel to represent the inputted media
    public MediaPanel(ActionListener handler, JFrame window, String editID, String logID, Media m) {
        super(handler, window);
        this.media = m;
        setPreferences();
        
        addLabels();

        this.edit = new JButton("Edit");
        edit.setActionCommand(editID + "%" + m.getName());
        edit.addActionListener(handler);
        add(edit);
        this.log = new JButton("Log");
        log.setActionCommand(logID + "%" + m.getName());
        log.addActionListener(handler);
        add(log);
    }

    //EFFECTS: adds all the relavant labels to this panel
    private void addLabels() {
        List<String> mediaInfo = media.listMediaInfo();
        this.name = new Label(mediaInfo.get(0));
        add(name);
        this.type = new Label(mediaInfo.get(1));
        add(type);
        this.status = new Label(mediaInfo.get(2));
        add(status);
        this.progress = new Label("Progress: " + mediaInfo.get(3) + "/" + mediaInfo.get(4) + " " + mediaInfo.get(5));
        add(progress);
        this.priority = new Label("Priorty: " + mediaInfo.get(6));
        add(priority);
        this.rating = new Label("Rating" + mediaInfo.get(7));
        add(rating);
    }

    //MODIFIES: this
    //EFFECTS: sets the layout preferences for this panel
    private void setPreferences() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setPreferredSize(new Dimension(900, 40)); // Reduce height to remove excess space
        setMaximumSize(new Dimension(900, 40)); // Prevent stretching
    }

    @Override
    //EFFECTS: this panel does not contain any data to return so throws exception if called
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No valid data");
    }

    public Media getMedia() {
        return media;
    }
}
