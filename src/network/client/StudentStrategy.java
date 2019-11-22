package network.client;

public interface StudentStrategy {
    Object queryStudent(long sId) throws Exception;                           //学生登录,向服务发起请求，获得学生的有关信息
    void addRequest(long sId,long courseId) throws  Exception;            //增加课程
    void delRequest(long sId,long courseId) throws Exception;            //删除课程
    Object queryRequest() throws  Exception;                                //请求查看所有课程内容
    Object queryRequest(long sId) throws Exception;                       //请求查看学生的选课信息
    Object queryInfoRequest(long sId) throws Exception;                   //查询学生的选课记录
}
