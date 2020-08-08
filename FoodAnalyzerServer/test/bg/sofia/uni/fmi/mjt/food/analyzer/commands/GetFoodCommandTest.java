package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.stub.FoodTestStub;

import com.google.zxing.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetFoodCommandTest {
    private FoodService foodServiceMoc;
    private FoodStorage foodStorageMoc;
    private Command command;
    private final FoodTestStub foodTestStub;

    public GetFoodCommandTest() {
        foodTestStub = new FoodTestStub();
    }

    @Before
    public void setUp() {
        foodServiceMoc = mock(FoodService.class);
        foodStorageMoc = mock(FoodStorage.class);
        command = new GetFoodCommand(foodServiceMoc, foodStorageMoc);
    }

    @Test(expected = InvalidFlagValueException.class)
    public void testExecuteCommandWithInvalidArgumentsShouldThrowException() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final List<String> arguments = List.of("!", "~");
        command.executeCommand(arguments);
    }

    @Test
    public void testExecuteCommandWithCachedFoodShouldReturnValidFood() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final Food food = foodTestStub.createFood();
        final String foodName = food.getDescription();

        final List<String> arguments = List.of(foodName);
        when(foodStorageMoc.getFood(arguments)).thenReturn(List.of(food));

        final String expectedFood = command.executeCommand(arguments);

        verify(foodServiceMoc, never()).getFoods(any());
        assertEquals(expectedFood, food.toString());
    }

    @Test
    public void testExecuteCommandWithNoCachedFoodShouldReturnValidFood() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final Food food = foodTestStub.createFood();
        final String foodName = food.getDescription();

        final List<String> arguments = List.of(foodName);
        when(foodStorageMoc.getFood(arguments)).thenReturn(null);
        when(foodServiceMoc.getFoods(foodName)).thenReturn(List.of(food));
        doNothing().when(foodStorageMoc)
                .cacheFood(food);

        final String expectedFood = command.executeCommand(arguments);

        verify(foodStorageMoc, never()).getBrandedFood(any());
        verify(foodStorageMoc, never()).cacheBrandedFood(any());
        verify(foodStorageMoc).cacheFood(food);

        assertEquals(expectedFood, food.toString());
    }
}