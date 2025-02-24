package model.enums;

// Represents different types of media
// Contains the media name, verb used for when user is "viewing" the media and units of increment for the media
public class MediaType {
    private String name;
    private String viewingVerb;
    private String increment;

    public MediaType(String name, String veiwingVerb, String increment) {
        this.name = name;
        this.viewingVerb = veiwingVerb;
        this.increment = increment;
    }
    
    public String getName() {
        return name;
    }

    public String getViewingVerb() {
        return viewingVerb;
    }

    public String getIncrement() {
        return increment;
    }
}
