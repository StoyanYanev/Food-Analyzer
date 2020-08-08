package bg.sofia.uni.fmi.mjt.food.analyzer;

import bg.sofia.uni.fmi.mjt.food.analyzer.commands.Command;
import bg.sofia.uni.fmi.mjt.food.analyzer.commands.CommandFactory;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodException;
import bg.sofia.uni.fmi.mjt.food.analyzer.exceptions.FoodServiceException;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.BrandedFoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.loders.FoodReportLoader;
import bg.sofia.uni.fmi.mjt.food.analyzer.service.FoodService;
import bg.sofia.uni.fmi.mjt.food.analyzer.storage.FoodStorage;

import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FoodAnalyzerServer {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;
    private static final String DELIMITER = " ";

    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer buffer;
    private FoodStorage foodStorage;
    private FoodService foodService;
    private CommandFactory commandFactory;

    public FoodAnalyzerServer(final ServerSocketChannel serverSocketChannel,
                              final String apiKey,
                              final String foodStoragePath,
                              final String brandedFoodStoragePath,
                              final String foodReportStoragePath) throws IOException, ClassNotFoundException {
        this.serverSocketChannel = serverSocketChannel;
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        final FoodLoader foodLoader = new FoodLoader(foodStoragePath);
        final BrandedFoodLoader brandedFoodLoader = new BrandedFoodLoader(brandedFoodStoragePath);
        final FoodReportLoader foodReportLoader = new FoodReportLoader(foodReportStoragePath);
        foodStorage = new FoodStorage(foodLoader, brandedFoodLoader, foodReportLoader);
        foodService = new FoodService(HttpClient.newHttpClient(), apiKey);
        commandFactory = new CommandFactory(foodService, foodStorage);
    }

    public void start() {
        try {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            final Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                final int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                handleReadyChannels(selector);
            }
        } catch (IOException | NotFoundException | InterruptedException e) {
            System.err.println("An error has occurred: " + e.getMessage());
        } finally {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                System.err.println("Exception thrown by close Socket: " + e.getMessage());
            }
        }
    }

    private void handleReadyChannels(final Selector selector) throws IOException, NotFoundException, InterruptedException {
        final Set<SelectionKey> selectedKeys = selector.selectedKeys();
        final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()) {
            final SelectionKey key = keyIterator.next();
            if (key.isReadable()) {
                final SocketChannel client = (SocketChannel) key.channel();
                if (client == null) {
                    continue;
                }
                executeClientCommand(client);
            } else if (key.isAcceptable()) {
                accept(selector, key);
            }

            keyIterator.remove();
        }
    }

    private void executeClientCommand(final SocketChannel client) throws InterruptedException, NotFoundException, IOException {
        try {
            final String command = getCommand(client).trim();
            final List<String> tokens = Arrays.asList(command.split(DELIMITER));

            final Command currentCommand = commandFactory.getCommand(tokens.get(0));
            final List<String> arguments = Stream.of(command.split(DELIMITER))
                    .skip(1)
                    .collect(Collectors.toList());

            final String message = currentCommand.executeCommand(arguments);
            sendMessage(message, client);
        } catch (FoodServiceException | FoodException | CommandException e) {
            sendMessage(e.getMessage(), client);
        }
    }

    private String getCommand(final SocketChannel client) throws IOException {
        buffer.clear();
        client.read(buffer);
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);

        return new String(bytes);
    }

    private void sendMessage(final String message, final SocketChannel client) {
        final String newMessage = message + System.lineSeparator();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(message.getBytes().length);
        byteBuffer.put(newMessage.getBytes());
        byteBuffer.flip();
        try {
            client.write(byteBuffer);
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException ex) {
                System.err.println("There is a problem with sending a message: " + e.getMessage());
            }
        }
    }

    private void accept(final Selector selector, final SelectionKey key) throws IOException {
        final ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        final SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}