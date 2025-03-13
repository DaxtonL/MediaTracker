package ui;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import model.*;
import model.enums.*;
import model.filters.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import exceptions.FailureToCompleteOperationException;
import exceptions.InvalidInputException;

// Media tracker application
public class MediaTrackerApp {
    private Boolean running;
    private MediaTracker tracker;
    Scanner scanner;
    String input;

    // EFFECTS: starts the media tracker application
    public MediaTrackerApp() {
        this.running = true;
        scanner = new Scanner(System.in);
        if (yesNoInput("Do you want to load a saved media tracker?")) {
            askLoadTracker();
        }
        
        if (tracker == null) {
            System.out.println("Input the name of your media tracker");
            tracker = new MediaTracker(scanner.nextLine());
        }

        run();
    }

    // EFFECTS: main loop of the program
    private void run() {
        System.out.println("Welcome to your media tracker!");

        while (running) {
            System.out.println();
            showCommands();
            input = scanner.nextLine();
            parseCommands(input);
        }
    }

    // EFFECTS: prints out the commands available to the user
    private void showCommands() {
        System.out.println("Commands:");
        System.out.println("A: Add new media");
        System.out.println("L: Get a filtered list of media");
        System.out.println("E: Edit a piece of media");
        System.out.println("R: Remove a peice of media");
        System.out.println("S: Save your media tracker");
        System.out.println("O: Open a saved media tracker");
        System.out.println("Q: Quit program");
    }

    // EFFECTS loads the media tracker with the inputted name
    //         returns null if media tracker cannot be found
    private MediaTracker loadMediaTracker(String name) {
        JsonReader reader = new JsonReader("./data/" + name + ".json");
        try {
            return reader.read();
        } catch (IOException e) {
            return null;
        }
    }

    private void askLoadTracker() {
        System.out.println("Input the name of the media tracker you want to load");
        tracker = loadMediaTracker(scanner.nextLine());
        if (tracker == null) {
            System.out.println("Could not find media tracker with that name");
            if (yesNoInput("Do you want to try again?")) {
                askLoadTracker();;
            }
        }
    }

    // EFFECTS saves the current media tracker, returns true if succesfull
    private boolean saveMediaTracker() {
        JsonWriter writer = new JsonWriter("./data/" + tracker.getName() + ".json");
        try {
            writer.open();
            writer.write(tracker);
            writer.close();
            System.out.println("Succesfully saved media tracker!");
            return true;
        } catch (IOException e) {
            System.out.println("Could not save media tracker!");
            return false;
        }
    }

    // EFFECTS checks if user wants to save before quitting, then quits the application
    private void quitApplication() {
        if (yesNoInput("Do you want to save this media tracker before you quit?")) {
            if (saveMediaTracker() == false) {
                if (!yesNoInput("Do you still want to quit?")) {
                    return;
                }
            }
        }
        System.out.println("Application ended");
        running = false;
    }


