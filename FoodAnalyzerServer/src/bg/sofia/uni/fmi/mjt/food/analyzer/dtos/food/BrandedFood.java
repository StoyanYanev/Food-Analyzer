package bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food;

import java.io.Serializable;
import java.util.Objects;

public class BrandedFood extends Food implements Serializable {
    private static final long serialVersionUID = 4802060880256344407L;

    private String gtinUpc;

    public BrandedFood(final int fdcId, final String description, final String dataType, final String gtinUpc) {
        super(fdcId, description, dataType);
        this.gtinUpc = gtinUpc;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrandedFood)) {
            return false;
        }
        BrandedFood that = (BrandedFood) o;
        return gtinUpc.equals(that.gtinUpc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gtinUpc);
    }

    @Override
    public String toString() {
        StringBuilder brandedBuilder = new StringBuilder();

        brandedBuilder.append(super.toString())
                .append(System.lineSeparator())
                .append("gtinUpc: ")
                .append(gtinUpc);

        return brandedBuilder.toString();
    }
}