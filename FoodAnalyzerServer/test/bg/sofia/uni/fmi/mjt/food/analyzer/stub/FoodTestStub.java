package bg.sofia.uni.fmi.mjt.food.analyzer.stub;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.LabelNutrients;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.Nutrient;

import java.util.Random;
import java.util.UUID;

public class FoodTestStub {
    private final Random rand;

    public FoodTestStub() {
        rand = new Random();
    }

    public BrandedFood createBrandedFood() {
        final String gtinUpc = String.valueOf(getRandomId());
        final int foodFdcId = getRandomId();
        final String description = UUID.randomUUID()
                .toString();
        final String dataType = "Branded";
        return new BrandedFood(foodFdcId, description, dataType, gtinUpc);
    }

    public Food createFood() {
        return createFood(UUID.randomUUID()
                .toString());
    }

    public Food createFood(final String description) {
        final int foodFdcId = getRandomId();
        final String dataType = UUID.randomUUID()
                .toString();
        return new Food(foodFdcId, description, dataType);
    }

    public FoodReport createFoodReport() {
        final int fdcId = getRandomId();
        final double value = 0.5016;
        final Nutrient nutrient = new Nutrient(value);
        final LabelNutrients nutrients = new LabelNutrients(nutrient, nutrient, nutrient, nutrient, nutrient);
        return new FoodReport("BREAD", "ENRICHED FLOUR (WHEAT FLOUR)", fdcId, nutrients);
    }

    public int getRandomId() {
        return rand.nextInt(Integer.SIZE - 1);
    }
}
