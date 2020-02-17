package application.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(String.format("Account with id %s not found.", message));
    }
}
