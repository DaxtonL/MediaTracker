package model;

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
    // EFFECTS: creates a new Media with a specified name, length, priority and an empty log and status set to "WAITLIST"
    // If priority and/or length is set to -1, this represents that the given field is N/A
    public Media(String name, MediaType type, Integer length, Integer priority){
        //stub
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: adds a new ViewLog to the log, if status is not VIEWING, changes the status of the show to VIEWING unless:
    // the length of the media is not null and the total viewing progress of the log is greater than the length of the meda
    // then changes the status of media to "FINISHED"
    public void logViewing(ViewLog log){
        //stub
    }

    // REQUIRES: log.size() > 0, pos <= log.size()
    // MODIFIES: this
    // EFFECTS: removes the ViewLog in log at position "pos" 
    public void removeLog(Integer pos){
        //stub
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: returns the sum of all the viewProgress of the ViewLogs in the log
    public Integer getTotalViewProgess(){
        return -1;
    }

    public List<ViewLog> getLog(){
        return null;
    }

    public void setName(String name){
        //stub
    }

    public String getName(){
        return null;
    }

    public void setPriority(Integer priority){
        //stub
    }

    public Integer getPriority(){
        return -1;
    }

    public void setStatus(Status status){
        //sub
    }

    public Status getStatus(){
        return null;
    }

    public void setType(MediaType type){
        //stub
    }

    public MediaType getType(){
        return null;
    }

    public void setLength(){
        //stub
    }

    public Integer getLength(){
        return -1;
    }
}   
