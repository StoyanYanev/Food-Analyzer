package bg.sofia.uni.fmi.mjt.food.analyzer.commands;

import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;

import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.List;

public abstract class Command {
    protected FoodService foodService;
    protected FoodStorage foodStorage;

    public Command(final FoodService foodService, final FoodStorage foodStorage) {
        this.foodService = foodService;
        this.foodStorage = foodStorage;
    }

    /**
     * Executes the command with the given arguments
     *
     * @param arguments of the command
     * @return result of execution
     * @throws IOException          if an error occurs during reading
     * @throws NotFoundException    if can not find the given image
     * @throws InterruptedException if the operation is interrupted when trying to get the food from service
     * @throws FoodException        if food name or food barcode can not be found
     * @throws FoodServiceException if failed to get food from service
     * @throws CommandException     if arguments or flags value are invalid
     */
    public abstract String executeCommand(final List<String> arguments) throws IOException, NotFoundException, InterruptedException,
            FoodException, FoodServiceException, CommandException;
}