package network.server;

import basis.Course;
import basis.SelectInfo;
import basis.Student;
import io.FileProperty;
import io.StudentCache;

import static network.server.CourseServer.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerStrategyImpl implements ServerStrategy {               //服务端的 ServerStrategy接口的实现类

//    static public final ThreadLocal<Socket> threadLocal = new ThreadLocal<>();      //每个线程存储自己的socket对象

    static int[] lock = new int[0];

    @Override
    public void service(Socket socket){
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Boolean flag= true;

            //switch 结构 处理相应的逻辑
            while(flag){
                int command = dis.readInt();
                switch (command){
                    case 1:
                        loginResponse(dis,dos);
                        break;
                    case 2:
                        addResponse(dis,dos);
                        break;
                    case 3:
                        delResponse(dis,dos);
                        break;
                    case 4:
                        queryResponse(dis,dos);
                        break;
                    case 5:
                        queryStuResponse(dis,dos);
                        break;
                    case 6:
                        queryInfoResponse(dis,dos);
                        break;
                    case 10:
                        addCourseResponse(dis,dos);
                        break;
                    case 11:
                        delCourseResponse(dis,dos);
                        break;
                    case 12:
                        chanegCourse(dis,dos);
                        break;
                    case 13:
                        addStuResponse(dis,dos);
                        break;
                    case 14:
                        delStuResponse(dis,dos);
                        break;
                    case 15:
                        modifyStuResponse(dis,dos);
                        break;
                    case 998:
                        CourseServer.restart();
                        break;
                    case 999:
                        flag=false;
                        CourseServer.stop();
                        break;
                    default:
                        flag=false;
                }
            }

        }
        catch (Exception e){
            System.out.println("此次服务结束");
        }


    }
    
    /**
	 * 登录学生端
	 * @param dis,dos
	 */
    private void loginResponse(DataInputStream dis,DataOutputStream dos) throws Exception{           //对应学生登录请求
        long sId = dis.readLong();
        Student student = cache.queryStudent(sId);//通过id 获取到对应的学生对象
        if (student == null){
            dos.writeBoolean(false);
            dos.flush();
        }
        else {
            dos.writeBoolean(true);
            dos.flush();
            writeStudent(student,dos);
        }

    }
    
    /**
	 * 加课请求
	 * @param dis,dos
	 */
    private void addResponse(DataInputStream dis,DataOutputStream dos)throws Exception{             //对应学生选课处理
        synchronized (lock){
            long sId=dis.readLong();
            long courseID = dis.readLong();

            // TODO: 先用课余量判断
            Student student = cache.queryStudent(sId);
            Course course = cache.queryCourse(courseID);
            boolean flag= true;

            //还要判断学生是否已选这门课
            if(student != null && course!= null && course.getLeftCapacity()>0 ){
                // courses为该名学生所选的课
                Course[] courses = cache.queryCourseOfStu(sId);
                for(int i=0;i<courses.length;i++){
                    if (courses[i]!=null  && courses[i].getCourseID() == courseID){
                        flag=false;
                        break;
                    }
                }
                if (flag){
                    dos.writeBoolean(true);
                    cache.addCourse(student,course);
                }
                else{
                    dos.writeBoolean(false);
                    System.out.println();
                }

            }
            else{
                dos.writeBoolean(false);
                System.out.println();
            }
            dos.flush();
        }


    }

    /**
	 * 删课请求
	 * @param dis
	 * @param dos  
	 */
    private void delResponse(DataInputStream dis,DataOutputStream dos)throws Exception{             //对应学生删课处理
        synchronized (lock){
            long sId=dis.readLong();
            long courseID = dis.readLong();
            Boolean flag =false;

            Student student = cache.queryStudent(sId);
            Course[] courses = cache.queryCourseOfStu(sId);

            for (int i=0;i<courses.length;i++){
                if (courses[i]!=null && courses[i].getCourseID() == courseID){
                    flag=true;
                    break;
                }
            }

            if(flag){
                Course course = cache.queryCourse(courseID);
                dos.writeBoolean(true);
                cache.removeCourse(student,course);
            }
            else{
                dos.writeBoolean(false);
            }
            dos.flush();
        }

    }

    /**
   	 * 查询请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void queryResponse(DataInputStream dis,DataOutputStream dos)throws Exception{           //对应学生查询所有课程处理
        synchronized (lock){
            List<Course> courses = StudentCache.courseList;
            dos.writeInt(FileProperty.MAX_COURSE_NUM);        //先传有多少门课程
            dos.flush();
            for (int i=0;i<FileProperty.MAX_COURSE_NUM;i++){
                writeCourse(courses.get(i), dos);         //传入课程信息   courseMap获取课程信息
            }
            dos.flush();
        }

    }

    /**
   	 * 查询学生请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void queryStuResponse(DataInputStream dis,DataOutputStream dos) throws Exception{        //对应学生查询自己的选课信息处理
        synchronized (lock){
            long sId = dis.readLong();

            int courseNum=0;
            Course[] courses = cache.queryCourseOfStu(sId);
            for(int i=0;i<courses.length;i++){
                if (courses[i]!=null)
                    courseNum++;
            }

            dos.writeInt(courseNum);
            dos.flush();

            for (int i=0;i<courses.length;i++){
                if (courses[i] != null){
                    writeCourse(courses[i],dos );
                }
            }
            dos.flush();
        }

    }

    
    /**
   	 * 查询选课信息请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void queryInfoResponse(DataInputStream dis,DataOutputStream dos) throws Exception{
        synchronized (lock){
            long sId = dis.readLong();
            int num =0;

            SelectInfo[] selectInfos = cache.querySelectInfoOfStu(sId);
            if (selectInfos == null){
                dos.writeInt(num);
                dos.flush();
            }
            else{
                for (int i=0;i<selectInfos.length;i++){
                    if (selectInfos[i] != null){
                        num ++;
                    }
                }
                dos.writeInt(num);
                dos.flush();
                for (int i=0;i<selectInfos.length;i++){
                    if (selectInfos[i]!= null)
                        writeSelectInfo(selectInfos[i],dos);
                }
            }
        }
    }

    
    /**
   	 * 增加学生请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void addStuResponse(DataInputStream dis,DataOutputStream dos)throws Exception{
        Student student = readStudent(dis);
        if(cache.queryStudent(student.getsID())!=null){
            dos.writeBoolean(false);
        }
        else{
            cache.addNewStudent(student);
            dos.writeBoolean(true);
        }
    }
    
    /**
   	 * 增加课程请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void addCourseResponse(DataInputStream dis,DataOutputStream dos)throws Exception{       //对应教务要加课的处理
        Course course =readCourse(dis);
        List<Course> courses = StudentCache.courseList;
        Boolean flag = true;
        for (int i=0;i<courses.size();i++){
            if (courses.get(i).getCourseID() == course.getCourseID()){
                flag=false;
                break;
            }
        }
        if (flag){
            StudentCache.courseList.add(course);
            FileProperty.setMaxCourseNum(FileProperty.MAX_COURSE_NUM+1);
            dos.writeBoolean(true);

        }
        else {
            dos.writeBoolean(false);
        }
    }

    
    /**
   	 * 删除课程请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void delCourseResponse(DataInputStream dis,DataOutputStream dos) throws Exception{       //对应教务要删课的处理
        long courseId = dis.readLong();
        Course course = cache.queryCourse(courseId);
        if(course.getMembers() == 0){

            StudentCache.courseList.remove(course);
            FileProperty.setMaxCourseNum(FileProperty.MAX_COURSE_NUM-1);
            dos.writeBoolean(true);

        }
        else{
            dos.writeBoolean(false);
        }
    }
    
    
    /**
   	 * 修改课程请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void chanegCourse(DataInputStream dis,DataOutputStream dos) throws Exception{
        long courseId= dis.readLong();
        String name =dis.readUTF();
        int totalCapacity = dis.readInt();
        Course course = cache.queryCourse(courseId);
        if (course!= null && totalCapacity>=course.getMembers()){
            course.setName(name);
            course.setTotalCapacity(totalCapacity);
            course.setLeftCapacity(totalCapacity-course.getMembers());
            dos.writeBoolean(true);
        }
        else{
            dos.writeBoolean(false);
        }
        dos.flush();
    }

    
    /**
   	 * 删除学生请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void delStuResponse(DataInputStream dis,DataOutputStream dos) throws Exception {
        long sId = dis.readLong();
        Boolean flag = cache.delStudent(sId);
        dos.writeBoolean(flag);
        dos.flush();
    }

    /**
   	 * 修改学生请求
   	 * @param dis
   	 * @param dos  
   	 */
    private void modifyStuResponse(DataInputStream dis,DataOutputStream dos) throws Exception{
        Student student = readStudent(dis);
        Boolean flag = cache.updateStudent(student);
        dos.writeBoolean(flag);
        dos.flush();
    }

    
    /**
   	 * 写入学生
   	 * @param dis
   	 * @param dos  
   	 */
    private void writeStudent(Student student,DataOutputStream dos)throws Exception{
        dos.writeLong(student.getsID());
        dos.writeInt(student.getClassID());
        dos.writeUTF(student.getName());
        dos.writeBoolean(student.getGender());

        dos.flush();

    }
    
    /**
   	 * 写入课程
   	 * @param dis
   	 * @param dos  
   	 */
    private void writeCourse(Course course,DataOutputStream dos)throws Exception{
        dos.writeLong(course.getCourseID());
        dos.writeUTF(course.getName());
        dos.writeInt(course.getTotalCapacity());
        dos.writeInt(course.getLeftCapacity());
        dos.writeInt(course.getMembers());
        dos.flush();
    }
    
    /**
   	 * 写入选课信息
   	 * @param dis
   	 * @param dos  
   	 */
    private void writeSelectInfo(SelectInfo selectInfo,DataOutputStream dos) throws Exception{
        dos.writeLong(selectInfo.getsID());
        dos.writeLong(selectInfo.getcID());
        dos.writeUTF(selectInfo.getDate());
        dos.flush();
    }

    /**
   	 * 读取课程
   	 * @param dis
   	 * @param dos  
   	 */
    private Course readCourse(DataInputStream dis) throws Exception{
        long courseId = dis.readLong();
        String name =dis.readUTF();
        int totalCapacity = dis.readInt();

        return new Course(courseId,name,totalCapacity);

    }
    
    /**
   	 * 读取学生信息
   	 * @param dis
   	 * @param dos  
   	 */
    private Student readStudent(DataInputStream dis) throws Exception{
        long sId = dis.readLong();
        int classId = dis.readInt();
        String name = dis.readUTF();
        Boolean gender = dis.readBoolean();
        return new Student(sId,name, classId,gender);
    }



}
