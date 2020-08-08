package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food;

import java.io.Serializable;
import java.util.Objects;

public class Food implements Serializable {
    private static final long serialVersionUID = -6366665616480843631L;

    private int fdcId;
    private String description;
    private String dataType;

    public Food(final int fdcId, final String description, final String dataType) {
        this.fdcId = fdcId;
        this.description = description;
        this.dataType = dataType;
    }

    public int getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Food)) {
            return false;
        }
        Food food = (Food) o;
        return fdcId == food.fdcId && description.equals(food.description) && dataType.equals(food.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fdcId, description, dataType);
    }

    @Override
    public String toString() {
        StringBuilder foodBuilder = new StringBuilder();

        foodBuilder.append("===FOOD DETAILS===")
                .append(System.lineSeparator())
                .append("fdcId: ")
                .append(fdcId)
                .append(System.lineSeparator())
                .append("description: ")
                .append(description)
                .append(System.lineSeparator())
                .append("type: ")
                .append(dataType);

        return foodBuilder.toString();
    }
}