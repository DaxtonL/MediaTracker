package model.enums;

// Represents different types of media
// Contains the media name, verb used for when user is "viewing" the media and units of increment for the media
public enum MediaType {
    MOVIE("watching","minutes"),
    GAME("playing","hours"),
    SHOW("watching","episodes"),
    MANGA("reading","chapters"),
    BOOK("reading","pages");

    private final String viewingVerb;
    private final String increment;

    MediaType(String viewingVerb, String increment) {
        this.viewingVerb = viewingVerb;
        this.increment = increment;
    }

    public String getViewingVerb() {
        return viewingVerb;
    }

    public String getIncrement() {
        return increment;
    }

}
