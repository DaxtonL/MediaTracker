package model;

// Class to represent strings used for various media types
public class MediaTypeStrings {
    private String viewingVerb;
    private String lengthIncrement;

    public MediaTypeStrings(String verb, String increment) {
        this.viewingVerb = verb;
        this.lengthIncrement = increment;
    }

    public String getViewingVerb() {
        return viewingVerb;
    }

    public String getLengthIncrement() {
        return lengthIncrement;
    }
}
