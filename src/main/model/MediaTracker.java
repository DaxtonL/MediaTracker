package model;

import model.filters.*;
import java.util.List;
import java.util.ArrayList;

// Represents a list of media
public class MediaTracker {
    private List<Media> mediaList;

    // REQUIRES: 
    // MODIFIES: this
    // EFFECTS: creates a new media tracker with an empty media list
    public MediaTracker(){
        //stub
    }

    // REQUIRES:
    // MODIFIES: list of media
    // EFFECTS: applies the filters to the mediaList and returns a new list with the filter applied
    public List<Media> getFilterMedia(List<Filter> filters){
        return null;
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: returns a list of string with the information about the media in mediaList in a output-ready form
    // information includes: name, status, logged progress, length, rating, and priority
    public List<String> displayMediaList(){
        return null;
    }

    // REQUIRES:
    // MODIFIES: this 
    // EFFECTS: checks if media to add has a distinct name,
    //          returns true if adding was succesful otherwise returns false
    public Boolean addMedia(Media m){
        return false;
    }

    // REQUIRES: The name of all media in mediaList are unique
    // MODIFIES: this
    // EFFECTS: If there is a media in mediaList with inputted name, removes it from mediaList
    //          returns true if adding was succesful otherwise returns false
    public Boolean removeMedia(String name){
        return false;
    }
}
