package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class InvalidFlagValueException extends CommandException {
    private static final long serialVersionUID = -5818202343296900781L;

    public InvalidFlagValueException(final String message) {
        super(message);
    }
}
