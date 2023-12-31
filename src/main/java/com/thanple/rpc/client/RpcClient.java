package com.thanple.rpc.client;

import com.thanple.rpc.message.Response;
import com.thanple.rpc.util.SerializeUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class RpcClient {



    private String remoteIp;
    private int remotePort;
    private volatile boolean isOpen;

    public RpcClient(String remoteIp, int remotePort) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    /**
     * 发送消息
     * @param client
     * @param data
     * @throws IOException
     */
    public static void sendMessage(SocketChannel client, byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        client.write(buffer);
    }

    public SocketChannel connect() throws Exception {


        // 连接到服务器
        SocketChannel client = SocketChannel.open(new InetSocketAddress(remoteIp,remotePort));
        client.configureBlocking(false);

        // 创建Selector
        Selector selector = Selector.open();
        client.register(selector, SelectionKey.OP_CONNECT);

        isOpen = true;

        new Thread(){
            @Override
            public void run() {
                while (isOpen) {
                    try {
                        selector.select();
                        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                        while (keys.hasNext()) {
                            SelectionKey key = keys.next();
                            keys.remove();

                            if (key.isConnectable()) {
                                handleConnect(key);
                            }

                            if (key.isReadable()) {
                                handleRead(key);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }.start();



//        String message = "Hello from the client!";
//        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
//        client.write(buffer);
//        buffer.clear();



        return client;
    }


    private static void handleConnect(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();

        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }

        channel.configureBlocking(false);
        channel.register(key.selector(), SelectionKey.OP_READ);

        // 发送数据到服务器
//        String message = "Hello, Server!";
//        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
//        channel.write(buffer);
    }

    private static void handleRead(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);

        if (read > 0) {
            Response response = SerializeUtil.unSerialize(buffer.array(), Response.class);
            System.out.println("Message from server: " + response.getUuid());
            MessageReceived.getInstance().receiveData(response);
        } else if (read < 0) {
            channel.close();
        }
    }
}
