package network.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class ServerProperty {

    public static int MANAGER_PORT;                     //管理员服务端口号
    public static int STUDENT_PORT;                     //学生服务端口号
    public static int THREAD_POOL_SIZE;                   //线程池的大小
    public static int CACHE_SIZE;                       //缓存表的大小

    static {
        try{
            Properties p = new Properties();
            p.load(new InputStreamReader(new FileInputStream("ServerConfig\\ServerProperties.properties")));
            MANAGER_PORT=Integer.valueOf(p.getProperty("MANAGER_PORT"));
            STUDENT_PORT=Integer.valueOf(p.getProperty("STUDENT_PORT"));
            THREAD_POOL_SIZE = Integer.valueOf(p.getProperty("THREAD_POOL_SIZE"));
            CACHE_SIZE = Integer.valueOf(p.getProperty("CACHE_SIZE"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Properties p = new Properties();
        p.setProperty("MANAGER_PORT", "8821");
        p.setProperty("STUDENT_PORT", "8820");
        p.setProperty("THREAD_POOL_SIZE", "200");
        p.setProperty("CACHE_SIZE", "50");
//        System.out.println(p.getProperty("THREAD_POOL_SIZE"));
        p.store(new OutputStreamWriter(new FileOutputStream("ServerConfig\\ServerProperties.properties")),
                "# Server Configuration");
    }


}
