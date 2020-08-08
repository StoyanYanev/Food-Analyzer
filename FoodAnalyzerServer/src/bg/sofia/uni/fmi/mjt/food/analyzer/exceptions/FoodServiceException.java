package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class FoodServiceException extends Exception {
    private static final long serialVersionUID = 132505173935044596L;

    public FoodServiceException(final String message) {
        super(message);
    }
}