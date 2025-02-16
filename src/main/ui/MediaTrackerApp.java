package ui;

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
        System.out.println("D: Delete a peice of media");
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
                System.out.println("You want to edit a peice of media:");
                break;
            case "D":
                System.out.println("You want to delete a piece of media:");
                break;
            case "Q":
                System.out.println("Stopping program");
                running = false;
                break;
            default:
                System.out.println("Command not recognized.");
                showCommands();
                break;
        }
    }

    private void addNewMedia() {
        Media m;
        String name;
        MediaType type;
        Integer length;
        Integer priority;

        System.out.println("What type of media to you want to create? (movie, show, book, game, manga)");
        String input = scanner.nextLine();

        type = stringToMediaType(input);
        if (type == null){
            System.out.println("That is not a valid media type.");
            System.out.println("Do you want to try again? (y/n)");
            input = scanner.nextLine();
            if (input.equals("y")){
                addNewMedia();
            } else {
                return;
            }
        }

        System.out.println("What is the name of the work?");
        input = scanner.nextLine();
        name = input;

        System.out.println("What is the length of the work? (leave blank if unkown)");
        input = scanner.nextLine();
        try {
            length = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            length = -1;
        }
        
        System.out.println("What is your viewing priority of this piece of media? (leave blank if unkown)");
        input = scanner.nextLine();
        try {
            priority = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            priority = -1;
        }

        m = new Media(name, type, length, priority);
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
        System.out.println("Do you want to try again? (y/n)");
        input = scanner.nextLine();
        if (input.equals("y")){
            addNewMedia();
        }
        else {
            return;
        }
    }

    private MediaType stringToMediaType(String s){
        try {
            return MediaType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void listMedia(){
        for (Media m : tracker.getFilterMedia(null)){
            System.out.println(m.displayMediaInfo());
        }
    }

}
