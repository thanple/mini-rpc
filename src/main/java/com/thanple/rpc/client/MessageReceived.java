package com.thanple.rpc.client;

import com.thanple.rpc.message.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class MessageReceived {
    public Map<String, CompletableFuture<Object>> futureMap = new ConcurrentHashMap<>();

    private static MessageReceived instance = new MessageReceived();
    public static MessageReceived getInstance() {
        return instance;
    }


    public Future<?> initFuture(String uuid){
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        futureMap.put(uuid,completableFuture);
        return completableFuture;
    }


    public void receiveData(Response response){
        CompletableFuture<Object> future = futureMap.remove(response.getUuid());
        if(null != future) {
            future.complete(response);
        }
    }

}
