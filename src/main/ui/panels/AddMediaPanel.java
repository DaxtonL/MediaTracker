package ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import exceptions.InvalidInputException;
import model.enums.MediaType;
import model.Media;

public class AddMediaPanel extends AppPanelUI implements ItemListener {    
    protected JTextField nameField;
    protected JComboBox<String> typeField;
    protected JFormattedTextField lengthField;
    protected JCheckBox lengthBox;
    private JPanel lengthPanel;
    protected JFormattedTextField priorityField;
    protected JCheckBox priorityBox;
    private JPanel priortyPanel;

    private String confirmID;
    protected Media media;

    //EFFECTS: Creates a new media panel with a handlder, window and confirm ID 
    public AddMediaPanel(ActionListener handler, JFrame window, String confirmID) {
        super(handler, window);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        this.confirmID = confirmID;
        add(makeNamePanel());
        add(makeTypePanel());
        addComponents();
        add(makeMenuPanel());
    }

    //EFFECTS: overloaded constructor that additionally takes in a media m
    public AddMediaPanel(ActionListener handler, JFrame window, String confirmID, Media m) {
        super(handler, window);
        media = m;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        this.confirmID = confirmID;
        add(makeNamePanel());
        add(makeTypePanel());
        addComponents();
        add(makeMenuPanel());
    }
    
    //EFFECS: protected field for adding components to the panel
    protected void addComponents() {
        lengthPanel = makeLengthPanel();
        add(lengthPanel);
        priortyPanel = makePriorityPanel();
        add(priortyPanel);
    }

    //EFFECTS: creates and returns a panel with a label and appropriate field to input the name value
    protected JPanel makeNamePanel() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching

        JLabel nameText = new JLabel("Name");
        nameField = new JTextField(20);
        p.add(nameText);
        p.add(nameField);
        
