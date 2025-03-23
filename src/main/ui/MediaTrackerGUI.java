package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Panel;
import java.io.IOException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Media;
import model.MediaTracker;
import persistence.*;
import ui.panels.*;

public class MediaTrackerGUI implements ActionListener {
    JFrame window;
    final int windowX = 800;
    final int windowY = 400;
    
    final String path = "test tracker";
    MediaTracker tracker;

    JPanel cards;
    MainPanel mainPanel;
    final String mainPanelString = "main menu";
    AddMediaPanel addMediaPanel;
    final String addPanelString = "add media menu";
    LoadMediaPanel loadMediaPanel;
    final String loadPanelString = "load media";

    JTextField trackerNameField;

    public MediaTrackerGUI() {
        // Initalizes the frame
        window = new JFrame("Media Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(windowX, windowY);
        window.setResizable(false);

        // Gets the media tracker
        this.tracker = getMediaTracker(path + ".json");

        // Initalizes the panels being used
        JPanel askLoad = askLoadPanel();
        List<Media> medias = tracker.getFilterMedia(null); 
        //mainPanel = new MainPanel(this, medias);

        cards = new JPanel(new CardLayout());
        cards.add(mainPanel, mainPanelString);
        cards.add(askLoad, "ask load");

        // Finalizes and dispalys the panel
        window.add(cards);
        changePanel("ask load");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void changePanel(String s) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, s);
    }

    private MediaTracker getMediaTracker(String path) {
        JsonReader reader = new JsonReader("./data/" + path);
        MediaTracker tracker;
        try {
            tracker = reader.read();
        } catch (IOException e) {
            tracker = null;
        }

        return tracker;
    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add")) {
            //addMediaPanel = new AddMediaPanel(this);
            //cards.add(addMediaPanel, addPanelString);
            changePanel(addPanelString);
        } else if (e.getActionCommand().equals("save")) {
            saveMediaTracker();
        } else if (e.getActionCommand().equals("load")) {
            //loadMediaPanel = new LoadMediaPanel(this);
            cards.add(loadMediaPanel, loadPanelString);
            changePanel(loadPanelString);
        } else if (e.getActionCommand().equals("back")) {
            changePanel(mainPanelString);
        } else if (e.getActionCommand().equals("add media")) {
            try {
                //Media m = addMediaPanel.tryDone();
                //System.out.println(m.listMediaInfo());
                //updateMenuPanel(m);
                changePanel(mainPanelString);
            } catch (Exception exception) {
                return;
            }
        } else if (e.getActionCommand().equals("load media")) {
            tracker = getMediaTracker(loadMediaPanel.getSelectedFile());
            updateMenuPanel();
            changePanel(mainPanelString);
        }  else if (e.getActionCommand().equals("go to name tracker")) {
            JPanel nameTracker = askNameTrackerPanel();
            cards.add(nameTracker, "name tracker panel");
            changePanel("name tracker panel");
        }  else if (e.getActionCommand().equals("name tracker")) {
            tracker = new MediaTracker(trackerNameField.getText());
            updateMenuPanel();
            changePanel(mainPanelString);
        }
    }

    private Boolean saveMediaTracker() {
        JsonWriter writer = new JsonWriter("./data/" + tracker.getName() + ".json");
        try {
            writer.open();
            writer.write(tracker);
            writer.close();
            System.out.println("Succesfully saved media tracker!");
            return true;
        } catch (IOException e) {
            System.out.println("Could not save media tracker!");
            return false;
        }
    }

    private void updateMenuPanel(Media m) {
        cards.remove(mainPanel);
        tracker.addMedia(m);
        List<Media> medias = tracker.getFilterMedia(null);
        //mainPanel = new MainPanel(this, medias);
        cards.add(mainPanel, mainPanelString);
        refreshFrame();
    }

    private void updateMenuPanel() {
        cards.remove(mainPanel);
        List<Media> medias = tracker.getFilterMedia(null);
        //mainPanel = new MainPanel(this, medias);
        cards.add(mainPanel, mainPanelString);
        refreshFrame();
    }

    public void refreshFrame() {
        window.revalidate();
        window.repaint();
    }

    private JPanel askLoadPanel() {
        JPanel p = new JPanel();
        JLabel label = new JLabel("Do you want to load a file?");
        JButton no = new JButton("No");
        no.setActionCommand("go to name tracker");
        no.addActionListener(this);
        JButton yes = new JButton("Yes");
        yes.setActionCommand("load");
        yes.addActionListener(this);

        p.add(label);
        p.add(no);
        p.add(yes);
        return p;
    }
    
    private JPanel askNameTrackerPanel() {
        JPanel p = new JPanel();
        JLabel label = new JLabel("Enter new media tracker name");
        trackerNameField = new JTextField(20);
        JButton done = new JButton("Done");
        done.setActionCommand("name tracker");
        done.addActionListener(this);

        p.add(label);
        p.add(trackerNameField);
        p.add(done);
        return p;
    }
}
