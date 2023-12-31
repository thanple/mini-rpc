import com.thanple.rpc.client.RemoteProxy;
import com.thanple.rpc.client.RpcClient;
import com.thanple.rpc.server.RpcServer;

import java.io.IOException;
import java.nio.channels.SocketChannel;
//import org.junit.Test;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */


public class RPCTest {


//    @Test
    public void server() throws IOException {
        RpcServer rpcServer = new RpcServer(8000);
        rpcServer.start();
    }

//    @Test
    public void client() throws Exception {
        RpcClient rpcClient = new RpcClient("localhost",8000);
        SocketChannel connect = rpcClient.connect();
        IHelloService helloService = RemoteProxy.create(connect, IHelloService.class);

        String helloResult = helloService.hello("Tom");
        System.out.println(helloResult);
    }

}
