package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;

import java.util.Scanner;

public class ManagerWindow implements ManagerClientView {



    private ManagerClient mc;

    @Override
    public void showScreen() {
        System.out.println("---------------欢迎进入选课系统---------------");
        System.out.println();
    }

    @Override
    public void setClient(BasicClient client) {
        mc = (ManagerClient) client;
    }

    /**
	 * 选项提示
	 * @return 
	 */
    @Override
    public int chooseManaWork() {
        System.out.println("输入4——查询所有课程信息");
        System.out.println("输入10——执行添加课程");
        System.out.println("输入11——执行删除课程");
        System.out.println("输入12——执行修改课程");
        System.out.println("输入13——录入学生信息");
        System.out.println("输入14——删除学生信息");
        System.out.println("输入15——修改学生信息");
        System.out.println("输入16——查询学生信息");
        System.out.println("输入999——维护服务器/关闭服务器");
        System.out.println("其他输入——退出选课系统");
        System.out.print("请输入你的操作：");
        Scanner in = new Scanner(System.in);
        int command=0 ;

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

    /**
	 * 显示课程信息
	 * @param courses 
	 */
    @Override
    public void showCourses(Course[] courses) {
        for (int i=0;i<courses.length;i++){
            System.out.println(courses[i]);
        }
    }

    /**
	 * 显示学生信息
	 * @param student,courses,selectInfos
	 */
    @Override
    public void showStudentInfo(Student student,Course[] courses, SelectInfo[] selectInfos) {
        if (student == null){
            System.out.println("没有该学生记录！");
        }
        else {
            System.out.println("该学生的相关信息：");
            System.out.println(student);
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

    
    /**
	 * 获取新课程
	 * @return 
	 */
    @Override
    public Object getNewCourse() {
        //用户输入课程信息
        Scanner in =new Scanner(System.in);
        System.out.print("输入课程号：");
        long courseId=0;
        while (true){
            try{
                courseId=in.nextLong();
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
        System.out.print("输入课程名：");
        String name = in.next();
        while (name.getBytes().length>32){
            System.out.print("输入不合法，请重新输入：");
            name = in.next();
        }
        System.out.print("输入课程容量：");
        int totalCapacity =0;
        while (true){
            try{
                totalCapacity=in.nextInt();
                if (totalCapacity>0)
                    break;
                else
                    System.out.print("输入不合法请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }

        return new Course(courseId,name,totalCapacity);
    }
    
    
    /**
	 * 读取课程信息
	 * @return 
	 */
    @Override
    public long getCourseId() {
        Scanner in =new Scanner(System.in);
        System.out.print("输入课程号：");
        long courseId =0;
        while (true){
            try{
                courseId=in.nextInt();
                if(courseId > 0)
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

    /**
	 * 获取学生ID
	 * @return 
	 */
    @Override
    public long getStudentId() {
        Scanner in =new Scanner(System.in);
        System.out.print("输入要查询的学号：");
        long sId =0;
        while(true){
            try{
                sId=in.nextInt();
                if (sId>0){
                    break;
                }
                else{
                    System.out.print("输入不合法请重新输入：");
                }

            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        return sId;
    }

    /**
	 * 修改课程信息
	 * @return 
	 */
    @Override
    public Object chanegCourse() {
        Scanner in =new Scanner(System.in);
        System.out.print("输入要修改的课程号：");
        long courseId =0;
        while (true){
            try{
                courseId=in.nextInt();
                if (courseId > 0)
                    break;
                else
                    System.out.print("输入不合法，请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        System.out.print("输入修改后的课程名：");
        String name = in.next();
        while (name.getBytes().length>32){
            System.out.print("输入不合法，请重新输入：");
            name = in.next();
        }
        System.out.print("输入修改后的课程容量：");
        int totalCapacity=0;
        while (true){
            try{
                totalCapacity=in.nextInt();
                if (totalCapacity > 0)
                    break;
                else
                    System.out.print("输入不合法，请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }

        return new Course(courseId,name,totalCapacity);
    }

    /**
	 * 录入新学生
	 * @return 
	 */
    @Override
    public Object getNewStudent() {
        Scanner in = new Scanner(System.in);
        System.out.print("请输入学号：");
        long sId =0;
        while (true){
            try{
                sId =in.nextInt();
                if (sId > 0)
                    break;
                else
                    System.out.print("输入不合法请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        System.out.print("请输入学生姓名：");
        String name = in.next();
        while (name.getBytes().length >32) {
            System.out.print("输入不合法，请重新输入：");
            name = in.next();
        }
        int classId = 0;
        System.out.print("请输入学生的班级：");
        while (true){
            try{
                classId=in.nextInt();
                break;
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }
        int gender =0;
        System.out.print("请输入学生性别（0代表男，1代表女）：");
        while (true){
            try{
                gender=in.nextInt();
                if (gender==0 || gender== 1)
                    break;
                else
                    System.out.print("输入不合法请重新输入：");
            }
            catch (Exception e){
                System.out.print("输入有误请重新输入：");
                in = new Scanner(System.in);
            }
        }

        return new Student(sId,name,classId,gender==0);
    }
}
