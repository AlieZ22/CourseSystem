package network.client;



public interface StudentClientView extends ClientView {
    long getStuId();                        //获取学生Id
    long getCourseId();                     //获取课程Id
    int chooseStuWork();                       //选择何种操作

    void showStudentInfo();                 //展示学生信息
    void showCourseInfo();                  //展示课程信息
    void showCourseOfStu();                 //显示学生课表
}
