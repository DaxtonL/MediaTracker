package model;

import java.time.LocalDate;

// Represents time where the users viewed a piece of media
// contains the date and how much of the media the consumed.
public class ViewLog {
    private LocalDate date;
    private Integer viewProgress;

    public ViewLog(LocalDate date, Integer viewProgress) {
        this.date = date;
        this.viewProgress = viewProgress;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getViewProgress() {
        return viewProgress;
    }

}
