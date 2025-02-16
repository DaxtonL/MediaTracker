package model;

import model.filters.*;
import model.enums.*;
import java.util.List;
import java.util.ArrayList;

// Represents a list of media
public class MediaTracker {
    private List<Media> mediaList;

    // REQUIRES: 
    // MODIFIES: this
    // EFFECTS: creates a new media tracker with an empty media list
    public MediaTracker(){
        mediaList = new ArrayList<>();
    }

    // REQUIRES:
    // MODIFIES: list of media
    // EFFECTS: applies the filters to the mediaList and returns a new list with the filter applied
    public List<Media> getFilterMedia(List<Filter> filters){
        if (filters == null || filters.isEmpty() ) {
            return mediaList;
        }

        List<Media> filteredList = new ArrayList<>();
        for (Media m : mediaList){
            Boolean passFilter = true;
            for (Filter f : filters){
                if (!f.applyFilter(m)){
                    passFilter = false;
                }
            }
            if (passFilter){
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: returns a list of string with the information about the media in mediaList in a output-ready form
    // information includes: name, status, logged progress, length, rating, and priority
    public List<String> displayMediaList(){
        List<String> output = new ArrayList<>();
        for (Media m : mediaList){
            String mName = m.getName();
            String mType = toProperCase(m.getType().toString()); 
            String mStatus = mediaTypeStrings(m).get(0);
            String mLength;
            if (m.getLength() == -1){
                mLength = "--";
            } else {
                mLength = m.getLength().toString();
            }
            String mProgress = m.getTotalViewProgess().toString();
            String mIncrement = mediaTypeStrings(m).get(1);
            String mPriorty;
            if (m.getPriority() == -1){
                mPriorty = "N/A";
            } else {
                mPriorty = m.getPriority().toString();
            }
            String mRating;
            if (m.getRating() == -1){
                mRating = "N/A";
            }
            else{
                mRating = m.getRating().toString()+"/10";
            }
            String details = mName + " | " + mType + " | " + mStatus + " | "  +
             mProgress + "/" + mLength + " " + mIncrement + " | Priority: " + mPriorty + 
             " | Rating: " + mRating;
            output.add(details);
        }
        return output;
    }

    // REQUIRES:
    // MODIFIES: this 
    // EFFECTS: checks if media to add has a distinct name,
    //          returns true if adding was succesful otherwise returns false
    public Boolean addMedia(Media m){
        for (Media thisM : mediaList){
            if (thisM.getName() == m.getName()){
                return false;
            }
        }
        mediaList.add(m);
        return true;
    }

    // REQUIRES: The name of all media in mediaList are unique
    // MODIFIES: this
    // EFFECTS: If there is a media in mediaList with inputted name, removes it from mediaList
    //          returns true if adding was succesful otherwise returns false
    public Boolean removeMedia(String name){
        for (Media thisM : mediaList){
            if (thisM.getName() == name){
                mediaList.remove(thisM);
                return true;
            }
        }
        return false;
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: outputs a list of strings based on the media type and status in a display-ready format
    private List<String> mediaTypeStrings(Media m){
        List<String> output = new ArrayList<>();
        MediaType type = m.getType();
        Status s = m.getStatus();
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
                output.add("units");
                break;
        }
        if (s != Status.VIEWING) {
            if (s == Status.ON_HOLD) {
                output.set(0, "On-hold");
            }
            else {
                output.set(0, toProperCase(s.toString()));
            }
        }
        return output;
    }

    // REQUIRES s.length > 0
    // MODIFIES string s
    // EFFECTS makes first character upper case and all following characters lowercase
    private String toProperCase(String s){
        String start = "";
        String end = "";
        if (s.length() > 0){
            start = s.substring(0, 1);
        }
        if (s.length() > 1){
            end = s.substring(1, s.length()).toLowerCase();
        }
        
        return start + end;
     }
}
