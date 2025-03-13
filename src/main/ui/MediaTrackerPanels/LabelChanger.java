package ui.MediaTrackerPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import persistence.JsonReader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class LabelChanger extends JFrame implements ActionListener {
    private JLabel label;
    private JTextField field;
    private JComboBox<String> jbox;

    public LabelChanger() {
        super("The title");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 200));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        JButton btn = new JButton("Load");
        btn.setActionCommand("myButton");
        btn.addActionListener(this); // Sets "this" object as an action listener for btn
                                     // so that when the btn is clicked,
                                     // this.actionPerformed(ActionEvent e) will be called.
                                     // You could also set a different object, if you wanted
                                     // a different object to respond to the button click
        label = new JLabel("flag");
        add(btn);
        add(label);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        System.out.println(getSavedFileNames());

        List<String> fileList = getSavedFileNames();
        String[] fileArray = new String[fileList.size()];
        fileList.toArray(fileArray);
        jbox = new JComboBox<>(fileArray);
        add(jbox);
        repaint();
        revalidate();
        jbox.setActionCommand("myJbox");
        jbox.setSelectedIndex(0);
        jbox.addActionListener(this);

    }

    //This is the method that is called when the the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            label.setText(jbox.getSelectedItem().toString());
        }
        // if (e.getActionCommand().equals("myJbox")) {
        //     label.setText(jbox.getSelectedItem().toString());
        // }
    }

    public static void main(String[] args) {
        new LabelChanger();
    }

    private List<String> getSavedFileNames() {
        List<String> fileNames = new ArrayList<>();
        File folder = new File("./data/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String name = listOfFiles[i].getName();
                    if (name.contains(".json")) {
                        fileNames.add(listOfFiles[i].getName());
                        System.out.println(name);
                    }
                }
            }
        }
        return fileNames;
    }
}