        return p;
    }

    //EFFECTS: creates and returns a panel with a label and appropriate field to input the type value
    protected JPanel makeTypePanel() {
        JPanel p = new JPanel();

        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching


        List<MediaType> types = new ArrayList<MediaType>(Arrays.asList(MediaType.values()));
        String[] typesArray = new String[types.size()];
        for (Integer i = 0; i < types.size(); i++) {
            String s = types.get(i).toString();
            typesArray[i] = s.substring(0, 1) + s.substring(1, s.length()).toLowerCase();
        }

        JLabel typeText = new JLabel("Media Type");
        typeField = new JComboBox<String>(typesArray);
        p.add(typeText);
        p.add(typeField);

        return p;
    }

    //EFFECTS: creates and returns a panel with a label and appropriate field to input the length value
    protected JPanel makeLengthPanel() {
        NumberFormat longFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
        numberFormatter.setMinimum(1); //Optional
        numberFormatter.setAllowsInvalid(false); //this is the key!!

        JPanel p = new JPanel();

        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching


        lengthBox = new JCheckBox("Length");
        lengthBox.addItemListener(this);
        lengthField = new JFormattedTextField(longFormat);
        lengthField.setColumns(5);

        p.add(lengthBox);
        p.add(lengthField);
        lengthField.setVisible(false);

        return p;
    }

    //EFFECTS: creates and returns a panel with a label and appropriate field to input the priority value
    protected JPanel makePriorityPanel() {
        NumberFormat longFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
        numberFormatter.setMinimum(1); //Optional
        numberFormatter.setAllowsInvalid(false); //this is the key!!

        JPanel p = new JPanel();

        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching


        priorityBox = new JCheckBox("Priority");
        priorityBox.addItemListener(this);
        priorityField = new JFormattedTextField(longFormat);
        priorityField.setColumns(5);

        p.add(priorityBox);
        p.add(priorityField);
        priorityField.setVisible(false);

        return p;
    }

    //EFFECTS: creates and returns a panel buttons for confirming and going back
    private JPanel makeMenuPanel() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setPreferredSize(new Dimension(350, 40)); // Reduce height to remove excess space
        p.setMaximumSize(new Dimension(350, 40)); // Prevent stretching
        JButton back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(handler);
        JButton done = new JButton("Done");
        done.setActionCommand(confirmID);
        done.addActionListener(handler);

        p.add(back);
        p.add(done);

        return p;
    }

    // public Media tryDone() throws FailureToCompleteOperationException {
    //     Boolean namePass = !nameField.getText().trim().isEmpty();
    //     Boolean lengthPass = (!lengthField.getText().trim().isEmpty() 
    //                     && Integer.parseInt(lengthField.getText().trim()) > 0)
    //                     || !lengthBox.isSelected();
    //     Boolean priorityPass = (!priorityField.getText().trim().isEmpty() 
    //                     && Integer.parseInt(priorityField.getText().trim()) > 0)
    //                     || !priorityBox.isSelected();
        
    //     if (namePass && lengthPass && priorityPass) {
    //         return new Media(nameField.getText(), 
    //             MediaType.valueOf(typeField.getSelectedItem().toString().toUpperCase()), 
    //             lengthBox.isSelected() ? Integer.parseInt(lengthField.getText().trim()) : -1, 
    //             priorityBox.isSelected() ? Integer.parseInt(priorityField.getText().trim()) : -1);
    //     } else {
    //         changeFieldColors(namePass, lengthPass, priorityPass);
    //         throw new FailureToCompleteOperationException("Not all fields are valid");
    //     }
    // }

    //EFFECTS: changes the colour of text fields that do not have valid data within them
    protected void changeFieldColors(Boolean namePass, Boolean lengthPass, Boolean priorityPass) {
        nameField.setBackground(!namePass ? Color.decode("#ffb09c") 
                        : UIManager.getColor("TextField.background"));
        lengthField.setBackground(!lengthPass ? Color.decode("#ffb09c") 
                    : UIManager.getColor("TextField.background"));
        priorityField.setBackground(!priorityPass ? Color.decode("#ffb09c") 
                    : UIManager.getColor("TextField.background"));

        refreshFrame();
        Timer timer = new Timer(800, e -> {
            nameField.setBackground(UIManager.getColor("TextField.background"));
            lengthField.setBackground(UIManager.getColor("TextField.background"));
            priorityField.setBackground(UIManager.getColor("TextField.background"));
            refreshFrame();
        });
        timer.setRepeats(false);
        timer.start();
    }

    //EFFECTS: handles the event that a checkbox is toggled to either show or hide the relevent textfield
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == lengthBox) {
            if (e.getStateChange() == 1) {
                lengthField.setVisible(true);
                refreshFrame();
            } else {
                lengthField.setVisible(false);
                refreshFrame();
            }
        } else if (e.getSource() == priorityBox) {
            if (e.getStateChange() == 1) {
                priorityField.setVisible(true);
                refreshFrame();
            } else {
                priorityField.setVisible(false);
                refreshFrame();
            }
        }
    }

    @Override
    //EFFECTS: tries to close this panel, if the fields are invalid, throws invalid input exception instead
    public List<String> closePanel() throws InvalidInputException {
        Boolean namePass = namePass();
        Boolean lengthPass = lengthPass();
        Boolean priorityPass = priorityPass();
        
        if (namePass && lengthPass && priorityPass) { 
            return outputVal();
        } else {
            changeFieldColors(namePass, lengthPass, priorityPass);
            throw new InvalidInputException("Not all fields are valid");
        }
    }

    //EFFECTS: returns the relevant data inputted in the fields
    protected List<String> outputVal() {
        List<String> l = new ArrayList<String>();
        l.add(nameField.getText());
        l.add(typeField.getSelectedItem().toString().toUpperCase()); 
        l.add(Integer.toString(lengthBox.isSelected() ? Integer.parseInt(lengthField.getText().trim()) : -1));
        l.add(Integer.toString(priorityBox.isSelected() ? Integer.parseInt(priorityField.getText().trim()) : -1));
        return l;
    }

    //EFFECTS: returns true if the name field has valid data, false otherwise
    private Boolean namePass() {
        return !nameField.getText().trim().isEmpty();
    }

    //EFFECTS: returns true if the length field has valid data, false otherwise
    private Boolean lengthPass() {
        return (!lengthField.getText().trim().isEmpty() 
            && Integer.parseInt(lengthField.getText().trim()) > 0)
            || !lengthBox.isSelected();
    }

    //EFFECTS: returns true if the priority field has valid data, false otherwise
    private Boolean priorityPass() {
        return (!priorityField.getText().trim().isEmpty() 
            && Integer.parseInt(priorityField.getText().trim()) > 0)
            || !priorityBox.isSelected();
    }

    //MODIES: window
    //EFFECTS: repaints the winodw to display updated info
    protected void refreshFrame() {
        window.repaint();
        window.revalidate();
    }
}
