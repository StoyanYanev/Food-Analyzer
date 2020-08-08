package bg.sofia.uni.fmi.mjt.food.analyzer.loders;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CashedFoodsFolderPaths;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodReportLoader extends Loader {
    public FoodReportLoader(final String foodReportStoragePath) {
        super(foodReportStoragePath);
    }

    /**
     * Returns cashed foods reports from the resource folder
     *
     * @return cached reports
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if class of a serialized object cannot be found
     */
    public Map<Integer, FoodReport> loadCachedReports() throws IOException, ClassNotFoundException {
        final List<Path> pathFiles = getPaths(CashedFoodsFolderPaths.CACHED_FOOD_REPORT_FOLDER_PATH);
        final Map<Integer, FoodReport> foodReportCache = new HashMap<>();
        for (final Path path : pathFiles) {
            try (var ois = new ObjectInputStream(Files.newInputStream(path))) {
                final FoodReport foodReport = (FoodReport) ois.readObject();
                foodReportCache.put(foodReport.getFdcId(), foodReport);
            }
        }

        return foodReportCache;
    }
}
