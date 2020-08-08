package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report;

import java.io.Serializable;
import java.util.Objects;

public class LabelNutrients implements Serializable {
    private static final long serialVersionUID = 1563926811125229663L;

    private Nutrient fat;
    private Nutrient fiber;
    private Nutrient protein;
    private Nutrient calories;
    private Nutrient carbohydrates;

    public LabelNutrients(final Nutrient fat,
                          final Nutrient fiber,
                          final Nutrient protein,
                          final Nutrient calories,
                          final Nutrient carbohydrates) {
        this.fat = fat;
        this.fiber = fiber;
        this.protein = protein;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
    }

    public Nutrient getFat() {
        return fat;
    }

    public Nutrient getFiber() {
        return fiber;
    }

    public Nutrient getProtein() {
        return protein;
    }

    public Nutrient getCalories() {
        return calories;
    }

    public Nutrient getCarbohydrates() {
        return carbohydrates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabelNutrients)) {
            return false;
        }
        LabelNutrients that = (LabelNutrients) o;
        return fat.equals(that.fat) && fiber.equals(that.fiber) && protein.equals(that.protein) && calories.equals(that.calories) &&
                carbohydrates.equals(that.carbohydrates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fat, fiber, protein, calories, carbohydrates);
    }

    @Override
    public String toString() {
        StringBuilder labelNutrientsBuilder = new StringBuilder();

        labelNutrientsBuilder.append("Label nutrients: ")
                .append(System.lineSeparator())
                .append("-fat ")
                .append(fat)
                .append(System.lineSeparator())
                .append("-fiber ")
                .append(fiber)
                .append(System.lineSeparator())
                .append("-protein ")
                .append(protein)
                .append(System.lineSeparator())
                .append("-calories ")
                .append(calories)
                .append(System.lineSeparator())
                .append("-carbohydrates ")
                .append(carbohydrates);

        return labelNutrientsBuilder.toString();
    }
}