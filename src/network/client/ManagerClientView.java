package network.client;

import basis.Course;
import basis.SelectInfo;
import basis.Student;


public interface ManagerClientView extends ClientView{
    void showCourses(Course[] courses);                 //显示所有课程
    void showStudentInfo(Student student,Course[] courses, SelectInfo[] selectInfos);        //显示学生的详细信息
    int chooseManaWork();               //选择教务的工作
    Object getNewCourse();              //获取新的课程
    long getCourseId();                 //输入获取一个课程号
    long getStudentId();                //输入获取一个学号
    Object chanegCourse();              //修改已有的课程
    Object getNewStudent();             //获取新的学生

}
