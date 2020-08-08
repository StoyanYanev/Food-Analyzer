package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import java.io.IOException;
import java.util.List;

/**
 * Gives report about the food (description, ingredients, fdcId, calories, carbohydrates, protein, fiber, fat)
 * by given unique food identifier (fdcId)
 */
public class GetFoodReportCommand extends Command {
    private static final int MAX_NUMBER_OF_ARGUMENTS = 1;

    public GetFoodReportCommand(final FoodService foodService, final FoodStorage foodStorage) {
        super(foodService, foodStorage);
    }

    @Override
    public String executeCommand(final List<String> arguments) throws InterruptedException, IOException, FoodServiceException,
            InvalidNumberOfArgumentsException, InvalidFlagValueException {
        if (!isNumberOfArgumentsValid(arguments)) {
            throw new InvalidNumberOfArgumentsException(MessageConstants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE);
        }
        if (!isCommandFlagValueValid(arguments)) {
            throw new InvalidFlagValueException(MessageConstants.INVALID_FLAG_VALUE_MESSAGE);
        }

        return getFoodReport(arguments);
    }

    private boolean isNumberOfArgumentsValid(final List<String> arguments) {
        return !arguments.isEmpty() && arguments.size() == MAX_NUMBER_OF_ARGUMENTS;
    }

    private boolean isCommandFlagValueValid(final List<String> arguments) {
        for (String argument : arguments) {
            if (!argument.matches("[0-9]+$")) {
                return false;
            }
        }

        return true;
    }

    private String getFoodReport(final List<String> arguments) throws InterruptedException, IOException, FoodServiceException {
        int foodFdcId = Integer.parseInt(arguments.get(0));
        final FoodReport cashedFoodReport = foodStorage.getFoodReport(foodFdcId);

        if (cashedFoodReport != null) {
            return cashedFoodReport.toString();
        }

        return getFoodReportFromService(foodFdcId);
    }

    private String getFoodReportFromService(final int foodFdcId) throws InterruptedException, IOException, FoodServiceException {
        final FoodReport foodReport = foodService.getFoodReport(foodFdcId);
        foodStorage.cacheFoodReport(foodReport);

        return foodReport.toString();
    }
}
