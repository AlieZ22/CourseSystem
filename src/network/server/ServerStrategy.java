package network.server;

import java.net.Socket;

public interface ServerStrategy {
    void service(Socket socket);            //对传入的socket对象进行服务
}
