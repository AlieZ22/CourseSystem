package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;

import java.net.Socket;


public class StudentClient extends BasicClient  {

    public static void main(String[] args) {
        BasicClient sc =new StudentClient();
        ClientView cv= new StudentWindow();
        sc.setView(cv);
        cv.setClient(sc);
        sc.start();

    }


    private Socket socket;
    private StudentStrategy ss ;
    private Student student;            //对应的学生对象
    private Course[] courses;           //全部课程信息
    private Course[] courseOfStu;       //学生的已选课程信息
    private SelectInfo[] selectInfos;   //学生的选课记录
    private StudentClientView scv;      //该客户端对应的操作界面

    public StudentClient() {
        super();
        try{
            socket =new Socket(ClientProperty.HOST,ClientProperty.STUDENT_PORT);
            ss = new StudentStrategyImpl(socket);
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }
    @Override
    public void setView(ClientView view){
        this.scv=(StudentClientView)view;

    }

    public Student getStudent(){
        return this.student;
    }
    public  Course[] getCourses(){
        return courses;
    }
    public Course[] getCourseOfStu(){
        return courseOfStu;
    }
    public SelectInfo[] getSelectInfos(){
        return selectInfos;
    }

    @Override
    public void login() throws Exception {                  //后期可以想想教务和学生的排斥进入系统
        System.out.println("欢迎进入学生选课系统...");
        long id =scv.getStuId();
        student=(Student) ss.queryStudent(id);            //登录不成功，抛出异常。。。
        if (student == null){
            System.out.println("没有该学生的记录！");
            System.exit(-1);
        }
    }

    @Override
    public void exit() throws Exception{
        System.out.println("退出选课系统...");
        socket.close();
    }


    /**
	 * 启动
	 */
    @Override
    public void start() {
        Boolean flag=true;
        int command;
        try {
            this.login();
            scv.showScreen();
            while(flag){
                scv.showStudentInfo();
                command=scv.chooseStuWork();
                switch (command){
                    case 2:
                        long courseId = scv.getCourseId();
                        ss.addRequest(student.getsID(),courseId);
                        break;
                    case 3:
                        courseId = scv.getCourseId();
                        ss.delRequest(student.getsID(),courseId);
                        break;
                    case 4:
                        courses = (Course[]) ss.queryRequest();
                        scv.showCourseInfo();
                        break;
                    case 5:
                        courseOfStu = (Course[]) ss.queryRequest(student.getsID());
                        selectInfos = (SelectInfo[]) ss.queryInfoRequest(student.getsID());
                        scv.showCourseOfStu();
                        break;
                    default:
                        flag=false;
                }
            }
            this.exit();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }

}
