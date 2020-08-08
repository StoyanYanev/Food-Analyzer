package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidFlagValueException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.InvalidNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.NotFoundFoodByBarcode;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;
import bg.sofia.uni.fmi.mjt.food.analyzer.stub.FoodTestStub;

import com.google.zxing.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetFoodByBarcodeCommandTest {
    private FoodStorage foodStorageMoc;
    private FoodService foodServiceMoc;
    private Command command;
    private final FoodTestStub foodTestStub;

    public GetFoodByBarcodeCommandTest() {
        foodTestStub = new FoodTestStub();
    }

    @Before
    public void setUp() {
        foodServiceMoc = mock(FoodService.class);
        foodStorageMoc = mock(FoodStorage.class);
        command = new GetFoodByBarcodeCommand(foodServiceMoc, foodStorageMoc);
    }

    @Test
    public void testExecuteCommandWithCodeFlagShouldReturnValidFood() throws IOException, FoodException, CommandException,
            FoodServiceException, NotFoundException, InterruptedException {
        final BrandedFood brandedFood = foodTestStub.createBrandedFood();
        final String gtinUpc = brandedFood.getGtinUpc();

        final List<String> arguments = List.of("--code=" + gtinUpc);

        when(foodStorageMoc.getBrandedFood(gtinUpc)).thenReturn(brandedFood);
        final String expectedFood = command.executeCommand(arguments);

        assertEquals(expectedFood, brandedFood.toString());
    }

    @Test
    public void testExecuteCommandWithImageFlagShouldReturnValidFood() throws IOException, FoodException, CommandException,
            FoodServiceException, NotFoundException, InterruptedException {
        final BrandedFood brandedFood = foodTestStub.createBrandedFood();
        final String imageGtinUpc = "606991071083";
        final String imagePath = "test/resources/barcodeTest.png";
        final List<String> arguments = List.of("--img=" + imagePath);

        when(foodStorageMoc.getBrandedFood(imageGtinUpc)).thenReturn(brandedFood);
        final String expectedFood = command.executeCommand(arguments);

        assertEquals(expectedFood, brandedFood.toString());
    }

    @Test
    public void testExecuteCommandWithTwoFlagsShouldReturnBrandedFoodFromCodeFlag() throws IOException, FoodException, CommandException,
            FoodServiceException, NotFoundException, InterruptedException {
        final BrandedFood brandedFood = foodTestStub.createBrandedFood();
        final String gtinUpc = brandedFood.getGtinUpc();

        final String imagePath = "test/resources/barcodeTest.png";
        final List<String> arguments = List.of("--img=" + imagePath, "--code=" + gtinUpc);

        when(foodStorageMoc.getBrandedFood(gtinUpc)).thenReturn(brandedFood);
        final String expectedFood = command.executeCommand(arguments);

        assertEquals(expectedFood, brandedFood.toString());
    }

    @Test(expected = InvalidFlagValueException.class)
    public void testExecuteCommandWithInvalidFlagShouldThrowException() throws IOException, CommandException, FoodServiceException,
            FoodException, NotFoundException, InterruptedException {
        final List<String> arguments = List.of("--code=123", " --image=test");
        command.executeCommand(arguments);
        verify(foodStorageMoc, never()).getBrandedFood(anyString());
    }

    @Test(expected = NotFoundFoodByBarcode.class)
    public void testExecuteCommandWithNoCachedFoodShouldThrowException() throws IOException, FoodException, CommandException,
            FoodServiceException, NotFoundException, InterruptedException {
        final String gtinUpc = String.valueOf(foodTestStub.getRandomId());
        final List<String> arguments = List.of("--code=" + gtinUpc);
        when(foodStorageMoc.getBrandedFood(gtinUpc)).thenReturn(null);
        command.executeCommand(arguments);
    }

    @Test(expected = InvalidNumberOfArgumentsException.class)
    public void testExecuteCommandWithInvalidNumberOfArgumentsShouldThrowException() throws IOException, CommandException,
            FoodServiceException, FoodException, NotFoundException, InterruptedException {
        final List<String> arguments = List.of("--code=123", " --image=/test/1.png", " --img=test");
        command.executeCommand(arguments);
        verify(foodStorageMoc, never()).getBrandedFood(anyString());
    }
}