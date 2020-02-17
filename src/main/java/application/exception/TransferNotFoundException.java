package application.exception;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException(String message) {
        super(String.format("Transfer with id %s not found.", message));
    }
}
