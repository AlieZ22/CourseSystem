package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;
import com.sun.security.ntlm.Client;

import java.util.Scanner;

public class StudentWindow implements StudentClientView{              //学生客户端界面的具体实现类

    private StudentClient sc;


    public StudentWindow(){

    }
 
    @Override
    public void setClient(BasicClient client) {
        this.sc=(StudentClient)client;
    }

    @Override
    public long getStuId() {
        Scanner in  = new Scanner(System.in);
        System.out.print("请输入学号：");
        long id;
        while (true){
            try{
                id=in.nextInt();
                if (id > 0)
                    break;
                else
                    System.out.print("输入不合法请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        return id;
    }

    @Override
    public long getCourseId() {
        Scanner in  = new Scanner(System.in);
        System.out.print("请输入课程号：");
        long courseId=0;
        while (true){
            try{
                courseId=in.nextInt();
                if (courseId>0)
                    break;
                else
                    System.out.print("输入不合法请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        return courseId;
    }



    @Override
    public int chooseStuWork() {
        System.out.println("输入2——进行选课");
        System.out.println("输入3——进行删课");
        System.out.println("输入4——查询所有课程信息");
        System.out.println("输入5——查询学生的已选课程信息");
        System.out.println("其他整数输入——退出选课系统");
        System.out.print("请输入你的操作：");

        Scanner in =new Scanner(System.in);
        int command=0;

        //输入检测
        while (true){
            try{
                command=in.nextInt();
                break;
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }

        return command;
    }

    @Override
    public void showScreen() {
        System.out.println("---------------欢迎进入选课系统---------------");
        System.out.println();
    }

    @Override
    public void showStudentInfo() {
        System.out.println();
        System.out.println(sc.getStudent());

    }

    @Override
    public void showCourseInfo() {
        Course[]courses =sc.getCourses();
        for (int i=0;i<courses.length;i++){
            System.out.println(courses[i]);
        }
    }

    @Override
    public void showCourseOfStu() {
        Course [] courses =sc.getCourseOfStu();
        SelectInfo[] selectInfos = sc.getSelectInfos();
        Boolean flag=true;
        for (int i=0;i<courses.length;i++){
            if (courses[i] != null){
                System.out.println(courses[i].toString(selectInfos[i]));
                flag=false;
            }
        }
        if (flag){
            System.out.println("该学生尚未选课！");
        }
    }
}
