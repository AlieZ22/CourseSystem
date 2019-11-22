package network.server;

import java.net.Socket;

public class ServeThread extends Thread {           //扩展线程类
    private Socket socket;                          //要服务的socket对象
    private ServerStrategy ss;                      //实现具体服务的对象

    public ServeThread(Socket socket,ServerStrategy ss){
        this.socket=socket;
        this.ss=ss;
    }

    @Override
    public void run() {
        ss.service(socket);
    }
}
