package bg.sofia.uni.fmi.mjt.food.analyzer.loders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Loader {
    private String storagePath;

    public Loader(final String storagePath) {
        this.storagePath = storagePath;
    }

    protected List<Path> getPaths(final String folderPath) throws IOException {
        try (final Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            return paths.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
    }

    public String getStoragePath() {
        return storagePath;
    }
}
