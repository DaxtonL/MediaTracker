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
    // MODIFIES: this 
    // EFFECTS: checks if media to add has a distinct name,
    //          returns true if adding was succesful otherwise returns false
    public Boolean addMedia(Media m){
        for (Media thisM : mediaList){
            if (thisM.getName().equals(m.getName())){
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
}
