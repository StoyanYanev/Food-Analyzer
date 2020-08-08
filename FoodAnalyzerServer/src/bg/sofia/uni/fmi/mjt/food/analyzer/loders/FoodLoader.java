package bg.sofia.uni.fmi.mjt.food.analyzer.loders;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CashedFoodsFolderPaths;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodLoader extends Loader {
    public FoodLoader(final String foodStoragePath) {
        super(foodStoragePath);
    }

    /**
     * Returns cashed foods from the resource folder
     *
     * @return cached foods
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if class of a serialized object cannot be found
     */
    public Set<Food> loadCachedFoods() throws IOException, ClassNotFoundException {
        final List<Path> pathFiles = getPaths(CashedFoodsFolderPaths.CACHED_FOODS_FOLDER_PATH);
        final Set<Food> foodsCache = new HashSet<>();
        for (final Path path : pathFiles) {
            try (var ois = new ObjectInputStream(Files.newInputStream(path))) {
                final Food food = (Food) ois.readObject();
                foodsCache.add(food);
            }
        }

        return foodsCache;
    }
}