    // MODIFIES: this
    // EFFECTS: executes the user command based on inputted string
    @SuppressWarnings("methodlength")
    private void parseCommands(String s) {
        s = s.toUpperCase();
        switch (s) {
            case "A":
                addNewMedia();
                break;
            case "L":
                listMedia();
                break;
            case "E":
                editMedia();
                break;
            case "R":
                removeMedia();
                break;
            case "S":
                saveMediaTracker();
                break;
            case "O":
                askLoadTracker();
                break;
            case "Q":
                quitApplication();
                break;
            default:
                System.out.println("Command not recognized.");
                showCommands();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new media and adds it to the tracker mediaList as
    // as long as there is no media already with the same name
    private void addNewMedia() {
        Media m = new Media(null, null, null, null);
        try {
            changeMediaValue(m, "NAME");
            changeMediaValue(m, "TYPE");
            changeMediaValue(m, "LENGTH");
            changeMediaValue(m, "PRIORITY");
            System.out.println(displayMedia(m));
            System.out.println("Is this peice of media correct? (y/n)");
            if (scanner.nextLine().equals("y")) {
                if (tracker.addMedia(m)) {
                    System.out.println("Piece of media succefully added!");
                    return;
                } else {
                    System.out.println("A piece of media with that name already exists");
                    throw new InvalidInputException("Media type already exists");
                }
            }
        } catch (Exception e) {
            System.out.println("Could not finish making the new media");
            if (yesNoInput("Do you want to try again?")) {
                addNewMedia();
            }
        }
    }

    // EFFECTS: prints out a list of media based on filters specified by user
    private void listMedia() {
        List<Filter> filters = new ArrayList<Filter>();
        Boolean ask = true;
        while (ask) {
            System.out.println("What filters do you want to apply (status, type, rating) (leave blank for none)");
            Filter f = stringToFilter(scanner.nextLine());
            if (f == null) {
                break;
            }
            filters.add(f);
            System.out.println("Do you want to add another filter?");
            if (scanner.nextLine().equals("y")) {
                ask = true;
            } else {
                ask = false;
            }
        }
        List<Media> filteredList = tracker.getFilterMedia(filters);
        System.out.println("Media:");
        System.out.println("---");
        for (Media m : filteredList) {
            System.out.println(displayMedia(m));
        }
        System.out.println("---");
    }

    private String displayMedia(Media m) {
        List<String> details = m.listMediaInfo();
        String s =  details.get(0) + " | " + details.get(1) + " | " + details.get(2) + " | "
                    + details.get(3) + "/" + details.get(4) + " " + details.get(5) + " | Priority: " 
                    + details.get(6) + " | Rating: " + details.get(7);
        return s;
    }

    // EFFECTS: prints out possible edits user can make to data
    private void showEdits() {
        System.out.println("Edits:");
        System.out.println("L+: log a viewing");
        System.out.println("L-: remove last logged viewing");
        System.out.println("S: change status of media");
        System.out.println("P: Change priority of media");
        System.out.println("R: Change rating of media");
        System.out.println("E: Edit the name, type and/or length");
    }

    // MODIFIES: chosen media
    // EFFCTS: allows user to apply edits to chose media
    @SuppressWarnings("methodlength")
    private void editMedia() {
        try {
            System.out.println("Input the name of the piece of media you want to edit");
            Media m = tracker.getMedia(scanner.nextLine());
            if (m == null) {
                throw new InvalidInputException("Could not find media with that name");
            }
            System.out.println(displayMedia(m));
            System.out.println("What edit do you want to make?");
            showEdits();
            input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "L+":
                    System.out.println("How many " + m.getType().getIncrement() + " do you want to log?");
                    input = scanner.nextLine();
                    Integer n = strToPositiveIntRange(input, 1, -1);
                    if (n == -1) {
                        return;
                    }
                    m.logViewing(new ViewLog(LocalDate.now(), n));
                    break;
                case "L-":
                    m.removeLog();
                    break;
                case "S":
                    changeMediaValue(m, "STATUS");
                    break;
                case "P":
                    changeMediaValue(m, "PRIORITY");
                    break;
                case "R":
                    changeMediaValue(m, "Rating");
                    break;
                case "E":
                    changeMediaValue(m, "NAME");
                    changeMediaValue(m, "TYPE");
                    changeMediaValue(m, "Length");
                    break;
                default:
                    throw new InvalidInputException("That is not a recognized command.");
            }
            System.out.println(displayMedia(m));
            System.out.println("Edit to " + m.getName() + " succesful!");
        } catch (FailureToCompleteOperationException | InvalidInputException e) {
            System.out.println(e.getMessage());
            if (yesNoInput("Do you want to try again?")) {
                editMedia();
            }
        }
        return;
    }

    // MODIFIES this
    // EFFECTS remove chosen media from list if it is in mediaList
    private void removeMedia() {
        System.out.println("Input the name of the peice of media you want to remove");
        String input = scanner.nextLine();
        if (tracker.removeMedia(input)) {
            System.out.println("Piece of media succesfully removed!");
            return;
        } else {
            System.out.println("Could not find media with that name");
            if (yesNoInput("Do you want to try again?")) {
                removeMedia();
            }
        }
    }

    // MODIFIES chosen media
    // EFFECTS applies the selected change to a given media's fields
    // returns true if change was succesful
    @SuppressWarnings("methodlength")
    private void changeMediaValue(Media m, String s) throws FailureToCompleteOperationException {
        s = s.toUpperCase();
        try {
            switch (s) {
                case "NAME":
                    System.out.println("Input media name");
                    m.setName(scanner.nextLine());
                    return;
                case "TYPE":
                    System.out.println("Input media type (movie, show, book, game, manga)");
                    m.setType(stringToMediaType(scanner.nextLine()));
                    return;
                case "LENGTH":
                    System.out.println("Input media length (leave blank for N/A)");
                    m.setLength(strToPositiveIntRange(scanner.nextLine(), 1, -1));
                    return;
                case "STATUS":
                    System.out.println("Input media status (waitlist, viewing, finished, hold, dropped)");
                    m.setStatus(stringToStatus(scanner.nextLine()));
                    return;
                case "PRIORITY":
                    System.out.println("Input media watchlist priority (leave blank for N/A)");
                    m.setPriority(strToPositiveIntRange(scanner.nextLine(), 1, -1));
                    return;
                case "RATING":
                    System.out.println("Input media rating (leave blank for N/A)");
                    Integer val = strToPositiveIntRange(scanner.nextLine(), 0, 10);
                    if (val == -1) {
                        throw new InvalidInputException("Input was outside of bounds.");
                    }
                    m.setRating(val);
                    return;
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            if (yesNoInput("Do you want to try again?")) {
                changeMediaValue(m, s);
            }
        }
        throw new FailureToCompleteOperationException("Could not complete the change.");
    }

    // EFFECTS prompts a user if they want to try again and returns true if they
    // input "y"
    private Boolean yesNoInput(String question) {
        System.out.println(question + " (y/n)");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES string
    // EFFECTS changes a string to a integer accepted by other methods
    // if cannot be changed to a integer return -1 (-1 represents a null integer in
    // the code)
    private Integer strToPositiveIntRange(String s, int min, int max) {
        Integer n;
        try {
            if (s.length() > 0) {
                n = Integer.parseInt(s);
                if (max < min && n >= min) {
                    return n;
                } else if (n <= max && n >= min) {
                    return n;
                }
            }
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // MODIFIES string
    // EFFECTS changes a string to a media type if a valid one exists
    // else throws IllegalArgumentException
    private MediaType stringToMediaType(String s) throws InvalidInputException {
        try {
            return MediaType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Could not find status with that name");
        }
    }

    // MODIFIES string
    // EFFECTS changes a string to a status if there is a valid one
    // else throws IllegalArgumentException
    private Status stringToStatus(String s) throws InvalidInputException {
        try {
            return Status.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Could not find status with that name");
        }
    }

    // MODIFIES string
    // EFFECTS changes a string to a filter
    private Filter stringToFilter(String s) {
        s = s.toUpperCase();
        try {
            switch (s) {
                case "STATUS":
                    System.out
                            .println("What status do you want to filter for?"
                                    + "(waitlist, viewing, finished, hold, dropped)");
                    return new FilterStatus(stringToStatus(scanner.nextLine()));
                case "TYPE":
                    System.out.println("What media type do you want to filter for? (movie, book, game, show, manga)");
                    return new FilterType(stringToMediaType(scanner.nextLine()));
                case "RATING":
                    return inputRatingFilter();
                default:
                    return null;
            }
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            if (yesNoInput("Do you want to try again?")) {
                stringToFilter(s);
            }
        }
        return null;
    }

    // EFFECTS creates a new rating filter based on user input and returns it
    private FilterRating inputRatingFilter() throws InvalidInputException {
        Boolean above;
        System.out.println("What rating value do you want to filter for? [0-10]");
        Integer n = strToPositiveIntRange(scanner.nextLine(), 0, 10);
        if (n == -1) {
            throw new InvalidInputException("Input outside of bounds.");
        }
        System.out.println("Do you want to filter for media above a certain rating? (y/n)");
        input = scanner.nextLine();
        if (input.equals("y")) {
            above = true;
        } else if (input.equals("n")) {
            above = false;
        } else {
            throw new InvalidInputException("That is not one of the options.");
        }
        return new FilterRating(above, n);
    }
}
