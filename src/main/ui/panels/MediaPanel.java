package ui.panels;

import model.Media;

import javax.swing.*;

import exceptions.InvalidInputException;

import java.awt.*;
import java.awt.event.ActionEvent;
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

    public MediaPanel(ActionListener handler, JFrame window, String confirmID, Media m) {
        super(handler, window);
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setPreferredSize(new Dimension(900, 40)); // Reduce height to remove excess space
        setMaximumSize(new Dimension(900, 40)); // Prevent stretching
        List<String> mediaInfo = m.listMediaInfo();
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

        this.edit = new JButton("Edit");
        edit.setActionCommand(confirmID + "%" + m.getName());
        edit.addActionListener(handler);
        add(edit);
        this.log = new JButton("Log");
        log.setActionCommand("log media");
        log.addActionListener(handler);
        add(log);
        this.media = m;
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No valid data");
    }

    public Media getMedia() {
        return media;
    }
}
