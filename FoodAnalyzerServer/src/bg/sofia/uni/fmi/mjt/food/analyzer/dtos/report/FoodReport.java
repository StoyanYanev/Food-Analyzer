package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report;

import java.io.Serializable;
import java.util.Objects;

public class FoodReport implements Serializable {
    private static final long serialVersionUID = -1940235219428316843L;

    private String description;
    private String ingredients;
    private int fdcId;
    private LabelNutrients labelNutrients;

    public FoodReport(final String description, final String ingredients, final int fdcId, final LabelNutrients labelNutrients) {
        this.description = description;
        this.ingredients = ingredients;
        this.fdcId = fdcId;
        this.labelNutrients = labelNutrients;
    }

    public String getName() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getFdcId() {
        return fdcId;
    }

    public LabelNutrients getLabelNutrients() {
        return labelNutrients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodReport)) {
            return false;
        }
        FoodReport that = (FoodReport) o;
        return fdcId == that.fdcId && description.equals(that.description) && ingredients.equals(that.ingredients) &&
                labelNutrients.equals(that.labelNutrients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, ingredients, fdcId, labelNutrients);
    }

    @Override
    public String toString() {
        StringBuilder foodReportBuilder = new StringBuilder();

        foodReportBuilder.append("===FOOD REPORT===")
                .append(System.lineSeparator())
                .append("description: ")
                .append(description)
                .append(System.lineSeparator())
                .append("ingredients: ")
                .append(ingredients)
                .append(System.lineSeparator())
                .append("fdcId: ")
                .append(fdcId)
                .append(System.lineSeparator())
                .append(labelNutrients);

        return foodReportBuilder.toString();
    }
}