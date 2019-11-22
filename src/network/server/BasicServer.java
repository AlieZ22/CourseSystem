package network.server;

abstract public class BasicServer {
    abstract public void release();             //释放工作资源
    abstract public void start();               //开始运行服务端
}
