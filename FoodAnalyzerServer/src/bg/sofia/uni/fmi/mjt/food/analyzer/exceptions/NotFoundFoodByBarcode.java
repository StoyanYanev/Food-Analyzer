package bg.sofia.uni.fmi.mjt.food.analyzer.exceptions;

public class NotFoundFoodByBarcode extends FoodException {
    private static final long serialVersionUID = -2766875762772098583L;

    public NotFoundFoodByBarcode(final String message) {
        super(message);
    }
}