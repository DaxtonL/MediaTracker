package ui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.enums.MediaType;
import model.enums.Status;
import model.Media;



public class EditMediaPanel extends AddMediaPanel {
    private JPanel ratingPanel;
    private JComboBox<String> ratingSelector;
    private JCheckBox ratingBox;

    private JPanel statusPanel;
    private JComboBox<String> satusSelector;

    //EFFECTS: creates an editmediapanel with a handler, window and confirmid
    public EditMediaPanel(ActionListener handler, JFrame window, String confirmID, Media m) {
        super(handler, window, confirmID, m);
    }

    @Override
    //EFFECTS: adds components to the panel
    protected void addComponents() {
        statusPanel = makeStatusPanel();
        add(statusPanel);
        super.addComponents();
        ratingPanel = makeRatingPanel();
        add(ratingPanel);
    }

    @Override
    //EFFECTS: creates the panel for editing the media name
    protected JPanel makeNamePanel() {
        JPanel p = super.makeNamePanel();
        nameField.setText(media.getName());
        return p;
    }

    @Override
    //EFFECTS: creates the panel for editing the media type
    protected JPanel makeTypePanel() {
        JPanel p = super.makeTypePanel();
        List<MediaType> types = new ArrayList<MediaType>(Arrays.asList(MediaType.values()));
        Integer selectedIndex = 0;
        for (Integer i = 0; i < types.size(); i++) {
            String s = types.get(i).toString();
            if (media.getType().toString().equals(s)) {
                selectedIndex = i;
            }
        }
        typeField.setSelectedIndex(selectedIndex);
        return p;
    }

    @Override
    //EFFECTS: creates the panel for editing the media length
    protected JPanel makeLengthPanel() {
        JPanel p = super.makeLengthPanel();
        if (media.getLength() != -1) {
            lengthField.setText(media.getLength().toString());
            lengthBox.setSelected(true);
        }
        return p;
    }

    @Override
    //EFFECTS: creates the panel for editing the media priority
    protected JPanel makePriorityPanel() {
        JPanel p = super.makePriorityPanel();
        if (media.getPriority() != -1) {
            priorityField.setText(media.getPriority().toString());
            priorityBox.setSelected(true);
        }
        return p;
    }

    //EFFECTS: creates the panel for editing the media rating
    private JPanel makeRatingPanel() {
        JPanel p = new JPanel();
        editLayout(p);

        String[] ratings = new String[10];
        Integer selectedIndex = 0;
        for (Integer i = 0; i < 10; i++) {
            ratings[9 - i] = Integer.toString(i + 1);
            if ((media.getRating() != -1) && i + 1 == media.getRating()) {
                selectedIndex = i;
            }
        }

        ratingBox = new JCheckBox();
        ratingBox.addItemListener(this);
        p.add(ratingBox);

        ratingSelector = new JComboBox<>(ratings);
        p.add(ratingSelector);

        JLabel ratingText = new JLabel("Rating");
        p.add(ratingText);


        if (media.getRating() == -1) {
            ratingSelector.setVisible(false);
        } else {
            ratingSelector.setSelectedIndex(selectedIndex);
        }

        return p;
    }

    //EFFECTS: creates the panel for editing the media status
    private JPanel makeStatusPanel() {
        JPanel p = new JPanel();

        editLayout(p);

        List<Status> status = new ArrayList<Status>(Arrays.asList(Status.values()));
        String[] statusArray = new String[status.size()];
        Integer selectedIndex = 0;
        for (Integer i = 0; i < status.size(); i++) {
            String s = status.get(i).toString();
            statusArray[i] = s.substring(0, 1) + s.substring(1, s.length()).toLowerCase();
            if (media.getStatus().toString().equals(s)) {
                selectedIndex = i;
            }
        }

        JLabel typeText = new JLabel("Status");
        satusSelector = new JComboBox<String>(statusArray);
        satusSelector.setSelectedIndex(selectedIndex);
        p.add(typeText);
        p.add(satusSelector);

        return p;
    }

    @Override
    //EFFECTS: handles when the checkboxes are selected or unselected and displays or hides the relevant field
    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e);
        if (e.getSource() == ratingBox) {
            if (e.getStateChange() == 1) {
                ratingSelector.setVisible(true);
                //ratingPanel.add(ratingSelector);
                super.refreshFrame();
            } else {
                ratingSelector.setVisible(false);
                //ratingPanel.remove(ratingSelector);
                refreshFrame();
            }
        }
    }

    @Override
    //EFFECTS: returns the new data for a peice of media based on the fields
    protected List<String> outputVal() {
        List<String> l = super.outputVal();
        l.add(satusSelector.getSelectedItem().toString().toUpperCase());  
        l.add(ratingBox.isSelected() ? ratingSelector.getSelectedItem().toString().toUpperCase() : "-1");
        l.add(media.getName());            
        return l;
    }

    //EFFECTS: changes the layout preferences for inputted panel p
    protected void editLayout(JPanel p) {
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching
    }
}
