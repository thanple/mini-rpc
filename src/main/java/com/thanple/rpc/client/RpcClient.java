package com.thanple.rpc.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class RpcClient {



    public static void sendMessage(SocketChannel client, String message) throws IOException {
        byte[] data = message.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        client.write(buffer);
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8000));
        client.configureBlocking(false);

        String message = "Hello from the client!";
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        client.write(buffer);
        buffer.clear();

        Thread.sleep(5000);
        client.read(buffer);
        String response = new String(buffer.array()).trim();
        System.out.println("Response from server: " + response);
        client.close();
    }
}
