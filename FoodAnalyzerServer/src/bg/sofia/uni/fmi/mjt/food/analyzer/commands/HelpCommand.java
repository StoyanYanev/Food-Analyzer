package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CommandInformation;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import java.util.List;

/**
 * Gives information about the available commands
 */
public class HelpCommand extends Command {
    private static final int MAX_NUMBER_OF_ARGUMENTS = 1;
    private static final String HELP_FLAG = "--help";

    public HelpCommand(final FoodService foodService, final FoodStorage foodStorage) {
        super(foodService, foodStorage);
    }

    @Override
    public String executeCommand(final List<String> arguments) throws InvalidNumberOfArgumentsException, InvalidFlagValueException {
        if (isNumberOfArgumentsValid(arguments)) {
            throw new InvalidNumberOfArgumentsException(MessageConstants.INVALID_NUMBER_OF_ARGUMENTS_MESSAGE);
        }
        if (!isCommandFlagValueValid(arguments)) {
            throw new InvalidFlagValueException(MessageConstants.INVALID_FLAG_VALUE_MESSAGE);
        }

        return CommandInformation.COMMAND_INFORMATION;
    }

    private boolean isNumberOfArgumentsValid(final List<String> arguments) {
        return arguments.isEmpty() || arguments.size() != MAX_NUMBER_OF_ARGUMENTS;
    }

    private boolean isCommandFlagValueValid(final List<String> arguments) {
        return arguments.get(0)
                .equals(HELP_FLAG);
    }
}