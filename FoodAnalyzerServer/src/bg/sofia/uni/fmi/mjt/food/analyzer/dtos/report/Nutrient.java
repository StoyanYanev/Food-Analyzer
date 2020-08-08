package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report;

import java.io.Serializable;
import java.util.Objects;

public class Nutrient implements Serializable {
    private static final long serialVersionUID = 6617397645578596847L;

    private double value;

    public Nutrient(final double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nutrient)) {
            return false;
        }
        Nutrient nutrient = (Nutrient) o;
        return Double.compare(nutrient.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        StringBuilder nutrientBuilder = new StringBuilder();

        nutrientBuilder.append("nutrient value: ")
                .append(value);

        return nutrientBuilder.toString();
    }
}