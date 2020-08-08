package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class InvalidCommandException extends CommandException {
    private static final long serialVersionUID = 5227379656155646306L;

    public InvalidCommandException(final String message) {
        super(message);
    }
}