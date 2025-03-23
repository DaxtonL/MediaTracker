package ui.panels;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.InvalidInputException;

import model.ViewLog;
import model.enums.MediaType;


public class LogPanel extends AppPanelUI {

    public LogPanel(ActionListener handler, JFrame window, ViewLog v, MediaType type) {
        super(handler, window);
        JLabel date = new JLabel(dateToString(v.getDate()));
        JLabel amount = new JLabel(Integer.toString(v.getViewProgress()) + type.getViewingVerb());
        add(date);
        add(amount);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("delete log");
        deleteButton.addActionListener(handler);
    }

    @Override
    public List<String> closePanel() throws InvalidInputException {
        throw new InvalidInputException("No valid data");
    }

    private String dateToString(LocalDate d) {
        String year = Integer.toString(d.getYear());
        String month = Integer.toString(d.getMonthValue());
        String day = Integer.toString(d.getDayOfMonth());
        return year + "," + month + "," + day;
    }

}
