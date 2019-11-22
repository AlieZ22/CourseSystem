package network.client;

import basis.Course;
import basis.Student;

import javax.sound.midi.SysexMessage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ManagerStrategyImpl implements ManagerStrategy {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ManagerStrategyImpl(Socket socket) throws Exception{
        this.socket=socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /**
	 * 增加课程
	 * @param course 
	 */
    @Override
    public void addCourse(Course course) throws Exception {
        dos.writeInt(10);
        dos.flush();


        dos.writeLong(course.getCourseID());
        dos.writeUTF(course.getName());
        dos.writeInt(course.getTotalCapacity());
        dos.flush();

        if (dis.readBoolean()){
            System.out.println("增加课后成功");
        }
        else
            System.out.println("增加课程失败");
    }

    /**
	 * 删除学生信息
	 * @param cID
	 * @return 
	 */
    @Override
    public void delCourse(long cId) throws Exception{
        dos.writeInt(11);
        dos.flush();

        dos.writeLong(cId);
        dos.flush();

        if (dis.readBoolean()){
            System.out.println("删课成功");
        }
        else
            System.out.println("删课失败");
    }
    
    /**
	 * 修改课程信息
	 * @param stuID
	 * @return 
	 */
    @Override
    public void modifyCourse(Course course) throws Exception {
        dos.writeInt(12);
        dos.flush();

        dos.writeLong(course.getCourseID());
        dos.writeUTF(course.getName());
        dos.writeInt(course.getTotalCapacity());
        dos.flush();

        if (dis.readBoolean()){
            System.out.println("修改成功");
        }
        else
            System.out.println("修改失败");
    }

    /**
	 * 添加学生信息
	 * @param student
	 */
    @Override
    public void addStudent(Student student) throws Exception {
        dos.writeInt(13);
        dos.flush();
        writeStudent(student);

        Boolean flag = dis.readBoolean();
        if (flag){
            System.out.println("添加学生成功！");
        }
        else{
            System.out.println("添加学生失败！");
        }
    }
    
    /**
	 * 删除学生信息
	 * @param sId
	 */
    @Override
    public void delStudent(long sId) throws Exception {
        dos.writeInt(14);
        dos.writeLong(sId);
        dos.flush();

        Boolean flag = dis.readBoolean();
        if (flag){
            System.out.println("删除该学生记录成功！");
        }
        else{
            System.out.println("删除失败！");
        }
    }
    
    /**
	 * 修改学生信息
	 * @param student
	 */
    @Override
    public void modifyStudent(Student student) throws Exception {
        dos.writeInt(15);
        writeStudent(student);
        dos.flush();
        Boolean flag =dis.readBoolean();
        if (flag){
            System.out.println("修改该学生记录成功！");
        }
        else{
            System.out.println("修改失败！");
        }
    }


    /**
	 * 停服
	 */
    @Override
    public void stopServer() throws Exception{
        dos.writeInt(999);
        dos.flush();
        //服务器关闭，同时也将终止客户端
        System.exit(-1);
    }

    /**
	 * 退出管理系统
	 */
    @Override
    public void stopManager() throws Exception {
        dos.writeInt(998);
        dos.flush();
    }

    /**
	 * 写入学生信息
	 * @param student
	 */
    private void writeStudent(Student student)throws Exception{
        dos.writeLong(student.getsID());
        dos.writeInt(student.getClassID());
        dos.writeUTF(student.getName());
        dos.writeBoolean(student.getGender());
        dos.flush();
    }

}
