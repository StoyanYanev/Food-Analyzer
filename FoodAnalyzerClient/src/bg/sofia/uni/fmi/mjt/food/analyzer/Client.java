package bg.sofia.uni.fmi.mjt.food.analyzer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final String ENCODING = "UTF-8";
    private static final String DISCONNECT_COMMAND = "disconnect";

    public void start() {
        try (final SocketChannel socketChannel = SocketChannel.open();
                final PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, ENCODING), true);
                final Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            final Thread client = new Thread(new ServerResponseHandler(socketChannel));
            client.start();

            System.out.print(">");
            while (true) {
                final String message = scanner.nextLine();
                if (message.equals(DISCONNECT_COMMAND)) {
                    client.join();
                    break;
                }
                writer.println(message);
                System.out.print(">");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}