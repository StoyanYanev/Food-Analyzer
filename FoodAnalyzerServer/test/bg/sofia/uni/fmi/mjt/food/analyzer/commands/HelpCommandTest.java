package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CommandInformation;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

import com.google.zxing.NotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class HelpCommandTest {
    private FoodStorage foodStorageMoc;
    private FoodService foodServiceMoc;
    private Command command;

    @Before
    public void setUp() {
        foodServiceMoc = mock(FoodService.class);
        foodStorageMoc = mock(FoodStorage.class);
        command = new HelpCommand(foodServiceMoc, foodStorageMoc);
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteCommandWithInvalidArgumentShouldThrowException() throws InterruptedException, IOException, FoodServiceException,
            NotFoundException, CommandException, FoodException {
        final List<String> arguments = List.of(UUID.randomUUID()
                        .toString(),
                UUID.randomUUID()
                        .toString());
        command.executeCommand(arguments);
    }

    @Test(expected = InvalidFlagValueException.class)
    public void testExecuteCommandWithInvalidFlagShouldThrowException() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final List<String> invalidFlags = List.of(UUID.randomUUID()
                .toString());
        command.executeCommand(invalidFlags);
    }

    @Test
    public void testExecuteCommandWithValidFlagShouldReturnValidResult() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final List<String> helpFlag = List.of("--help");
        final String actualResult = command.executeCommand(helpFlag);

        assertEquals(CommandInformation.COMMAND_INFORMATION, actualResult);
    }
}