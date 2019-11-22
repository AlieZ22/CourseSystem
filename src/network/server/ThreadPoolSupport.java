package network.server;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolSupport implements ServerStrategy {


    public ExecutorService es = Executors.newFixedThreadPool(ServerProperty.THREAD_POOL_SIZE);
    ServerStrategy ss;
    public ThreadPoolSupport(ServerStrategy ss){
        this.ss=ss;
    }

    //重新构建线程池
    public void restart(){
        es=Executors.newFixedThreadPool(ServerProperty.THREAD_POOL_SIZE);
//        System.out.println(es.isTerminated());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        es.shutdownNow();

    }

    @Override
    public void service(Socket socket) {
        es.submit(new ServeThread(socket,ss));
    }
}
