package model;

import java.util.ArrayList;
import java.util.List;

import model.enums.MediaType;
import model.enums.Status;

// Represents a piece of media having a name, list of times the user viewed the media,
// the length of the media, the status of the media (WAITLIST, VIEWING, FINISHED, DROPPED, ON-HOLD),
// and the user's rating and priority of the peice of media.
public class Media {
    private String name;
    private MediaType type;
    private Integer length;
    private Status status;
    private Integer priority;
    private Integer rating;
    private List<ViewLog> log;


    // MODIFIES: this
    // EFFECTS: creates a new Media with a specified name, length, priority, an empty log, rating set to -1 and status set to "WAITLIST"
    // If priority, rating and/or length is set to -1, this represents that the given field is N/A
    public Media(String name, MediaType type, Integer length, Integer priority){
        this.name = name;
        this.type = type;
        this.length = length;
        this.priority = priority;
        this.status = Status.WAITLIST;
        this.rating = -1;
        this.log = new ArrayList<>();
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: adds a new ViewLog to the log, if status is not VIEWING, changes the status of the show to VIEWING unless:
    // the length of the media is not null and the total viewing progress of the log is greater than the length of the meda
    // then changes the status of media to "FINISHED"
    public void logViewing(ViewLog viewing){
        log.add(viewing);
        if (length != -1 && getTotalViewProgess() >= length){
            status = Status.FINISHED;
        }
        else if (status != Status.VIEWING) {
            status = Status.VIEWING;
        }
    }

    // REQUIRES: log.size() > 0, pos <= log.size()
    // MODIFIES: this
    // EFFECTS: removes the ViewLog in log at position "pos" 
    public void removeLog(Integer pos){
        log.remove(log.get(pos));
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: returns the sum of all the viewProgress of the ViewLogs in the log
    public Integer getTotalViewProgess(){
        Integer total = 0;
        for (ViewLog v : log){
            total += v.getViewProgress();
        }
        return total;
    }

    public List<ViewLog> getLog(){
        return log;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setPriority(Integer priority){
        this.priority = priority;
    }

    public Integer getPriority(){
        return priority;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setType(MediaType type){
        this.type = type;
    }

    public MediaType getType(){
        return type;
    }

    public void setLength(Integer length){
        this.length = length;
    }

    public Integer getLength(){
        return length;
    }

    public void setRating(Integer rating){
        this.rating = rating;
    }
    
    public Integer getRating(){
        return rating;
    }
}   
