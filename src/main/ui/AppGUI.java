package ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logging.*;
import model.Media;
import model.MediaTracker;
import model.enums.MediaType;
import model.enums.Status;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.panels.AddMediaPanel;
import ui.panels.AppPanelUI;
import ui.panels.EditMediaPanel;
import ui.panels.LoadMediaPanel;
import ui.panels.LogViewingPanel;
import ui.panels.MainPanel;
import ui.panels.TextFieldPanel;
import ui.panels.YesNoPanel;
import java.awt.event.WindowListener;

public class AppGUI implements ActionListener, WindowListener {
    JFrame window;
    final int windowX = 800;
    final int windowY = 400;

    MediaTracker tracker;

    JPanel masterPanel;
    AppPanelUI currentPanel; 

    final String mainPanelString = "main panel";
    final String addMediaPanelString = "add media panel";
    final String yesNoPanelString = "yes no panel";
    final String loadPanelString = "load panel";

    final String mediaEditID = "edit";
    final String mediaLogID = "log";

    // MODIFIES: this
    // EFFECTS: creates a new AppGui with a window, 
    // and sets the main panel to the ask user if they want to load a tracker
    public AppGUI() {
        // Initalizes the frame
        EventLog.getInstance().clear();
        window = new JFrame("Media Tracker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(windowX, windowY);
        window.setResizable(false);
        window.addWindowListener(this);

        masterPanel = new JPanel(new CardLayout());
        YesNoPanel p = new YesNoPanel(this, window, "Do you want to load a tracker?", 
                    "yes load tracker", "no load tracker");
        // MainPanel p = new MainPanel(this, window, tracker.getFilterMedia(null));
        switchPanel(p, "yes no load");

        window.add(masterPanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void switchPanel(AppPanelUI p, String s) {
        masterPanel.add(p, s);
        CardLayout cl = (CardLayout)(masterPanel.getLayout());
        cl.show(masterPanel, s);
        if (currentPanel != null) {
            masterPanel.remove(currentPanel);
        }
        currentPanel = p;
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

    private Boolean saveMediaTracker() {
        JsonWriter writer = new JsonWriter("./data/" + tracker.getName() + ".json");
        try {
            writer.open();
            writer.write(tracker);
            writer.close();
            //System.out.println("Succesfully saved media tracker!");
            return true;
        } catch (IOException e) {
            //System.out.println("Could not save media tracker!");
            return false;
        }
    }

    private AppPanelUI getMainPanel() {
        return new MainPanel(this, window, tracker, mediaEditID, mediaLogID);
    }

    @Override
    @SuppressWarnings("methodlength")
    public void actionPerformed(ActionEvent e) {
        String[] splitE = e.getActionCommand().split("[%]");
        if (e.getActionCommand().equals("yes load tracker")) {
            AppPanelUI p = new LoadMediaPanel(this, window, "load");
            switchPanel(p, "load panel");
        } else if (e.getActionCommand().equals("load")) {
            loadTracker();
        } else if (e.getActionCommand().equals("no load tracker")) {
            AppPanelUI p = new TextFieldPanel(this, window, "Input tracker name", "done tracker name");
            switchPanel(p, "tracker name");
        } else if (e.getActionCommand().equals("done tracker name")) {
            setTrackerName();
        } else if (e.getActionCommand().equals("add media")) {
            addMedia();
        } else if (e.getActionCommand().equals("add")) {
            AppPanelUI p = new AddMediaPanel(this, window, "add media");
            switchPanel(p, "add media");
        } else if (e.getActionCommand().equals("back")) {
            AppPanelUI p = getMainPanel();
            switchPanel(p, "main panel");
        } else if (e.getActionCommand().equals("save")) {
            saveMediaTracker();
        } else if (splitE[0].equals("edit")) {
            Media m = tracker.getMedia(splitE[1]);
            AppPanelUI p = new EditMediaPanel(this, window, "edit media", m);
            switchPanel(p, "edit media");
        }   else if (splitE[0].equals("log")) {
            Media m = tracker.getMedia(splitE[1]);
            AppPanelUI p = new LogViewingPanel(this, window, "log media", m);
            switchPanel(p, "log media");
        } else if (e.getActionCommand().equals("edit media")) {
            editMedia();
        }
    }

    private void loadTracker() {
        try {
            tracker = getMediaTracker(currentPanel.closePanel().get(0));
            AppPanelUI p = getMainPanel();
            switchPanel(p, "main panel");
        } catch (Exception exception) {
            return;
        }
    }

    private void editMedia() {
        try {
            List<String> l = currentPanel.closePanel();
            updateMedia(l);
            AppPanelUI p = getMainPanel();
            switchPanel(p, "main panel");   
        } catch (Exception exception) {
            return;
        }
    }

    private void setTrackerName() {
        try {
            String s = currentPanel.closePanel().get(0);
            tracker = new MediaTracker(s);
            AppPanelUI p = getMainPanel();
            switchPanel(p, "main panel");
        } catch (Exception exception) {
            return;
        }
    }

    @SuppressWarnings("methodlength")
    private void updateMedia(List<String> l) {
        Media m = tracker.getMedia(l.get(6));

        String newName = l.get(0);
        MediaType newType = MediaType.valueOf(l.get(1));
        Integer newLength = Integer.parseInt(l.get(2));
        Integer newPriority = Integer.parseInt(l.get(3));
        Status newStatus = Status.valueOf(l.get(4));
        Integer newRating = Integer.parseInt(l.get(5));

        if (!m.getName().equals(newName)) {
            m.setName(newName);
        }
        if (!m.getType().equals(newType)) {
            m.setType(newType);
        }
        if (!m.getLength().equals(newLength)) {
            m.setLength(newLength);
        }
        if (!m.getPriority().equals(newPriority)) {
            m.setPriority(newPriority);
        }
        if (!m.getStatus().equals(newStatus)) {
            m.setStatus(newStatus);
        }
        if (!m.getRating().equals(newRating)) {
            m.setRating(newRating);
        }
    }

    private void addMedia() {
        try {
            List<String> l = currentPanel.closePanel();
            Media m = new Media(l.get(0), MediaType.valueOf(l.get(1)), 
                    Integer.parseInt(l.get(2)), Integer.parseInt(l.get(3)));
            tracker.addMedia(m);
            AppPanelUI p = getMainPanel();
            switchPanel(p, "main panel");
        } catch (Exception exception) {
            return;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // stub
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Iterator<Event> it = EventLog.getInstance().iterator();
        while (it.hasNext()) {
            Event event = it.next();
            System.out.println(event.getDescription());
        }
        System.out.println("Closed app");
        // stub
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
        // stub
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // stub
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // stub
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // stub
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // stub
    }
}
