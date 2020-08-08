package bg.sofia.uni.fmi.mjt.food.analyzer.service;

import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.BrandedFood;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.Food;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.food.FoodList;
import bg.sofia.uni.fmi.mjt.food.analyzer.dtos.report.FoodReport;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.EmptyFoodNameException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.stub.FoodTestStub;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FoodServiceTest {
    private static final String API_KEY = "KEY";
    private static final int STATUS_CODE_OK = 200;
    private static final int STATUS_CODE_INTERNAL_SERVER_ERROR = 500;

    private HttpClient httpClientMock;
    @Mock
    private HttpResponse<String> httpResponseMock;
    @Mock
    private CompletableFuture<HttpResponse<String>> httpResponseCompletableFutureMock;
    @Mock
    private FoodService foodService;
    @Mock
    private CompletableFuture<String> completableFutureMockObject;
    private final FoodTestStub foodTestStub;

    public FoodServiceTest() {
        foodTestStub = new FoodTestStub();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        httpClientMock = mock(HttpClient.class);
        foodService = new FoodService(httpClientMock, API_KEY);

        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(
                httpResponseMock);
    }

    @Test(expected = EmptyFoodNameException.class)
    public void testGetFoodsWithEmptyFoodNameShouldThrowException() throws InterruptedException, FoodServiceException,
            EmptyFoodNameException, IOException {
        String foodName = "";
        foodService.getFoods(foodName);
    }

    @Test(expected = EmptyFoodNameException.class)
    public void testGetFoodsWithNullFoodNameShouldThrowException() throws InterruptedException, FoodServiceException,
            EmptyFoodNameException, IOException {
        foodService.getFoods(null);
        verify(httpClientMock, times(0)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }

    @Test
    public void testGetFoodsWithValidFoodNameShouldReturnValidFood() throws InterruptedException, FoodServiceException,
            EmptyFoodNameException, IOException, ExecutionException {
        final String foodName = UUID.randomUUID()
                .toString();
        final int currentPage = 1;
        final int totalPages = 2;
        final Food food = foodTestStub.createFood();
        final FoodList foodList = new FoodList(currentPage, totalPages, List.of(food));

        final String foodListJson = new Gson().toJson(foodList);

        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_OK);
        when(httpResponseMock.body()).thenReturn(foodListJson);
        when(httpClientMock.sendAsync(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenAnswer(answer -> {
            answer.getArguments();
            CompletableFuture<HttpResponse<String>> future = new CompletableFuture<>();
            future.complete(httpResponseMock);
            return future;
        });

        final List<Food> expectedFoods = foodService.getFoods(foodName);
        final int expectedFoodsSize = 2;

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        verify(httpClientMock, times(expectedFoodsSize)).sendAsync(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        assertNotNull(expectedFoods);
        assertEquals(expectedFoodsSize, expectedFoods.size());
        assertEquals(expectedFoods.get(0), food);
    }

    @Test(expected = FoodServiceException.class)
    public void testGetFoodsWithInvalidFoodNameShouldThrowException() throws InterruptedException, FoodServiceException,
            EmptyFoodNameException, IOException {
        final String invalidFoodName = UUID.randomUUID()
                .toString();
        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_INTERNAL_SERVER_ERROR);

        foodService.getFoods(invalidFoodName);

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }

    @Test
    public void testGetFoodReportWithValidFdcIdShouldReturnValidFood() throws InterruptedException, FoodServiceException, IOException {
        final FoodReport foodReport = foodTestStub.createFoodReport();
        final int fdcId = foodReport.getFdcId();

        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_OK);
        when(httpResponseMock.body()).thenReturn(new Gson().toJson(foodReport));
        final FoodReport expectedReport = foodService.getFoodReport(fdcId);

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        assertNotNull(expectedReport);
        assertEquals(expectedReport, foodReport);
    }

    @Test(expected = FoodServiceException.class)
    public void testGetFoodReportWithInvalidFdcIdShouldThrowException() throws InterruptedException, FoodServiceException, IOException {
        final int invalidFdcId = -1;

        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_INTERNAL_SERVER_ERROR);
        foodService.getFoodReport(invalidFdcId);

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }

    @Test
    public void testGetBrandedFoodWithValidBrandedFdcIdShouldReturnValidBrandedFood() throws InterruptedException, FoodServiceException,
            IOException {
        final BrandedFood food = foodTestStub.createBrandedFood();
        final int foodFdcId = food.getFdcId();

        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_OK);
        when(httpResponseMock.body()).thenReturn(new Gson().toJson(food));
        final BrandedFood brandedFood = foodService.getBrandedFood(foodFdcId);

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
        assertNotNull(brandedFood);
        assertEquals(brandedFood, food);
    }

    @Test(expected = FoodServiceException.class)
    public void testGetBrandedFoodWithInValidBrandedFdcIdShouldThrowException() throws InterruptedException, FoodServiceException,
            IOException {
        final int invalidBrandedFdcId = -1;

        when(httpResponseMock.statusCode()).thenReturn(STATUS_CODE_INTERNAL_SERVER_ERROR);
        foodService.getBrandedFood(invalidBrandedFdcId);

        verify(httpClientMock, times(1)).send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }
}