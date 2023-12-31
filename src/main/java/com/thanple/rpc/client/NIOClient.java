package com.thanple.rpc.client;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {

    private String remoteIp;
    private int remotePort;

    public NIOClient(String remoteIp, int remotePort) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    public void connect() throws IOException {
        // 创建客户端SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        // 创建选择器
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isConnectable()) {
                    handleConnect(socketChannel, key);
                }
                if (key.isWritable()) {
                    handleWrite(socketChannel,selector);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }

                iter.remove();
            }
        }

    }


    private static void handleConnect(SocketChannel socketChannel, SelectionKey key) throws IOException {
        if (socketChannel.finishConnect()) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private static void handleWrite(SocketChannel socketChannel, Selector selector) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 写入要发送的数据
        buffer.put("Hello from Client".getBytes());
        buffer.flip();
        socketChannel.write(buffer);

        // 修改当前key的兴趣操作为读操作
        socketChannel.keyFor(selector).interestOps(SelectionKey.OP_READ);
    }



    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead > 0) {
            // 处理读到的数据
            buffer.flip();
            byte[] data = new byte[numRead];
            buffer.get(data);

            String receivedString = new String(data, "UTF-8");
            System.out.println("Message from server: " + receivedString);
        }
    }
}
