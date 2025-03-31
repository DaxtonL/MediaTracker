package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.enums.MediaType;
import model.enums.Status;
import logging.*;

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
    // EFFECTS: creates a new Media with a specified name, length, priority, 
    // an empty log, rating set to -1 and status set to "WAITLIST"
    // If priority, rating and/or length is set to -1, this represents that the given field is N/A
    public Media(String name, MediaType type, Integer length, Integer priority) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.priority = priority;
        this.status = Status.WAITLIST;
        this.rating = -1;
        this.log = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Created new media"));
    }

    // REQUIRES: to be called only when loading previously created media
    // MODIFIES: this
    // EFFECTS: Constructor used for loading media, creates a Media with previously specified name, length, priority, 
    //          rating, status, and log
    public Media(String name, MediaType type, Integer length, Status status, 
                    Integer priority, Integer rating, List<ViewLog> log) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.status = status;
        this.priority = priority;
        this.rating = rating;
        this.log = log;
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: adds a new ViewLog to the log, if status is not VIEWING, 
    // changes the status of the show to VIEWING unless:
    // the length of the media is not null and 
    // the total viewing progress of the log is greater than the length of the meda
    // then changes the status of media to "FINISHED"
    public void logViewing(ViewLog viewing) {
        log.add(viewing);
        if (length != -1 && getTotalViewProgess() >= length) {
            status = Status.FINISHED;
        } else if (status != Status.VIEWING) {
            status = Status.VIEWING;
        }
        EventLog.getInstance().logEvent(new Event("Logged a viewing for a piece of media"));
    }

    // REQUIRES: 
    // MODIFIES: this
    // EFFECTS: removes the newest added viewLog in log
    public void removeLog() {
        if (log.size() > 0) {
            log.remove(log.get(log.size() - 1));
            EventLog.getInstance().logEvent(new Event("Removed a viewing from a piece of media"));
        }
    }

    // EFFECTS: returns the sum of all the viewProgress of the ViewLogs in the log
    public Integer getTotalViewProgess() {
        Integer total = 0;
        for (ViewLog v : log) {
            total += v.getViewProgress();
        }
        return total;
    }

    // EFFECTS: returns a list of string with the information about the media 
    //          in mediaList in a output-ready form
    // information includes: name, type, status, progress, length, increment, priority, rating
    public List<String> listMediaInfo() {
        List<String> details = new ArrayList<String>();
        String mediaType = toProperCase(type.toString());
        String mediaStatus = mediaTypeStrings();
        String progress = getTotalViewProgess().toString();
        String mediaLength = mediaNumToString(length, "--");
        String increment = type.getIncrement().toLowerCase();
        String mediaPriority = mediaNumToString(priority, "--");
        String mediaRating = mediaNumToString(rating, "--") + "/10";
        // String details = name + " | " + mediaType + " | " + mediaStatus + " | "
        //         + progress + "/" + mediaLength + " " + increment + " | Priority: " 
        //         + mediaPriority + " | Rating: " + mediaRating;
        details.add(name);
        details.add(mediaType);
        details.add(mediaStatus);
        details.add(progress);
        details.add(mediaLength);
        details.add(increment);
        details.add(mediaPriority);
        details.add(mediaRating);

        return details;
    }

    // EFFECTS: outputs a string based on the media type and status in a display-ready format
    private String mediaTypeStrings() {
        if (status == Status.VIEWING) {
            return toProperCase(type.getViewingVerb());
        } else if (status == Status.HOLD) {
            return "On-hold";
        } else {
            return toProperCase(status.toString());
        }
    }

    // REQUIRES: n >= -1
    // EFFECTS returns a string in ready to display form
    //         if n == -1, return the "nullString" string, else returns n as a string
    private String mediaNumToString(Integer n, String nullString) {
        if (n == -1) {
            return nullString;
        } else {
            return n.toString();
        }
    }

    // REQUIRES s.length > 2
    // MODIFIES string s
    // EFFECTS makes first character upper case and all following characters lowercase
    private String toProperCase(String s) {
        String start = "";
        String end = "";
        start = s.substring(0, 1).toUpperCase();
        end = s.substring(1, s.length()).toLowerCase();

        return start + end;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("type", type);
        json.put("length", length.toString());
        json.put("status", status);
        json.put("priority", priority.toString());
        json.put("rating", rating.toString());
        JSONArray jsonArray = new JSONArray();
        for (ViewLog l : log) {
            JSONObject localJson = new JSONObject();
            localJson.put("year", Integer.toString(l.getDate().getYear()));
            localJson.put("month", l.getDate().getMonth());
            localJson.put("day", Integer.toString(l.getDate().getDayOfMonth()));
            localJson.put("viewProgress", Integer.toString(l.getViewProgress())); 
            jsonArray.put(localJson);  
        }
        json.put("log", jsonArray);
        return json;
    }

    public List<ViewLog> getLog() {
        return log;
    }

    public void setName(String name) {
        this.name = name;
        EventLog.getInstance().logEvent(new Event("Changed name for a piece of media"));
    }

    public String getName() {
        return name;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
        EventLog.getInstance().logEvent(new Event("Changed priority for a piece of media"));
    }

    public Integer getPriority() {
        return priority;
    }

    public void setStatus(Status status) {
        this.status = status;
        EventLog.getInstance().logEvent(new Event("Changed status for a piece of media"));
    }

    public Status getStatus() {
        return this.status;
    }

    public void setType(MediaType type) {
        this.type = type;
        EventLog.getInstance().logEvent(new Event("Changed type for a piece of media"));
    }

    public MediaType getType() {
        return type;
    }

    public void setLength(Integer length) {
        this.length = length;
        EventLog.getInstance().logEvent(new Event("Changed length for a piece of media"));
    }

    public Integer getLength() {
        return length;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
        EventLog.getInstance().logEvent(new Event("Changed rating for a piece of media"));
    }
    
    public Integer getRating() {
        return rating;
    }
}   
