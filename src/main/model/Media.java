package model;

import java.util.List;

// Represents a piece of media having a name, list of times the user viewed the media,
// the length of the media, the status of the media (WAITLIST, WATCHING, FINISHED, DROPPED, ON-HOLD),
// and the user's rating and priority of the peice of media.
public class Media {
    private String name;
    private List<ViewLog> log;
    private MediaType type;
    private Integer length;
    private Integer rating;
    private Status status;
    private Integer priority;

    // REQUIRES
    // MODIFIES this
    // EFFECTS creates a new Media with a specified name, media type, status, priority and length and an empty log
    // null is considered "specified" for both priority and length for media
    public void Media(){
        //stub
    }

    // REQUIRES
    // MODIFIES this
    // EFFECTS adds a new ViewLog to the log
    // if the length of the media is not null and the total viewing progress of the log is greater than the length of the meda
    // changes the status of media to "FINISHED"
    public void logViewing(ViewLog log){
        //stub
    }

    public List<ViewLog> getLog(){
        return null;
    }

    // REQUIRES log.size() > 0, pos <= log.size()
    // MODIFIES this
    // EFFECTS removes the ViewLog in log at position "pos" 
    public void removeLog(Integer pos){
        //stub
    }

    // REQUIRES
    // MODIFIES this
    // EFFECTS returns the sum of all the viewProgress of the ViewLogs in the log
    public Integer getTotalViewProgess(){
        return -1;
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
}   
