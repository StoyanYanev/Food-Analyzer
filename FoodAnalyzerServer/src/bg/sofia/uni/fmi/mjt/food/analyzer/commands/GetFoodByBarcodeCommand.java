package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.converters.BarcodeConverter;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.NotFoundFoodByBarcode;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.List;

/**
 * This command is executed by branded food (should be given GtinUpc of code flag or absolute path to barcode image of the image flag)
 * and gives information about the food (description, fdcId-unique food identifier, data type, gtinUpc-Global Trade Item Number)
 */
public class GetFoodByBarcodeCommand extends Command {
    protected static final int POSITION_OF_FIRST_FLAG = 0;
    private static final int POSITION_OF_SECOND_FLAG = 1;
    private static final int MAX_NUMBER_OF_ARGUMENTS = 2;
    private static final String ARGUMENT_DELIMITER = "=";
    private static final int FLAG_VALUE_POSITION = 1;
    private static final String IMAGE_FLAG = "--img=";

    public GetFoodByBarcodeCommand(final FoodService foodService, final FoodStorage foodStorage) {
        super(foodService, foodStorage);
    }

    @Override
    public String executeCommand(final List<String> arguments) throws IOException, NotFoundException, NotFoundFoodByBarcode,
            InvalidNumberOfArgumentsException, InvalidFlagValueException {
        if (!isNumberOfArgumentsValid(arguments)) {
            throw new InvalidNumberOfArgumentsException(MessageConstants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE);
        }
        if (!isCommandFlagValueValid(arguments)) {
            throw new InvalidFlagValueException(MessageConstants.INVALID_FLAG_VALUE_MESSAGE);
        }

        return getFoodByBarcode(arguments);
    }

    private boolean isNumberOfArgumentsValid(final List<String> arguments) {
        return !arguments.isEmpty() && arguments.size() <= MAX_NUMBER_OF_ARGUMENTS;
    }

    private boolean isCommandFlagValueValid(final List<String> arguments) {
        for (final String argument : arguments) {
            //check if code flag value contains only numbers and image flag value is a path
            if (!argument.matches("--code=[0-9]+$|" + "--img=\\/?([A-z0-9-_+]+\\/)*([A-z0-9]+\\.[a-z]+)$")) {
                return false;
            }
        }

        return true;
    }

    private String getFoodByBarcode(final List<String> arguments) throws IOException, NotFoundException, NotFoundFoodByBarcode {
        final String flagName = arguments.get(POSITION_OF_FIRST_FLAG);
        String flagValue = flagName.split(ARGUMENT_DELIMITER)[FLAG_VALUE_POSITION];
        boolean isArgumentContainingImageFlag = flagName.contains(IMAGE_FLAG);

        if (arguments.size() == MAX_NUMBER_OF_ARGUMENTS && isArgumentContainingImageFlag) {
            //skip image flag and get value of code flag
            flagValue = arguments.get(POSITION_OF_SECOND_FLAG)
                    .split(ARGUMENT_DELIMITER)[FLAG_VALUE_POSITION];
            isArgumentContainingImageFlag = false;
        }

        String barcode;
        if (isArgumentContainingImageFlag) {
            barcode = BarcodeConverter.getConvertedGtinUpcFromImage(flagValue);
        } else {
            barcode = flagValue;
        }

        return getFoodByBarcode(barcode);
    }

    private String getFoodByBarcode(final String barcode) throws NotFoundFoodByBarcode {
        final Food cashedFood = foodStorage.getBrandedFood(barcode);
        if (cashedFood == null) {
            throw new NotFoundFoodByBarcode(MessageConstants.NOT_FOUND_FOOD_BY_BARCODE_MESSAGE);
        }

        return cashedFood.toString();
    }
}
