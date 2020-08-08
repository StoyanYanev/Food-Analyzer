package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class EmptyFoodNameException extends FoodException {
    private static final long serialVersionUID = 4979751460242333526L;

    public EmptyFoodNameException(final String message) {
        super(message);
    }
}