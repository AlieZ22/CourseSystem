package network;

import basis.Course;
import basis.SelectInfo;
import com.sun.security.ntlm.Client;
import io.FileProperty;
import network.client.*;

import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Test {
//    static {
//        System.out.println("???");
//    }
//    static int ind=a();
//
//    static public int a(){
//        System.out.println("....");
//        return 1;
//    }
    public static void main(String[] args) throws  Exception{

        for (int i=1;i<=200;i++){
            Thread t = new TestThread6(i);
            t.start();
        }



    }
}


//测试①   第一门课程剩余容量为100,200名同学去选这一门课

class TestThread1 extends Thread{

    int i;
    public TestThread1(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();
//                        long id = random.nextInt(100000)+1;
            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                    ClientProperty.STUDENT_PORT));
//                        System.out.println(id+" ***** ");
//            System.out.println(i+" -----*****");

            ssl.addRequest(i,1);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

//测试②   在测试①之后，200 名 同学对第一门课都进行删课
class TestThread2 extends Thread{

    int i;
    public TestThread2(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();
//                        long id = random.nextInt(100000)+1;
            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                    ClientProperty.STUDENT_PORT));
//                        System.out.println(id+" ***** ");
//            System.out.println(i+" -----*****");
            ssl.delRequest(i,1);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

//测试③   前100名 同学选第一门课 100,100-150选第2门课 容量20,150-200名学生选第3门课
class TestThread3 extends Thread{

    int i;
    public TestThread3(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();
//                        long id = random.nextInt(100000)+1;
            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                   ClientProperty.STUDENT_PORT));
//                        System.out.println(id+" ***** ");
//            System.out.println(i+" -----*****");

            long courseId;
            if (i<=100)
                courseId=1;
            else if(i<=150)
                courseId=2;
            else
                courseId=3;
            ssl.addRequest(i,courseId);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

//测试④   逆操作 退课
class TestThread4 extends Thread{

    int i;
    public TestThread4(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();
//                        long id = random.nextInt(100000)+1;
            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                    ClientProperty.STUDENT_PORT));
//                        System.out.println(id+" ***** ");
//            System.out.println(i+" -----*****");

            long courseId;
            if (i<=100)
                courseId=1;
            else if(i<=150)
                courseId=2;
            else
                courseId=3;
            ssl.delRequest(i,courseId);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}


//测试⑤   200名学生 学号随机，随机选课或者删课
class TestThread5 extends Thread{

    int i;
    public TestThread5(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();
//                        long id = random.nextInt(100000)+1;
            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                    ClientProperty.STUDENT_PORT));
//                        System.out.println(id+" ***** ");
//            System.out.println(i+" -----*****");

            long sId;
            long courseId;
            int command;
            sId = (long)random.nextInt(100000)+1;
            command = random.nextInt(2)+2;
            courseId = (long)random.nextInt(FileProperty.MAX_COURSE_NUM)+1;
            System.out.println("sId : "+sId + "   comand:"+ command);
            switch (command){
                case 2:
                    ssl.addRequest(sId,courseId);
                    break;
                case 3:
                    ssl.delRequest(sId,courseId);
                    break;
                    default:
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}


//测试6 200名随机学号的学生，随机4种请求操作
class TestThread6 extends Thread{

    int i;
    public TestThread6(int i){
        this.i=i;
    }

    @Override
    public void run() {
        try{
            Random random =new Random();

            StudentStrategyImpl ssl =new StudentStrategyImpl(new Socket(ClientProperty.HOST,
                    ClientProperty.STUDENT_PORT));

            long sId;
            long courseId;
            int command;
            Course[] courses;
            Course[] coursesOfStu;
            sId = (long)random.nextInt(100000)+1;
            command = random.nextInt(4)+2;
            courseId = (long)random.nextInt(FileProperty.MAX_COURSE_NUM)+1;
            System.out.println("sId : "+sId + "   comand:"+ command);
            switch (command){
                case 2:
                    ssl.addRequest(sId,courseId);
                    break;
                case 3:
                    ssl.delRequest(sId,courseId);
                    break;
                case 4:
                    courses = (Course[]) ssl.queryRequest();
                    for (int i=0;i<courses.length;i++){
                        System.out.println(courses[i]);
                    }
                    break;
                case 5:
                    coursesOfStu = (Course[]) ssl.queryRequest(sId);
                    SelectInfo[] selectInfos = (SelectInfo[]) ssl.queryInfoRequest(sId);
                    Boolean flag =true;
                    for (int i=0;i<coursesOfStu.length;i++){
                        if (coursesOfStu[i] != null){
                            System.out.println(coursesOfStu[i].toString(selectInfos[i]));
                            flag = false;
                        }
                    }
                    if (flag){
                        System.out.println("该学生尚未选课！");
                    }
                    break;
                default:
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}