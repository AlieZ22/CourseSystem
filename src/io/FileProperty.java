package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class FileProperty {
    public static void main(String[] args) throws  Exception {
        Properties p = new Properties();
//        p.load(new InputStreamReader(new FileInputStream("FileConfig/FileProperties.properties")));
        p.setProperty("MAX_COURSE_NUM", "4");
        p.setProperty("MAX_ADDRESS_NUM", "6");
        p.setProperty("EACH_PIECE_STUDENTINFO_SIZE","97");
        p.setProperty("COURSE_FILE", "course.txt");
        p.setProperty("COURSE_FILE_MODE", "rw");
        p.setProperty("SELECTINFO_FILE","selectInfo.txt");
        p.setProperty("SELECTINFO_FILE_MODE","rw");
        p.setProperty("STUDENT_FILE", "student.txt");
        p.setProperty("STUDNET_FILE_MODE","rw");
        p.store(new OutputStreamWriter(new FileOutputStream("FileConfig/FileProperties.properties")),
                "# File Configuration");
    }


    public static Properties fileConfig ;
    public static String courseFile;
    public static String courseFileMode;
    public static String selectInfoFile;
    public static String selectInfoFileMode;
    public static String studentFile;
    public static String studentFIleMode;
    public static int MAX_COURSE_NUM;                   //学生可以选课的最大课程数
    public static int MAX_ADDRESS_NUM;				    //每个学生的最大选课地址个数
    public static int EACH_PIECE_STUDENTINFO_SIZE;      //每条记录的长度

    //加载文件的配置信息
    static {
        try{
            fileConfig = new Properties();
            fileConfig.load(new InputStreamReader(new FileInputStream("FileConfig/FileProperties.properties")));
            courseFile = fileConfig.getProperty("COURSE_FILE");
            courseFileMode = fileConfig.getProperty("COURSE_FILE_MODE");
            selectInfoFile = fileConfig.getProperty("SELECTINFO_FILE");
            selectInfoFileMode=fileConfig.getProperty("SELECTINFO_FILE_MODE");
            studentFile = fileConfig.getProperty("STUDENT_FILE");
            studentFIleMode = fileConfig.getProperty("STUDNET_FILE_MODE");
            MAX_COURSE_NUM=Integer.valueOf(fileConfig.getProperty("MAX_COURSE_NUM"));
            MAX_ADDRESS_NUM = Integer.valueOf(fileConfig.getProperty("MAX_ADDRESS_NUM"));
            EACH_PIECE_STUDENTINFO_SIZE = Integer.valueOf(fileConfig.getProperty("EACH_PIECE_STUDENTINFO_SIZE"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static int[] lock = new int[0];

    public static void setMaxCourseNum(int num) throws Exception{
        synchronized (lock){                //互斥方法
            MAX_COURSE_NUM=num;
            fileConfig.setProperty("MAX_COURSE_NUM", String.valueOf(num));
            fileConfig.store(new OutputStreamWriter(new FileOutputStream("FileConfig/FileProperties.properties")),
                    "# File Configuration");
        }

    }

}
