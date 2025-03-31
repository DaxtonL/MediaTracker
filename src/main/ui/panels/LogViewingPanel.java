package ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.NumberFormatter;

import exceptions.InvalidInputException;
import model.Media;
import model.ViewLog;

public class LogViewingPanel extends AppPanelUI implements ActionListener {
    private Media media;
    private JScrollPane listLog;
    private JFormattedTextField amountField;
    private JPanel addPanel;
    private JPanel topPanel;

    //EFFECTS: creates a log viewing panel with a handler, window, confirmID and media
    public LogViewingPanel(ActionListener handler, JFrame window, String confirmID, Media m) {
        super(handler, window);
        setLayout(new BorderLayout());
        //this.confirmID = confirmID;
        media = m;
        listLog = listLog();
        topPanel = topPanel();
        add(topPanel, BorderLayout.PAGE_START);
        add(listLog, BorderLayout.CENTER);
    }

    //EFFECT: creates and returns the "top panel" of this panel with relevant buttons and labels
    private JPanel topPanel() {
        JPanel p = new JPanel();
        JButton back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(handler);
        JButton log = new JButton("New Log");
        log.setActionCommand("new log");
        log.addActionListener(this);
        
        JButton deleteButton = new JButton("Delete last log");
        deleteButton.setActionCommand("delete log");
        deleteButton.addActionListener(this);

        p.add(back);
        p.add(log);
        p.add(deleteButton);
        return p;
    }

    //EFFECTS: returns a JScrollPane with a list of the viewings of the selected media
    private JScrollPane listLog() {
        List<ViewLog> log = media.getLog();
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        
       // LogPanel[] panelsArray = new LogPanel[log.size()];
        for (Integer i = 0; i < log.size(); i++) {
            ViewLog v = log.get(i);
            //panelsArray[i] = new LogPanel(handler, window, v, media.getType());
            list.add(new LogPanel(handler, window, v, media.getType()));
        }

        JScrollPane scrJPanel = new JScrollPane(list);
        scrJPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrJPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrJPanel;
    }

    @Override
    //EFFECTS this panel does not have any data to return so throws exception if this method is called
    public List<String> closePanel() throws InvalidInputException {
        throw new UnsupportedOperationException("Unimplemented method 'closePanel'");
    }

    @Override
    //EFFECTS handles the events when buttons are pressed
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().toString().equals("new log")) {
            addPanel = makeNewLog();
            switchTopPanel(topPanel, addPanel);
            refreshWindow();
        } else if (e.getActionCommand().toString().equals("cancel")) {
            switchTopPanel(addPanel, topPanel);
            refreshWindow();
        } else if (e.getActionCommand().toString().equals("add log")) {
            ViewLog l = new ViewLog(LocalDate.now(), Integer.parseInt(amountField.getText()));
            media.logViewing(l);
            relistLog();
            remove(addPanel);
            switchTopPanel(addPanel, topPanel);
            refreshWindow();
        } else if (e.getActionCommand().toString().equals("delete log")) {
            media.removeLog();
            relistLog();
            refreshWindow();
        }
    }

    //EFFECTS: updates the list of views
    private void relistLog() {
        remove(listLog);
        listLog = listLog();
        add(listLog, BorderLayout.CENTER);
    }

    //EFFECTS switches the Borderlayout.TOP panel from old panel to new panel
    private void switchTopPanel(JPanel oldPanel, JPanel newPanel) {
        remove(oldPanel);
        add(newPanel, BorderLayout.PAGE_START);
    }

    //EFFECTS repaints the window
    private void refreshWindow() {
        window.repaint();
        window.revalidate();
    }

    //EFFECTS: creates and returns a new panel used for creating a new viewing log
    private JPanel makeNewLog() {
        JPanel p = new JPanel();
        NumberFormat longFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
        numberFormatter.setMinimum(1); //Optional
        numberFormatter.setAllowsInvalid(false); //this is the key!!

        JButton cancelButton = new JButton("Cancle");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        p.add(cancelButton);

        amountField = new JFormattedTextField(longFormat);
        amountField.setColumns(3);
        p.add(amountField);

        JButton addButton = new JButton("Add");
        addButton.setActionCommand("add log");
        addButton.addActionListener(this);
        p.add(addButton);

        return p;
    }

}
