package exceptions;

// Exception that is thrown when the App was not able to complete an operation
public class FailureToCompleteOperationException extends Exception {
    public FailureToCompleteOperationException(String message) {
        super(message);
    }
}
