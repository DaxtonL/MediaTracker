package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Panel;
import java.io.IOException;

import javax.smartcardio.Card;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Media;
import model.MediaTracker;
import ui.MediaTrackerPanels.*;
import model.enums.MediaType;
import persistence.*;

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

    public MediaTrackerGUI() {
        // Initalizes the frame
        window = new JFrame("Media Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(windowX, windowY);
        window.setResizable(false);

        // Gets the media tracker
        this.tracker = getMediaTracker();

        // Initalizes the panels being used
        List<Media> medias = tracker.getFilterMedia(null); 
        mainPanel = new MainPanel(this, medias);

        cards = new JPanel(new CardLayout());
        cards.add(mainPanel, mainPanelString);

        // Finalizes and dispalys the panel
        window.add(cards);
        changePanel(mainPanelString);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void changePanel(String s) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, s);
    }

    private MediaTracker getMediaTracker() {
        JsonReader reader = new JsonReader("./data/" + path + ".json");
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
            addMediaPanel = new AddMediaPanel(this);
            cards.add(addMediaPanel, addPanelString);
            changePanel(addPanelString);
        } else if (e.getActionCommand().equals("save")) {
            saveMediaTracker();
        } else if (e.getActionCommand().equals("load")) {
            //stub
        } else if (e.getActionCommand().equals("add back")) {
            changePanel(mainPanelString);
        } else if (e.getActionCommand().equals("add done")) {
            try {
                Media m = addMediaPanel.tryDone();
                //System.out.println(m.listMediaInfo());
                updateMenuPanel(m);
                changePanel(mainPanelString);
            } catch (Exception exception) {
                return;
            }
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
        mainPanel = new MainPanel(this, medias);
        cards.add(mainPanel, mainPanelString);
        refreshFrame();
    }

    public void refreshFrame() {
        window.revalidate();
        window.repaint();
    }
}
