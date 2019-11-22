package network.client;

import basis.Course;
import basis.Student;

public interface ManagerStrategy {
    void addCourse(Course course) throws Exception;               //增加课程
    void delCourse(long cId) throws Exception;                    //删除课程
    void modifyCourse(Course course) throws Exception;            //修改课程信息
    void addStudent(Student student) throws Exception;            //增加学生信息
    void delStudent(long sId) throws Exception;                   //减少学生信息
    void modifyStudent(Student student) throws Exception;         //修改学生信息
    void stopServer() throws Exception;                           //停止服务器
    void stopManager() throws Exception;                          //发送教务退出的信号

}
