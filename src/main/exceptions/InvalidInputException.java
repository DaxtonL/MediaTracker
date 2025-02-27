package exceptions;

// Exception that is thrown when the user's input is not within the scope of expected inputs
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
