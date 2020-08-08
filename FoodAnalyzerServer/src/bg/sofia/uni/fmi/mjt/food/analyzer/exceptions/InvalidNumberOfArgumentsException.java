package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class InvalidNumberOfArgumentsException extends CommandException {
    private static final long serialVersionUID = -6412913124325481871L;

    public InvalidNumberOfArgumentsException(final String message) {
        super(message);
    }
}