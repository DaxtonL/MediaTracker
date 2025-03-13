package ui.MediaTrackerPanels;


import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import model.Media;
import ui.MediaTrackerGUI;

public class MainPanel extends JPanel {
    MediaTrackerGUI handler;
    List<Media> mediaList;

    public MainPanel(MediaTrackerGUI handler, List<Media> mediaList) {
        this.handler = handler;
        this.mediaList = mediaList;
        setLayout(new BorderLayout());
   
        add(makeTopPanel(), BorderLayout.PAGE_START);
        add(listMedia(mediaList), BorderLayout.CENTER);
    }
 
    private JPanel makeTopPanel() {
        JPanel p = new JPanel();
        JButton mediaBtn = new JButton("Add Media");
        mediaBtn.setActionCommand("add");
        mediaBtn.addActionListener(handler);

        JButton b2 = new JButton("Load");
        b2.setActionCommand("load");
        b2.addActionListener(handler);

        JButton b3 = new JButton("Save");
        b3.setActionCommand("save");
        b3.addActionListener(handler);

        p.add(mediaBtn);
        p.add(b2);
        p.add(b3);

        return p;
    }

    private JScrollPane listMedia(List<Media> mediaList) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));

        MediaPanel[] panelsArray = new MediaPanel[mediaList.size()];
        for (Integer i = 0; i < mediaList.size(); i++) {
            panelsArray[i] = new MediaPanel(mediaList.get(i));
            list.add(new MediaPanel(mediaList.get(i)));
        }
        JScrollPane scrJPanel = new JScrollPane(list);

        return scrJPanel;
    }
}
