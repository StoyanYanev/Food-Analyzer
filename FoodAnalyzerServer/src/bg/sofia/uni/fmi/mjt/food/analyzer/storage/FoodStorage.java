package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.BrandedFoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodReportLoader;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FoodStorage {
    private static final String FILE_EXTENSION = ".txt";

    private final FoodLoader foodLoader;
    private final BrandedFoodLoader brandedFoodLoader;
    private final FoodReportLoader foodReportLoader;
    private final Set<Food> foodsCache;
    private final Map<Integer, FoodReport> foodReportCache;
    private final Map<String, BrandedFood> brandedFoodCache;

    public FoodStorage(final FoodLoader foodLoader,
                       final BrandedFoodLoader brandedFoodLoader,
                       final FoodReportLoader foodReportLoader) throws IOException, ClassNotFoundException {
        this.foodLoader = foodLoader;
        this.brandedFoodLoader = brandedFoodLoader;
        this.foodReportLoader = foodReportLoader;
        foodsCache = foodLoader.loadCachedFoods();
        brandedFoodCache = brandedFoodLoader.loadCachedBrandedFood();
        foodReportCache = foodReportLoader.loadCachedReports();
    }

    /**
     * Caches the given food locally
     *
     * @param food to be cached
     * @throws IOException if an I/O error occurs
     */
    public void cacheFood(final Food food) throws IOException {
        final String pathToFile = foodLoader.getStoragePath() + File.separator + food.getFdcId() + FILE_EXTENSION;
        final Path path = Paths.get(pathToFile);
        cacheObject(food, path);
        foodsCache.add(food);
    }

    /**
     * Caches the given branded food locally
     *
     * @param brandedFood to be cached
     * @throws IOException if an I/O error occurs
     */
    public void cacheBrandedFood(final BrandedFood brandedFood) throws IOException {
        final String pathToFile = brandedFoodLoader.getStoragePath() + File.separator + brandedFood.getGtinUpc() + FILE_EXTENSION;
        final Path path = Paths.get(pathToFile);
        cacheObject(brandedFood, path);
        brandedFoodCache.put(brandedFood.getGtinUpc(), brandedFood);
    }

    /**
     * Caches the given food report locally
     *
     * @param foodReport to be cached
     * @throws IOException if an I/O error occurs
     */
    public void cacheFoodReport(final FoodReport foodReport) throws IOException {
        final String pathToFile = foodReportLoader.getStoragePath() + File.separator + foodReport.getFdcId() + FILE_EXTENSION;
        Path path = Path.of(pathToFile);
        cacheObject(foodReport, path);
        foodReportCache.put(foodReport.getFdcId(), foodReport);
    }

    /**
     * Returns cached foods
     *
     * @param foodNameWords food name with more then one word
     * @return cached foods
     */
    public List<Food> getFood(final List<String> foodNameWords) {
        final List<Food> foods = new ArrayList<>();
        for (final Food food : foodsCache) {
            if (isContainingWords(food, foodNameWords)) {
                foods.add(food);
            }
        }

        return foods;
    }

    /**
     * Returns cached branded foods
     *
     * @param gtinUpc GtinUpc(Global Trade Item Number)
     * @return cached branded foods
     */
    public Food getBrandedFood(final String gtinUpc) {
        return brandedFoodCache.get(gtinUpc);
    }

    /**
     * Gets cached food report
     *
     * @param fdcId unique food identifier
     * @return cached food report
     */
    public FoodReport getFoodReport(final int fdcId) {
        return foodReportCache.get(fdcId);
    }

    private void cacheObject(final Object object, final Path path) throws IOException {
        try (final var oop = new ObjectOutputStream(Files.newOutputStream(path))) {
            oop.writeObject(object);
            oop.flush();
        }
    }

    private boolean isContainingWords(final Food food, final List<String> words) {
        for (final String word : words) {
            if (food.getDescription()
                    .toLowerCase()
                    .contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}