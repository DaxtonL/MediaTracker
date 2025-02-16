package ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import model.*;
import model.enums.*;
import model.enums.MediaType;

public class MediaTrackerApp {
    private Boolean running;
    private MediaTracker tracker;
    Scanner scanner;

    public MediaTrackerApp() {
        this.running = true;
        
        tracker = new MediaTracker();
        scanner = new Scanner(System.in);
        
        Run();
    }

    private void Run() {
        String input;

        System.out.println("Welcome to your media tracker!");
        showCommands();

        while (running){
            //System.out.println();
            input = scanner.nextLine();
            parseCommands(input);
        }
    }

    private void showCommands(){
        System.out.println("Commands:");
        System.out.println("A: Add new media");
        System.out.println("L: Get a filtered list of media");
        System.out.println("E: Edit a piece of media");
        System.out.println("R: Remove a peice of media");
        System.out.println("Q: Quit program");
    }

    private void parseCommands(String s){
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
            case "Q":
                System.out.println("Application ended");
                running = false;
                break;
            default:
                System.out.println("Command not recognized.");
                showCommands();
                break;
        }
    }

    private void addNewMedia(){
        String input;
        Media m = new Media(null, null, null, null);
        changeMediaValue(m, "NAME");
        changeMediaValue(m, "TYPE");
        changeMediaValue(m, "LENGTH");
        changeMediaValue(m, "PRIORITY");

        System.out.println(m.displayMediaInfo());
        System.out.println("Is this peice of media correct? (y/n)");

        input = scanner.nextLine();
        if (input.equals("y")){
            if (tracker.addMedia(m)){
                System.out.println("Piece of media succefully added!");
                return;
            }
            else {
                System.out.println("A piece of media with that name already exists");
            }
        }
        if (tryAgainInput()){
            addNewMedia();
        }   
    }

    private MediaType stringToMediaType(String s){
        try {
            return MediaType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Status stringToStatus(String s){
        try {
            return Status.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void listMedia(){
        for (Media m : tracker.getFilterMedia(null)){
            System.out.println(m.displayMediaInfo());
        }
    }

    private void removeMedia(){
        System.out.println("Input the name of the peice of media you want to remove");
        String input = scanner.nextLine();
        if (tracker.removeMedia(input)){
            System.out.println("Piece of media succesfully removed!");
            return;
        }
        else {
            System.out.println("Could not find media with that name");
            if (tryAgainInput()){
                removeMedia();
            }
        }
    }

    private void editMedia(){
        System.out.println("Input the name of the peice of media you want to edit");
        String input = scanner.nextLine();
        Media m = tracker.getMedia(input);
        if (m == null){
            System.out.println("Could not find media with that name");
            System.out.println("Do you want to try again? (y/n)");
            input = scanner.nextLine();
            if (input.equals("y")){
                removeMedia();
            }
            return;
        }
        System.out.println(m.displayMediaInfo());
        System.out.println("What edit do you want to make?");
        showEdits();
        input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "L+":
                
                break;
            case "L-":
                
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
                return;
        }
        System.out.println("Edit to " + m.getName() + " succesful!");
    }

    private void showEdits(){
        System.out.println("Edits:");
        System.out.println("L+: log a viewing");
        System.out.println("L-: remove last logged viewing");
        System.out.println("S: change status of media");
        System.out.println("P: Change priority of media");
        System.out.println("R: Change rating of media");
        System.out.println("E: Edit the name, type and/or length");
    }

    private void changeMediaValue(Media m, String s){
        s = s.toUpperCase();
        String input;
        Boolean tryAgain = true;
        while (tryAgain){
            switch (s) {
                case "NAME":
                    System.out.println("Input media name");
                    input = scanner.nextLine();
                    m.setName(input);
                    tryAgain = false;
                    break;
                case "TYPE":
                    System.out.println("Input media type (movie, show, book, game, manga)");
                    MediaType type = stringToMediaType(scanner.nextLine());
                    if (type == null){
                        System.out.println("Invalid media type");
                        tryAgain = tryAgainInput();
                        break;
                    }
                    m.setType(type);
                    tryAgain = false;
                    break;
                case "LENGTH":
                    System.out.println("Input media length (leave blank for N/A)");
                    input = scanner.nextLine();
                    Integer length;
                    try {
                        if (input.length() > 0){
                            length = Integer.parseInt(input);
                        } else {
                            length = -1;
                        } 
                    } catch (NumberFormatException e) {
                        length = -1;
                    }
                    m.setLength(length);
                    tryAgain = false;
                    break;
                case "STATUS":
                    System.out.println("Input media type (movie, show, book, game, manga)");
                    Status status = stringToStatus(scanner.nextLine());
                    if (status == null){
                        System.out.println("Invalid status");
                        tryAgain = tryAgainInput();
                        break;
                    }
                    m.setStatus(status);
                    tryAgain = false;
                    break;
                case "PRIORITY":
                    System.out.println("Input media watchlist priority (leave blank for N/A)");
                    input = scanner.nextLine();
                    Integer priority;
                    try {
                        if (input.length() > 0){
                            priority = Integer.parseInt(input);
                        } else {
                            priority = -1;
                        } 
                    } catch (NumberFormatException e) {
                        priority = -1;
                    }
                    m.setPriority(priority);
                    tryAgain = false;
                    break;
                case "RATING":
                    System.out.println("Input media rating (leave blank for N/A)");
                    input = scanner.nextLine();
                    Integer rating;
                    try {
                        if (input.length() > 0){
                            rating = Integer.parseInt(input);
                        } else {
                            rating = -1;
                        } 
                    } catch (NumberFormatException e) {
                        rating = -1;
                    }
                    m.setLength(rating);
                    tryAgain = false;
                    break;
                default:
                    tryAgain = false;
                    break;
            }
        }
    }

    private Boolean tryAgainInput() {
        System.out.println("Do you want to try again? (y/n)");
        String input = scanner.nextLine();
        if (input.equals("y")){
            return true;
        } else {
            return false;
        }
    }
}
