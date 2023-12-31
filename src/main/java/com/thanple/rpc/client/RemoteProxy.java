package com.thanple.rpc.client;

import com.thanple.rpc.message.Request;
import com.thanple.rpc.util.SerializeUtil;

import java.lang.reflect.Proxy;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class RemoteProxy {

    public static <T> T create(SocketChannel channel, Class<T> serviceStub) {


        Object proxyInstance = Proxy.newProxyInstance(
                serviceStub.getClassLoader(),
                new Class<?>[]{serviceStub},
                (proxy, method, args)->{
                    Request request = new Request();
                    request.setUuid(UUID.randomUUID().toString());
                    request.setClassName(className);
                    request.setMethodName(method.getName());
                    request.setParamTypes(method.getParameterTypes());
                    request.setParams(args);

                    Future<?> future = MessageReceived.getInstance().initFuture(request.getUuid());

                    RpcClient.sendMessage(channel, SerializeUtil.serializeObj(request));
                    Object o = future.get(3, TimeUnit.SECONDS);

                    return o;
                }
        );

        return (T)proxyInstance;
    }

}
