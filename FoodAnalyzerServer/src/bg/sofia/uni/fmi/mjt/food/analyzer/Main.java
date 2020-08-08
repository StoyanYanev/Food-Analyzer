package bg.sofia.uni.fmi.mjt.food.analyzer;

import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.CashedFoodsFolderPaths;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

public class Main {
    public static void main(String[] args) {
        try {
            final String apiKey = "NvZMLAWhobduyaJFfrbFW8oqfEaetbFUoUWimAmR";
            final ServerSocketChannel socketChannel = ServerSocketChannel.open();
            final FoodAnalyzerServer server = new FoodAnalyzerServer(socketChannel,
                    apiKey,
                    CashedFoodsFolderPaths.CACHED_FOODS_FOLDER_PATH,
                    CashedFoodsFolderPaths.CACHED_BRANDED_FOODS_FOLDER_PATH,
                    CashedFoodsFolderPaths.CACHED_FOOD_REPORT_FOLDER_PATH);
            server.start();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
