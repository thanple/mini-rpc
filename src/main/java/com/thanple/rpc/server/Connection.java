package com.thanple.rpc.server;

import java.nio.ByteBuffer;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class Connection {
    ByteBuffer buffer = ByteBuffer.allocate(256); // 调整大小以适应你的需要


    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }
}
