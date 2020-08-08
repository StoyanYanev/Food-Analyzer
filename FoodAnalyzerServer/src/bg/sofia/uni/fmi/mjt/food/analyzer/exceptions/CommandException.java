package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class CommandException extends Exception {
    private static final long serialVersionUID = -4265727780465841138L;

    public CommandException(final String message) {
        super(message);
    }
}
