package bg.sofia.uni.fmi.mjt.food.analyzer.converters;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.FoodList;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;

import com.google.gson.Gson;

public final class JsonConverter {

    private JsonConverter() {

    }

    /**
     * Returns converted json file to FoodList object
     *
     * @param jsonFoodList
     * @return FoodList object
     * @see FoodList
     */
    public static FoodList getConvertedFoodList(final String jsonFoodList) {
        return new Gson().fromJson(jsonFoodList, FoodList.class);
    }

    /**
     * Returns converted json file to BrandedFood object
     *
     * @param jsonBrandedFood
     * @return BrandedFood object
     * @see BrandedFood
     */
    public static BrandedFood getConvertedBrandedFood(final String jsonBrandedFood) {
        return new Gson().fromJson(jsonBrandedFood, BrandedFood.class);
    }

    /**
     * Returns converted json file to FoodReport object
     *
     * @param jsonFoodReport
     * @return FoodReport object
     * @see FoodReport
     */
    public static FoodReport getConvertedFoodReport(final String jsonFoodReport) {
        return new Gson().fromJson(jsonFoodReport, FoodReport.class);
    }
}