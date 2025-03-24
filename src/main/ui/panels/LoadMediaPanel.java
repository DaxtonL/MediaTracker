package ui.panels;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import exceptions.InvalidInputException;

public class LoadMediaPanel extends AppPanelUI {
    private JComboBox<String> fileList;
    private JButton back;
    private JButton load;

    public LoadMediaPanel(ActionListener handler, JFrame window, String confirmID) {
        super(handler, window);
        this.handler = handler;
        back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(handler);
        load = new JButton("Load");
        load.setActionCommand(confirmID);
        load.addActionListener(handler);
        fileList = new JComboBox<String>(getSavedFileNames());

        add(back);
        add(load);
        add(fileList);
    }

    private String[] getSavedFileNames() {
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
        String[] fileArray = new String[fileNames.size()];
        fileNames.toArray(fileArray);
        return fileArray;
    }

    public String getSelectedFile() {
        return fileList.getSelectedItem().toString();
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        List<String> l = new ArrayList<String>();
        l.add(fileList.getSelectedItem().toString());
        return l;
    }
}
