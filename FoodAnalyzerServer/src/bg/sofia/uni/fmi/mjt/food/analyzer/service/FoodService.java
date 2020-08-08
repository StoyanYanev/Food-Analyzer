package bg.sofia.uni.fmi.mjt.food.analyzer.service;

import bg.sofia.uni.fmi.mjt.food.analyzer.converters.JsonConverter;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.FoodList;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.EmptyFoodNameException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.utilitis.MessageConstants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FoodService {
    private static final int DEFAULT_PAGE = 1;
    private static final String FOOD_DATA_API_URL = "https://api.nal.usda.gov/fdc/v1/";
    private static final int RATE_LIMIT = 2;
    private static final int STATUS_CODE_OK = 200;

    private HttpClient client;
    private String apiKey;

    public FoodService(final HttpClient client, final String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    /**
     * Returns foods from API by given food name
     *
     * @param foodName name of the food
     * @return foods
     * @throws EmptyFoodNameException if the given food name is empty
     * @throws InterruptedException   if the operation is interrupted when trying to get the food
     * @throws IOException            if an I/O error occurs
     * @throws FoodServiceException   if failed to get food
     */
    public List<Food> getFoods(final String foodName) throws EmptyFoodNameException, InterruptedException, IOException,
            FoodServiceException {
        if (foodName == null || foodName.isEmpty()) {
            throw new EmptyFoodNameException(MessageConstants.EMPTY_FOOD_NAME_MESSAGE);
        }

        final HttpResponse<String> foodDataResponse = getFoodResponse(buildUriByFoodName(foodName));
        final FoodList foods = JsonConverter.getConvertedFoodList(foodDataResponse.body());

        return getFoodsFromAllPages(foodName, foods);
    }

    /**
     * Returns food report from API by given unique food identifier
     *
     * @param foodFdcId unique food identifier
     * @return food report
     * @throws InterruptedException if the operation is interrupted when trying to get the food
     * @throws IOException          if an I/O error occurs
     * @throws FoodServiceException if failed to get food
     */
    public FoodReport getFoodReport(final int foodFdcId) throws InterruptedException, IOException, FoodServiceException {
        final HttpResponse<String> foodDataResponse = getFoodResponse(buildUriByFoodFdcId(foodFdcId));

        return JsonConverter.getConvertedFoodReport(foodDataResponse.body());
    }

    /**
     * Returns branded food from API by given GtinUpc(Global Trade Item Number)
     *
     * @param foodFdcId unique food identifier
     * @return branded food
     * @throws InterruptedException if the operation is interrupted when trying to get the food
     * @throws IOException          if an I/O error occurs
     * @throws FoodServiceException if failed to get food
     */
    public BrandedFood getBrandedFood(final int foodFdcId) throws InterruptedException, IOException, FoodServiceException {
        final HttpResponse<String> foodDataResponse = getFoodResponse(buildUriByFoodFdcId(foodFdcId));

        return JsonConverter.getConvertedBrandedFood(foodDataResponse.body());
    }

    private URI buildUriByFoodName(final String foodName) {
        return buildUriByPageNumber(foodName, DEFAULT_PAGE);
    }

    private URI buildUriByPageNumber(final String foodName, final int pageNumber) {
        final StringBuilder uri = new StringBuilder();
        uri.append(FOOD_DATA_API_URL)
                .append("search?generalSearchInput=")
                .append(foodName)
                .append("&pageNumber=")
                .append(pageNumber)
                .append("&requireAllWords=true")
                .append("&api_key=")
                .append(apiKey);

        return URI.create(uri.toString());
    }

    private URI buildUriByFoodFdcId(final int foodFdcId) {
        final StringBuilder uri = new StringBuilder();
        uri.append(FOOD_DATA_API_URL)
                .append(foodFdcId)
                .append("?api_key=")
                .append(apiKey);

        return URI.create(uri.toString());
    }

    private HttpResponse<String> getFoodResponse(final URI url) throws IOException, InterruptedException, FoodServiceException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        final HttpResponse<String> foodDataResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (foodDataResponse.statusCode() != STATUS_CODE_OK) {
            throw new FoodServiceException(MessageConstants.FAILED_TO_GET_FOOD_MESSAGE);
        }

        return foodDataResponse;
    }

    private List<Food> getFoodsFromAllPages(final String foodName, final FoodList foods) {
        final List<CompletableFuture<String>> foodsResponse = new ArrayList<>();
        for (int currentPage = 1; currentPage <= RATE_LIMIT && currentPage <= foods.getTotalPages(); currentPage++) {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildUriByPageNumber(foodName, currentPage))
                    .build();

            final CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            final CompletableFuture<String> result = httpResponseCompletableFuture.thenApply(HttpResponse::body);
            foodsResponse.add(result);

        }

        CompletableFuture.allOf(foodsResponse.toArray(CompletableFuture[]::new))
                .join();

        final List<Food> foodsFromAllPages = new ArrayList<>();
        for (final CompletableFuture<String> response : foodsResponse) {
            FoodList currentFoods = JsonConverter.getConvertedFoodList(response.getNow(null));

            foodsFromAllPages.addAll(currentFoods.getFoods());
        }

        return foodsFromAllPages;
    }
}