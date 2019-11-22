package network.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class ClientProperty {

    public static int MANAGER_PORT;                     //管理员服务端口号
    public static int STUDENT_PORT;                     //学生服务端口号
    public static String HOST;                          //服务器主机的IP地址

    static {
        try{
            Properties p = new Properties();
            p.load(new InputStreamReader(new FileInputStream("ClientConfig\\ClientProperties.properties")));
            MANAGER_PORT=Integer.valueOf(p.getProperty("MANAGER_PORT"));
            STUDENT_PORT=Integer.valueOf(p.getProperty("STUDENT_PORT"));
            HOST = p.getProperty("HOST");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Properties p = new Properties();
        p.setProperty("MANAGER_PORT", "8821");
        p.setProperty("STUDENT_PORT", "8820");
        p.setProperty("HOST", "127.0.0.1");
        p.store(new OutputStreamWriter(new FileOutputStream("ClientConfig\\ClientProperties.properties")),
                "# Client Configuration");
    }
}
