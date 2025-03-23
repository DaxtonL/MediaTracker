package ui.panels;


import java.awt.BorderLayout;
import model.MediaTracker;
import model.enums.MediaType;
import model.filters.FilterType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import exceptions.InvalidInputException;
import model.Media;
import model.filters.*;


public class MainPanel extends AppPanelUI implements ActionListener {
    private MediaTracker tracker;
    private String mediaConfirmID;
    private JComboBox filterType;
    private JScrollPane listMediaPanel;

    public MainPanel(ActionListener handler, JFrame window, MediaTracker tracker, String mediaConfirmID) {
        super(handler, window);
        setLayout(new BorderLayout());

        this.tracker = tracker;
        this.mediaConfirmID = mediaConfirmID;

        add(makeTopPanel(), BorderLayout.PAGE_START);
        listMediaPanel = listMedia();
        add(listMediaPanel, BorderLayout.CENTER);
    }
 
    private JPanel makeTopPanel() {
        JPanel p = new JPanel();
        JButton mediaBtn = new JButton("Add Media");
        mediaBtn.setActionCommand("add");
        mediaBtn.addActionListener(handler);

        JButton b2 = new JButton("Load");
        b2.setActionCommand("yes load tracker");
        b2.addActionListener(handler);

        JButton b3 = new JButton("Save");
        b3.setActionCommand("save");
        b3.addActionListener(handler);

        List<MediaType> types = new ArrayList<MediaType>(Arrays.asList(MediaType.values()));
        String[] typesArray = new String[types.size() + 1];
        typesArray[0] = "All Types";
        for (Integer i = 0; i < types.size(); i++) {
            String s = types.get(i).toString();
            typesArray[i + 1] = s.substring(0, 1) + s.substring(1, s.length()).toLowerCase();
        }
        filterType = new JComboBox<String>(typesArray);
        filterType.setActionCommand("changed filter");
        filterType.addActionListener(this);

        p.add(mediaBtn);
        p.add(b2);
        p.add(b3);
        p.add(filterType);

        return p;
    }

    private JScrollPane listMedia() {
        List<Media> mediaList = tracker.getFilterMedia(null);
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        
        //MediaPanel[] panelsArray = new MediaPanel[mediaList.size()];
        for (Integer i = 0; i < mediaList.size(); i++) {
            Media m = mediaList.get(i);
            //panelsArray[i] = new MediaPanel(handler, window, m);
            list.add(new MediaPanel(handler, window, mediaConfirmID, m));
        }

        JScrollPane scrJPanel = new JScrollPane(list);
        scrJPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrJPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrJPanel;
    }

    private JScrollPane listMedia(MediaType type) {
        FilterType f1 = new FilterType(type);
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(f1);
        List<Media> mediaList = tracker.getFilterMedia(filters);


        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        
        //MediaPanel[] panelsArray = new MediaPanel[mediaList.size()];
        for (Integer i = 0; i < mediaList.size(); i++) {
            Media m = mediaList.get(i);
            //panelsArray[i] = new MediaPanel(handler, window, m);
            list.add(new MediaPanel(handler, window, mediaConfirmID, m));
        }

        JScrollPane scrJPanel = new JScrollPane(list);
        scrJPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrJPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrJPanel;
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No return data");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("changed filter")) {
            String s = filterType.getSelectedItem().toString().toUpperCase();
            System.out.println(s);
            if (s.equals("ALL TYPES")) {
                remove(listMediaPanel);
                listMediaPanel = listMedia();
                add(listMediaPanel, BorderLayout.CENTER);
                window.repaint();
                window.revalidate();
                return;
            } else {
                MediaType type = MediaType.valueOf(filterType.getSelectedItem().toString().toUpperCase());
                remove(listMediaPanel);
                listMediaPanel = listMedia(type);
                add(listMediaPanel, BorderLayout.CENTER);
                window.repaint();
                window.revalidate();
                return;
            }
        }
    }
}
