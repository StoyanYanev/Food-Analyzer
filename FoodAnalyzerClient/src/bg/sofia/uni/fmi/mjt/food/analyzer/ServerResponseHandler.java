package bg.sofia.uni.fmi.mjt.food.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class ServerResponseHandler implements Runnable {
    private static final String ENCODING = "UTF-8";
    private SocketChannel socketChannel;

    public ServerResponseHandler(final SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try (final BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, ENCODING))) {
            while (true) {
                final String reply = reader.readLine();
                if (reply == null) {
                    break;
                }
                System.out.println(reply);
                System.out.print(">");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}