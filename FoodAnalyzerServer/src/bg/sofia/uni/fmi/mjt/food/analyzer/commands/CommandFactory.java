package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.Commands;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private Map<String, Command> commands;

    public CommandFactory(final FoodService foodService, final FoodStorage foodStorage) {
        commands = new HashMap<>();
        initializeCommands(foodService, foodStorage);
    }

    /**
     * Return command by given command name
     *
     * @param commandName the name of the command
     * @return command
     * @throws InvalidCommandException if command does not exists
     */
    public Command getCommand(final String commandName) throws InvalidCommandException {
        Command command = commands.get(commandName);
        if (command == null) {
            throw new InvalidCommandException(MessageConstants.INVALID_COMMAND_MESSAGE);
        }

        return command;
    }

    private void initializeCommands(final FoodService foodService, final FoodStorage foodStorage) {
        commands.put(Commands.GET_FOOD.getCommandName(), new GetFoodCommand(foodService, foodStorage));
        commands.put(Commands.GET_FOOD_REPORT.getCommandName(), new GetFoodReportCommand(foodService, foodStorage));
        commands.put(Commands.GET_FOOD_BY_BARCODE.getCommandName(), new GetFoodByBarcodeCommand(foodService, foodStorage));
        commands.put(Commands.FOOD_HELP.getCommandName(), new HelpCommand(foodService, foodStorage));
    }
}
