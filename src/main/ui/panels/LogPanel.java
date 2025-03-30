package ui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.InvalidInputException;

import model.ViewLog;
import model.enums.MediaType;


public class LogPanel extends AppPanelUI {

    public LogPanel(ActionListener handler, JFrame window, ViewLog v, MediaType type) {
        super(handler, window);
        setPreferences();
        JLabel date = new JLabel("Date: " + dateToString(v.getDate()));
        JLabel amount = new JLabel("Amount: " + Integer.toString(v.getViewProgress()) + type.getIncrement());
        add(date);
        add(amount);
        // JButton deleteButton = new JButton("Delete");
        // deleteButton.setActionCommand("delete log");
        // deleteButton.addActionListener(handler);
        //add(deleteButton);
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No valid data");
    }

    private String dateToString(LocalDate d) {
        String year = Integer.toString(d.getYear());
        String month = Integer.toString(d.getMonthValue());
        String day = Integer.toString(d.getDayOfMonth());
        return year + ", " + month + ", " + day;
    }

    private void setPreferences() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setPreferredSize(new Dimension(900, 40)); // Reduce height to remove excess space
        setMaximumSize(new Dimension(900, 40)); // Prevent stretching
    }
}
