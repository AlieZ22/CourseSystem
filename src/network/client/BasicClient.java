package network.client;

abstract public class BasicClient {
    public BasicClient(){

    }
    abstract public void login() throws Exception;           //登录
    abstract public void exit() throws Exception;            //退出
    abstract public void start();                            //开始执行工作
    abstract public void setView(ClientView view);           //设置视图
}
