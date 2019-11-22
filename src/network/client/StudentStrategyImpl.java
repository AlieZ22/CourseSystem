package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class StudentStrategyImpl implements StudentStrategy {          //StudentStrategy 接口的实现类

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public StudentStrategyImpl(Socket socket) throws Exception{
        this.socket=socket;
        dis=new DataInputStream(socket.getInputStream());
        dos= new DataOutputStream(socket.getOutputStream());
    }

    /**
	 * 查询学生信息
	 * @param sId
	 * @return 
	 */
    @Override
    public Object queryStudent(long sId) throws Exception{
        dos.writeInt(1);            //执行查询学生信息
        dos.writeLong(sId);
        dos.flush();

        Boolean flag = dis.readBoolean();

        if (flag){
            return readStudent();
        }
        else
            return null;

    }
    
    /**
	 * 学生选课
	 * @param sId，courseId
	 */
    @Override
    public void addRequest(long sId, long courseId)throws Exception {
        dos.writeInt(2);
        dos.writeLong(sId);
        dos.writeLong(courseId);
        dos.flush();

        Boolean flag = dis.readBoolean();
        if (flag){
            System.out.println("学号："+sId+"课程号："+courseId+"   选课成功！");
        }
        else
            System.out.println("学号："+sId+"课程号："+courseId+"选课失败！");

    }

    /**
	 * 学生退课
	 * @param sId,courseId
	 */
    @Override
    public void delRequest(long sId, long courseId)throws Exception {
        dos.writeInt(3);
        dos.writeLong(sId);
        dos.writeLong(courseId);
        dos.flush();
        Boolean flag= dis.readBoolean();

        if (flag){
            System.out.println("删课成功！");
        }
        else
            System.out.println("删课失败！");

    }

    
    /**
	 * 查询全部课程信息
	 * @return 
	 */
    @Override
    public Object queryRequest() throws Exception{
        dos.writeInt(4);
        dos.flush();

        int courseNum = dis.readInt();      //课程数目
        Course[] courses =new Course[courseNum];
        for (int i=0;i<courseNum;i++){
            courses[i] = readCourse();
        }

        return courses;

    }

    /**
	 * 查询已选课程信息
	 * @param sId
	 * @return 
	 */
    @Override
    public Object queryRequest(long sId) throws Exception{
        dos.writeInt(5);
        dos.writeLong(sId);
        dos.flush();
        int courseNum = dis.readInt();
        Course []courses =new Course[courseNum];
        for (int i=0;i<courseNum;i++){
            courses[i]=readCourse();
        }
        return courses;
    }

    /**
	 * 查询选课信息
	 * @param sId
	 * @return 
	 */
    @Override
    public Object queryInfoRequest(long sId) throws Exception {
        dos.writeInt(6);
        dos.writeLong(sId);
        dos.flush();
        int num = dis.readInt();
        SelectInfo[] selectInfos = new SelectInfo[num];
        for (int i=0;i<num;i++){
                selectInfos[i]=readSelectInfo();
        }
        return selectInfos;
    }

    private Student readStudent() throws  Exception{
        long sId= dis.readLong();
        int classId = dis.readInt();
        String name = dis.readUTF();
        Boolean gender = dis.readBoolean();

        return new Student(sId,name,classId,gender);

    }
    private Course readCourse() throws Exception{

        long courseId= dis.readLong();
        String name = dis.readUTF();
        int totalCapacity = dis.readInt();
        int leftCapacity = dis.readInt();
        int members = dis.readInt();
        return new Course(courseId,name,totalCapacity,leftCapacity,members);

    }
    private SelectInfo readSelectInfo() throws Exception{
        long sId = dis.readLong();
        long cId = dis.readLong();
        String date = dis.readUTF();
        return new SelectInfo(sId,cId,date);

    }
}
