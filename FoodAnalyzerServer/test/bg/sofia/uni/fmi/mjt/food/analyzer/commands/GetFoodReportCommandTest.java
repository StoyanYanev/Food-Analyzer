package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.stub.FoodTestStub;

import com.google.zxing.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetFoodReportCommandTest {
    private FoodStorage foodStorageMoc;
    private FoodService foodServiceMoc;
    private Command command;
    private final FoodTestStub foodTestStub;

    public GetFoodReportCommandTest() {
        foodTestStub = new FoodTestStub();
    }

    @Before
    public void setUp() {
        foodServiceMoc = mock(FoodService.class);
        foodStorageMoc = mock(FoodStorage.class);
        command = new GetFoodReportCommand(foodServiceMoc, foodStorageMoc);
    }

    @Test
    public void testExecuteCommandWithCachedFoodShouldReturnValidFoodReport() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final FoodReport foodReport = foodTestStub.createFoodReport();
        final int fdcId = foodReport.getFdcId();

        final List<String> arguments = List.of(String.valueOf(fdcId));
        when(foodStorageMoc.getFoodReport(fdcId)).thenReturn(foodReport);

        final String expectedFoodReport = command.executeCommand(arguments);

        verify(foodServiceMoc, never()).getFoodReport(anyInt());
        assertEquals(expectedFoodReport, foodReport.toString());
    }

    @Test
    public void testExecuteCommandWithNoCachedFoodShouldReturnValidFoodReport() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final FoodReport foodReport = foodTestStub.createFoodReport();
        final int fdcId = foodReport.getFdcId();

        final List<String> arguments = List.of(String.valueOf(fdcId));
        when(foodStorageMoc.getFoodReport(fdcId)).thenReturn(null);
        when(foodServiceMoc.getFoodReport(fdcId)).thenReturn(foodReport);

        final String expectedFoodReport = command.executeCommand(arguments);

        assertEquals(expectedFoodReport, foodReport.toString());
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteCommandWithInvalidArgumentShouldThrowException() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final List<String> arguments = List.of(UUID.randomUUID()
                        .toString(),
                UUID.randomUUID()
                        .toString());
        command.executeCommand(arguments);
    }

    @Test(expected = InvalidFlagValueException.class)
    public void testExecuteCommandWithInvalidNumberOfArgumentsShouldThrowException() throws IOException, CommandException,
            FoodServiceException, FoodException, NotFoundException, InterruptedException {
        final List<String> arguments = List.of(UUID.randomUUID()
                .toString());
        command.executeCommand(arguments);
    }
}