package model.exceptions;

public class InvalidInputException extends Exception {
    private String error; 

    public InvalidInputException(String message) {
        super(message);
        this.error = message;
    }

    public String getError() {
        return error;
    }
}
