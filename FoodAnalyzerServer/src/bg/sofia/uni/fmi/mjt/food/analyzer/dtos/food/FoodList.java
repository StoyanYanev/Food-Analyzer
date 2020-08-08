package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class FoodList implements Serializable {
    private static final long serialVersionUID = -4901498664793045852L;

    private int currentPage;
    private int totalPages;
    private List<Food> foods;

    public FoodList(final int currentPage, final int totalPages, List<Food> foods) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.foods = foods;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodList)) {
            return false;
        }
        FoodList foodList = (FoodList) o;
        return currentPage == foodList.currentPage && totalPages == foodList.totalPages && foods.equals(foodList.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPage, totalPages, foods);
    }

    @Override
    public String toString() {
        StringBuilder foodsBuilder = new StringBuilder();

        foodsBuilder.append("===FOODS===")
                .append(System.lineSeparator())
                .append("currentPage: ")
                .append(currentPage)
                .append(System.lineSeparator())
                .append("totalPages: ")
                .append(totalPages)
                .append(System.lineSeparator())
                .append("foods= ")
                .append(foods);

        return foodsBuilder.toString();
    }
}