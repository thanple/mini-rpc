package com.thanple.rpc.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */

public class RpcServer {

    private volatile boolean isOpen;

    public static void main(String[] args) throws IOException {
        new RpcServer().start(8000);
    }


    public void start(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        isOpen = true;

        while (isOpen) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    readData(key);
                }
                iter.remove();
            }
        }
    }


    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        SelectionKey key = client.register(selector, SelectionKey.OP_READ);
        key.attach(new Connection());
    }

    private static void readData(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        Connection conn = (Connection) key.attachment();
        ByteBuffer buffer = conn.getBuffer();
        int read = client.read(buffer);

        if (read == -1) {
            client.close();
            return;
        }

        processBuffer(buffer, client);
    }

    private static void processBuffer(ByteBuffer buffer, SocketChannel client) throws IOException {
        buffer.flip(); // 切换到读模式

        while (buffer.remaining() >= 4) {
            buffer.mark(); // 标记当前位置
            int length = buffer.getInt();

            if (buffer.remaining() < length) {
                buffer.reset(); // 重置到标记的位置
                buffer.compact(); // 为下次读取准备
                return;
            }

            byte[] data = new byte[length];
            buffer.get(data, 0, length);
            String message = new String(data);
            System.out.println("Received message: " + message);

            // 发送客户端消息
            ByteBuffer outBuffer = ByteBuffer.wrap(data);
            client.write(outBuffer);
        }

        buffer.compact(); // 为下次读取准备
    }

}
