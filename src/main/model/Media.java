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
    }

    // REQUIRES: 
    // MODIFIES: this
    // EFFECTS: removes the newest added viewLog in log
    public void removeLog() {
        if (log.size() > 0) {
            log.remove(log.get(log.size() - 1));
        }
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: returns the sum of all the viewProgress of the ViewLogs in the log
    public Integer getTotalViewProgess() {
        Integer total = 0;
        for (ViewLog v : log) {
            total += v.getViewProgress();
        }
        return total;
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: returns a list of string with the information about the media 
    //          in mediaList in a output-ready form
    // information includes: name, status, logged progress, length, rating, and priority
    public String displayMediaInfo() {
        String mediaType = toProperCase(type.toString()); 
        String mediaStatus = mediaTypeStrings().getViewingVerb();
        String mediaLength;
        if (length == -1) {
            mediaLength = "--";
        } else {
            mediaLength = length.toString();
        }
        String progress = getTotalViewProgess().toString();
        String increment = mediaTypeStrings().getLengthIncrement();
        String mediaPriority;
        if (priority == -1) {
            mediaPriority = "N/A";
        } else {
            mediaPriority = priority.toString();
        }
        String mediaRating;
        if (rating == -1) {
            mediaRating = "N/A";
        } else {
            mediaRating = rating.toString() + "/10";
        }
        String details = name + " | " + mediaType + " | " + mediaStatus + " | "
                + progress + "/" + mediaLength + " " + increment + " | Priority: " 
                + mediaPriority + " | Rating: " + mediaRating;
        return details;
    }

    public List<ViewLog> getLog() {
        return log;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public MediaType getType() {
        return type;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Integer getRating() {
        return rating;
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: outputs a list of strings based on the media type and status in a display-ready format
    public MediaTypeStrings mediaTypeStrings() {
        List<String> output = new ArrayList<>();
        if (type == null) {
            type = MediaType.NULL_MEDIA;
        }
        switch (type) {
            case MOVIE:
                output.add("Watching");
                output.add("minutes");
                break;
            
            case SHOW:
                output.add("Watching");
                output.add("episodes");
                break;
            
            case BOOK:
                output.add("Reading");
                output.add("pages");
                break;

            case MANGA:
                output.add("Reading");
                output.add("chapters");
                break;

            case GAME:
                output.add("Playing");
                output.add("hours");
                break;

            default:
                output.add("Viewing");
                output.add("unit");
                break;
        }
        if (status != Status.VIEWING) {
            if (status == Status.HOLD) {
                output.set(0, "Hold");
            } else {
                output.set(0, toProperCase(status.toString()));
            }
        }
        return new MediaTypeStrings(output.get(0), output.get(1));
    }

    // REQUIRES s.length > 2
    // MODIFIES string s
    // EFFECTS makes first character upper case and all following characters lowercase
    private String toProperCase(String s) {
        String start = "";
        String end = "";
        start = s.substring(0, 1);
        end = s.substring(1, s.length()).toLowerCase();

        
        return start + end;
    }
}   
