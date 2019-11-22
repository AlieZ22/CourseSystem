package network.server;

import io.StudentCache;


import java.net.ServerSocket;
import java.net.Socket;

public class CourseServer extends BasicServer{

    public static void main(String[] args) {
        cache = new StudentCache(ServerProperty.CACHE_SIZE);
        BasicServer bs = new CourseServer();
        bs.start();


    }


    public static StudentCache cache ;
    public static ThreadPoolSupport tps;                          //提供线程服务的接口
    public static ServerSocket studentServer;
    public static ServerSocket managerServer;
    public CourseServer(){
        try{
            tps=new ThreadPoolSupport(new ServerStrategyImpl());
            studentServer=new ServerSocket(ServerProperty.STUDENT_PORT);
            managerServer = new ServerSocket(ServerProperty.MANAGER_PORT);
            this.release();                                 //当程序结束时，执行收尾工作
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void start(){
        startStuService();
        startManService();

    }

    /**
	 * 开启学生服务 
	 */
    public static void startStuService(){
        System.out.println("开启学生服务...");
        new Thread(()->{
            try{
                while (true){
                    Socket socket =studentServer.accept();
                    tps.service(socket);
                }
            }
            catch (Exception e){
            }
            System.out.println("学生服务结束");
        }).start();
    }
    
    /**
	 * 开启管理员服务
	 */
    public static void startManService(){
        System.out.println("开启管理员服务...");
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    while (true){
                        Socket socket = managerServer.accept();             //互斥锁？
                        tps.es.shutdown();
                        while (!tps.es.isTerminated());         //处理完线程池中的所有进程
                        studentServer.close();                  //监听端口关闭后 已经建立的流socket还存在吗
                        tps.restart();                          //重启线程池
                        tps.service(socket);                    //学生端处理完后 在管理员进行服务
                    }
                }
                catch (Exception e){
                }
                System.out.println("教务服务结束");
            }
        }).start();

    }

    
    /**
	 * 重启服务器  重新加载缓存表
	 */
    public static void restart() throws Exception{

        studentServer = new ServerSocket(ServerProperty.STUDENT_PORT);
        cache.close();
        cache=new StudentCache(24);
        startStuService();
    }

   
    /**
	 * 终止服务器
	 */
    public static void stop() throws Exception{

        studentServer.close();
        managerServer.close();
        cache.close();
        System.exit(0);
    }



    /**
     * 终止服务器释放有关资源
     */
    @Override
    public void release() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try{
                    studentServer.close();
                    managerServer.close();
                    cache.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
