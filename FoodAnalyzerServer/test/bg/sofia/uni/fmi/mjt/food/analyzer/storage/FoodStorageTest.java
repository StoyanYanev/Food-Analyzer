package bg.sofia.uni.fmi.mjt.food.analyzer.storage;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.BrandedFoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodReportLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.stub.FoodTestStub;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodStorageTest {
    private FoodLoader foodLoader;
    private BrandedFoodLoader brandedFoodLoader;
    private FoodReportLoader foodReportLoader;
    private FoodStorage foodStorage;
    private final FoodTestStub foodTestStub;

    public FoodStorageTest() {
        foodTestStub = new FoodTestStub();
    }

    @Before
    public void setUp() throws IOException, ClassNotFoundException {
        foodLoader = mock(FoodLoader.class);
        brandedFoodLoader = mock(BrandedFoodLoader.class);
        foodReportLoader = mock(FoodReportLoader.class);
        foodStorage = new FoodStorage(foodLoader, brandedFoodLoader, foodReportLoader);
    }

    @Test
    public void testGetFoodShouldReturnValidFood() throws IOException, ClassNotFoundException {
        final List<String> foodNameWords = List.of("beef", "noodle", "soup");
        final Food food = foodTestStub.createFood(foodNameWords.get(0));

        when(foodLoader.loadCachedFoods()).thenReturn(Set.of(food));
        foodStorage = new FoodStorage(foodLoader, brandedFoodLoader, foodReportLoader);

        List<Food> actualFoods = foodStorage.getFood(foodNameWords);

        final int expectedSize = 1;
        assertEquals(expectedSize, actualFoods.size());
        assertEquals(food, actualFoods.get(0));
    }

    @Test
    public void testGetBrandedFoodWithCachedBrandedFoodShouldReturnValidBrandedFood() throws IOException, ClassNotFoundException {
        final BrandedFood brandedFood = foodTestStub.createBrandedFood();
        final String gtinUpc = brandedFood.getGtinUpc();
        final Map<String, BrandedFood> cashBrandedFood = new HashMap<>();
        cashBrandedFood.put(gtinUpc, brandedFood);

        when(brandedFoodLoader.loadCachedBrandedFood()).thenReturn(cashBrandedFood);
        foodStorage = new FoodStorage(foodLoader, brandedFoodLoader, foodReportLoader);

        Food actualBrandedFood = foodStorage.getBrandedFood(gtinUpc);
        assertEquals(brandedFood, actualBrandedFood);
    }

    @Test
    public void testGetBrandedFoodWithNoCachedBrandedFoodShouldReturnNull() {
        final String gtinUpc = UUID.randomUUID()
                .toString();
        when(foodStorage.getBrandedFood(gtinUpc)).thenReturn(null);

        assertNull(foodStorage.getBrandedFood(gtinUpc));
    }

    @Test
    public void testGetFoodReportWithCachedFoodReportShouldReturnValidFoodReport() throws IOException, ClassNotFoundException {
        final FoodReport foodReport = foodTestStub.createFoodReport();
        final int fdcId = foodReport.getFdcId();
        final Map<Integer, FoodReport> cashReport = new HashMap<>();
        cashReport.put(fdcId, foodReport);

        when(foodReportLoader.loadCachedReports()).thenReturn(cashReport);
        foodStorage = new FoodStorage(foodLoader, brandedFoodLoader, foodReportLoader);

        FoodReport actualFoodReport = foodStorage.getFoodReport(fdcId);
        assertEquals(foodReport, actualFoodReport);
    }

    @Test
    public void testGetFoodReportNoWithCachedFoodReportShouldReturnNull() {
        final int foodFdcId = foodTestStub.getRandomId();
        when(foodStorage.getFoodReport(foodFdcId)).thenReturn(null);

        assertNull(foodStorage.getFoodReport(foodFdcId));
    }
}