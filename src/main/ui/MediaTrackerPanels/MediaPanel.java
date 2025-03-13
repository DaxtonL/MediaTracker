package ui.MediaTrackerPanels;

import model.Media;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MediaPanel extends JPanel implements ActionListener {
    private Label name;
    private Label type;
    private Label status;
    private Label progress;
    private Label priority;
    private Label rating;

    private JButton edit;
    private JButton log;


    public MediaPanel(Media m) {
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
        add(edit);
        this.log = new JButton("Log");
        add(log);
    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        //stub
    }
}
