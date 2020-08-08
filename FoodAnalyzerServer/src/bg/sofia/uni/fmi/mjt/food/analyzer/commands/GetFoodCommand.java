package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.EmptyFoodNameException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import java.io.IOException;
import java.util.List;

/**
 * Gives information about the food (description, fdcId-unique food identifier, data type, gtinUpc-only for food with data type=branded)
 */
public class GetFoodCommand extends Command {
    private static final String SPACE = "%20";
    private static final String BRANDED_FOOD = "Branded";

    public GetFoodCommand(final FoodService foodService, final FoodStorage foodStorage) {
        super(foodService, foodStorage);
    }

    @Override
    public String executeCommand(final List<String> arguments) throws InterruptedException, EmptyFoodNameException, IOException,
            FoodServiceException, InvalidFlagValueException {
        if (!isCommandFlagValueValid(arguments)) {
            throw new InvalidFlagValueException(MessageConstants.INVALID_FLAG_VALUE_MESSAGE);
        }

        List<Food> foods = foodStorage.getFood(arguments);
        if (foods == null || foods.isEmpty()) {
            foods = getFoodsFromService(arguments);
        }

        return convertFoodsToString(foods);
    }

    private boolean isCommandFlagValueValid(final List<String> arguments) {
        for (String argument : arguments) {
            if (!argument.matches("[a-zA-Z0-9%.,()-;/]+$")) {
                return false;
            }
        }

        return true;
    }

    private String convertFoodsToString(final List<Food> foods) {
        final StringBuilder foodsBuilder = new StringBuilder();

        for (int i = 0; i < foods.size(); i++) {
            foodsBuilder.append(foods.get(i)
                    .toString());
            if (i + 1 < foods.size()) {
                foodsBuilder.append(System.lineSeparator());
            }
        }

        return foodsBuilder.toString();
    }

    private List<Food> getFoodsFromService(final List<String> arguments) throws InterruptedException, FoodServiceException,
            EmptyFoodNameException, IOException {
        final String foodName = constructFoodNameByArguments(arguments);
        final List<Food> foods = foodService.getFoods(foodName);
        cachedFoods(foods);

        return foods;
    }

    private String constructFoodNameByArguments(final List<String> arguments) {
        final StringBuilder foodName = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            foodName.append(arguments.get(i));

            if (i < arguments.size() - 1) {
                foodName.append(SPACE);
            }
        }

        return foodName.toString();
    }

    private void cachedFoods(final List<Food> foods) throws InterruptedException, IOException, FoodServiceException {
        for (final Food food : foods) {
            foodStorage.cacheFood(food);

            if (food.getDataType()
                    .equals(BRANDED_FOOD)) {
                final BrandedFood brandedFood = foodService.getBrandedFood(food.getFdcId());
                foodStorage.cacheBrandedFood(brandedFood);
            }
        }
    }
}