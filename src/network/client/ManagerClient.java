package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;

import java.net.Socket;

public class ManagerClient extends BasicClient{

    private Socket socket;
    private ManagerStrategy ms;
    private ManagerClientView mcv;


    public static void main(String[] args) {
        BasicClient bc = new ManagerClient();
        ClientView cv = new ManagerWindow();
        bc.setView(cv);
        cv.setClient(bc);
        bc.start();
    }

    /**
	 * 设置窗口
	 * @param view 
	 */
    @Override
    public void setView(ClientView view) {
        mcv = (ManagerClientView) view;
    }

  
    public ManagerClient(){
        super();
        try{
            socket =new Socket(ClientProperty.HOST,ClientProperty.MANAGER_PORT);
            ms = new ManagerStrategyImpl(socket);
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    /**
  	 * 登录 
  	 */
    @Override
    public void login() {
        System.out.println("欢迎进入教务系统...");

    }
    
    
    /**
  	 * 退出
  	 */
    @Override
    public void exit() throws Exception{
        System.out.println("退出教务系统...");
        socket.close();
    }


    /**
  	 * 启动
  	 */
    @Override
    public void start() {
        login();
        Boolean flag=true;
        int command=0;
        mcv.showScreen();
        try{
            while (flag){
                command=mcv.chooseManaWork();
                switch (command){
                    case 4:
                        Course[] courses =(Course[]) new StudentStrategyImpl(socket).queryRequest();
                        mcv.showCourses(courses);
                        break;
                    case 10:
                        Course course=(Course) mcv.getNewCourse();
                        ms.addCourse(course);
                        break;
                    case 11:
                        ms.delCourse(mcv.getCourseId());
                        break;
                    case 12:
                        ms.modifyCourse((Course) mcv.chanegCourse());
                        break;
                    case 13:
                        Student student =(Student)mcv.getNewStudent();
                        ms.addStudent(student);
                        break;
                    case 14:
                        ms.delStudent(mcv.getStudentId());
                        break;
                    case 15:
                        student = (Student)mcv.getNewStudent();
                        ms.modifyStudent(student);
                        break;
                    case 16:
                        //用学生的相关请求操作完成对学生信息的查询

                        StudentStrategy ss = new StudentStrategyImpl(socket);
                        long sId = mcv.getStudentId();
                        student = (Student) ss.queryStudent(sId);
                        courses = (Course[]) ss.queryRequest(sId);
                        SelectInfo[] selectInfos = (SelectInfo[])ss.queryInfoRequest(sId);
                        mcv.showStudentInfo(student,courses,selectInfos);
                        break;
                    case 999:
                        ms.stopServer();
                        break;
                    default:
                        flag=false;
                }
            }
            ms.stopManager();
            exit();
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
