package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidCommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.Commands;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CommandFactoryTest {
    private FoodStorage foodStorageMoc;
    private FoodService foodServiceMoc;
    private CommandFactory commandFactory;

    @Before
    public void setUp() {
        foodServiceMoc = mock(FoodService.class);
        foodStorageMoc = mock(FoodStorage.class);
        commandFactory = new CommandFactory(foodServiceMoc, foodStorageMoc);
    }

    @Test(expected = InvalidCommandException.class)
    public void testGetInvalidCommandShouldThrowException() throws InvalidCommandException {
        final String invalidCommandName = UUID.randomUUID()
                .toString();

        commandFactory.getCommand(invalidCommandName);
    }

    @Test
    public void testGetCommandShouldReturnValidCommand() throws InvalidCommandException {
        Command command = commandFactory.getCommand(Commands.GET_FOOD.getCommandName());

        assertTrue(command instanceof GetFoodCommand);
    }
}