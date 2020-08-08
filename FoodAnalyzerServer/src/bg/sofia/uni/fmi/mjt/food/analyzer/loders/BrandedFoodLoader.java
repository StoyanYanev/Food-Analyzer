package bg.sofia.uni.fmi.mjt.food.analyzer.loders;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CashedFoodsFolderPaths;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandedFoodLoader extends Loader {
    public BrandedFoodLoader(final String brandedFoodStoragePath) {
        super(brandedFoodStoragePath);
    }

    /**
     * Returns cached branded foods from the resource folder
     *
     * @return cached branded foods
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if class of a serialized object cannot be found
     */
    public Map<String, BrandedFood> loadCachedBrandedFood() throws IOException, ClassNotFoundException {
        final List<Path> pathFiles = getPaths(CashedFoodsFolderPaths.CACHED_BRANDED_FOODS_FOLDER_PATH);
        final Map<String, BrandedFood> brandedFoodCache = new HashMap<>();
        for (final Path path : pathFiles) {
            try (var ois = new ObjectInputStream(Files.newInputStream(path))) {
                final BrandedFood brandedFood = (BrandedFood) ois.readObject();
                brandedFoodCache.put(brandedFood.getGtinUpc(), brandedFood);
            }
        }

        return brandedFoodCache;
    }
}
