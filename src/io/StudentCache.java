package io;

import basis.Course;
import basis.SelectInfo;
import basis.Student;

import java.util.*;

public class StudentCache extends LRUCache<Student, CoursePage>{
	
	private static final long serialVersionUID = 1L;
	private static final int offset = 1;
	public static List<Course> courseList;

	public StudentCache(int capacity) {
		super(capacity);
		preLoad(capacity);
	}
	
	
	/**
	 * 预加载
	 * @param preSize
	 */
	@Override
	public void preLoad(int preSize) {
		if(preSize>this.capacity) {
			preSize = this.capacity;
		}
		try {
			// 预加载学生和选课信息
			for(int i=offset;i<offset+preSize;i++) {
				Student s = null;
				s = StudentInfoFile.readOnePiece(i);
				if(s.getsID()!=0) {
					add(s);
				}
			}
			// 预加载课程信息
			courseList = new ArrayList<>(FileProperty.MAX_COURSE_NUM);
			CourseFile.getAllCourses(courseList);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 置换时，写回被修改的记录
	 * @param key
	 * @param value
	 */
	@Override
	public void displace(Student key, CoursePage value) {
		try {
			// 写回选课信息
			for(int i=0;i<value.page.length;i++) {
				if(value.modified[i]!=0) {
					// 写回一条选课信息 SelectInfo
					long n = -1;
					if(value.modified[i]==1){  //表示被添加
						n = SelectInfoFile.retrive(value.page[i]);
					}
					key.courseIdx[i] = n;
				}
			}
			// 写回学生 Student
			StudentInfoFile.retrive(key, value.page.length);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重载add函数，根据学生自动获取CoursePage并添加
	 * @param s
	 */
	public void add(Student s) {
		// TODO: 根据学生自动从文件中获取CoursePage
		try {
			SelectInfo[] page = new SelectInfo[FileProperty.MAX_COURSE_NUM];

			for(int i=0;i<page.length;i++) {
				page[i] = SelectInfoFile.getOnePiece(s.courseIdx[i]);
			}
			CoursePage cp = new CoursePage(page);
			add(s, cp);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 添加一个文件中没有的学生
     * @param s
     */
	synchronized public void addNewStudent(Student s){
	    try{
            add(s);
            StudentInfoFile.addStudentInfo(s);
			System.out.println("成功添加新学生");
        }catch (Exception e){
	        e.printStackTrace();
        }
    }

	
	/**
	 * 某一名学生选一门课
	 * @param s
	 * @param c
	 * @return
	 */
	synchronized public boolean addCourse(Student s, Course c) {
		// 经确定 是可以添加了

		int index = courseList.indexOf(c);
		CoursePage cp = query(s);
		Date date = new Date();
		if(cp.page[index] == null) {
			cp.page[index] = new SelectInfo(s.getsID(), c.getCourseID(), date.toString());
			cp.modified[index] = 1;
			cp.isModified = true;
			c.setLeftCapacity(c.getLeftCapacity()-1);
			add(s,cp);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 某一名学生退选一门课
	 * @param s
	 * @param c
	 * @return
	 */
	synchronized public boolean removeCourse(Student s, Course c) {
		int index = courseList.indexOf(c);
		CoursePage cp = query(s);
		if(cp.page[index] != null) {
			cp.page[index] = null;
			cp.modified[index] = 2;
			cp.isModified = true;
			s.courseIdx[index] = -1;
			c.setLeftCapacity(c.getLeftCapacity()+1);
		}
		return false;
	}
	
	/**
	 * 根据学号返回学生
	 * @param sId
	 * @return
	 */
	public Student queryStudent(long sId) {
		Iterator it = this.entrySet().iterator();
		// 先在缓存中看是否存在
		while(it.hasNext()) {
			Map.Entry<Student, CoursePage> entry = 
					(Map.Entry<Student, CoursePage>) it.next();
			if(entry.getKey().getsID() == sId) {
				return entry.getKey();
			}
		}
		// 再到文件中看是否有这个学生
		try {
			Student s = StudentInfoFile.searchStudentInfo(sId);
			if(s!=null) {
				// 文件中有该学生，将其加入进StudentCache, 并返回给客户端
				add(s);
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 根据课程号查课程
	 * @param cId
	 * @return
	 */
	public Course queryCourse(long cId) {
		Iterator<Course> it = courseList.iterator();
		while(it.hasNext()) {
			Course c = it.next();
			if(c.getCourseID() == cId) {
				return c;
			}
		}
		return null;
	}

	/**
	 * 返回一个学生所选的所有课程（定长）
	 * @param sId
	 * @return
	 */
	public Course[] queryCourseOfStu(long sId) {
		Student s = queryStudent(sId);
		CoursePage cp = query(s);
		Course[] courses = new Course[FileProperty.MAX_COURSE_NUM];
		if (cp == null )
			return courses;
		for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
			if(cp.page[i]!=null) {
				courses[i] = queryCourse(cp.page[i].getcID());
			}else {
				courses[i] = null;
			}
		}
		return courses;
	}


	/**
	 * 根据学号查一个学生的选课记录(定长)
	 * @param sId
	 * @return
	 */
	public SelectInfo[] querySelectInfoOfStu(long sId) {
		Student s = queryStudent(sId);
		if (s == null)
			return null;
		return query(s).page;
	}

	/**
	 * 根据学号删除某个学生
	 * @param sId
	 * @return
	 */
	public Boolean delStudent(long sId) {
		Student s = queryStudent(sId);
		if(s!=null) {
			// 说明文件中有该学生
			try {
				remove(s);    // 从缓存中删除
				StudentInfoFile.deleteStudentInfo(sId);   // 从文件中删除
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else{
			return false;
		}
	}

	/**
	 * 修改某个学生信息
	 * @param  s
	 * @return
	 */
	public boolean updateStudent(Student s) {
		Student student = queryStudent(s.getsID());

		if(student ==null) {
			return false;
		}
		student.setsID(s.getsID());
		student.setName(s.getName());
		student.setClassID(s.getClassID());
		student.setGender(s.getGender());
		System.out.println("成功，退出修改...");
		return true;
	}
	/**
	 * 遍历缓存输出
	 */
	public void travel() {
		Iterator it = this.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Student, CoursePage> entry = 
					(Map.Entry<Student, CoursePage>) it.next();
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
	}
	
	
	/**
	 * 将所有缓存写回文件
	 */

	public void close() {
		try {
//			System.out.println("close: 第一门课选的人数："+courseList.get(0).getMembers());
			// 写回所有课程信息
			CourseFile.setAllCourses(courseList);
			// 写回选课信息
			// 写回学生信息
			// 遍历整个缓存，被修改的一条写回文件
			Iterator it = this.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<Student, CoursePage> entry =
						(Map.Entry<Student, CoursePage>) it.next();
				CoursePage cp = entry.getValue();
				if(cp.isModified) {
					Student s = entry.getKey();
					displace(s, cp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 测试
	public static void main(String[] args) {
	/*	
		Course c1 = new Course(101010, "数学", 100);
		Course c2 = new Course(202020, "语文", 100);
		Course c3 = new Course(303030, "英语", 100);
		
		Student s1 = new Student(1111, "aaa", 5, true);
		SelectInfo[] sis1 = {
				new SelectInfo(s1.getsID(), c1.getCourseID(), "2019.7.7"),
				null,
				new SelectInfo(s1.getsID(), c3.getCourseID(), "2019.7.8")
				};
		CoursePage page1 = new CoursePage(sis1);
		Student s2 = new Student(2222, "bbb", 6, true);
		SelectInfo[] sis2 = {
				new SelectInfo(s2.getsID(), c1.getCourseID(), "2019.7.7"),
				null,
				null
				};
		CoursePage page2 = new CoursePage(sis1);
		Student s3 = new Student(3333, "ccc", 7, false);
		SelectInfo[] sis3 = {
				new SelectInfo(s3.getsID(), c1.getCourseID(), "2019.7.7"),
				new SelectInfo(s3.getsID(), c2.getCourseID(), "2019.7.5"),
				new SelectInfo(s3.getsID(), c3.getCourseID(), "2019.7.8")
				};
		CoursePage page3 = new CoursePage(sis3);
		Student s4 = new Student(4444, "ddd", 9, true);
		SelectInfo[] sis4 = {
				new SelectInfo(s4.getsID(), c1.getCourseID(), "2019.7.7"),
				null,
				new SelectInfo(s4.getsID(), c3.getCourseID(), "2019.7.8")
				};
		CoursePage page4 = new CoursePage(sis4);
		Student s5 = new Student(5555, "eee", 8, false);
		SelectInfo[] sis5 = {
				null,
				null,
				new SelectInfo(s5.getsID(), c3.getCourseID(), "2019.7.8")
				};
		CoursePage page5 = new CoursePage(sis5);
		
		StudentCache cache = new StudentCache(3);
		cache.add(s1, page1);
		cache.add(s2, page2);
		cache.add(s3, page3);
		cache.query(s1);
		cache.add(s4, page4);
		cache.add(s5, page5);
	*/
		
		StudentCache cache = new StudentCache(24);
		cache.travel();
		
		// 测试多线程选课（2个人）
//		Iterator iterator = cache.entrySet().iterator();
//		while(iterator.hasNext()) {
//			Map.Entry<Student, CoursePage> entry =
//					(Map.Entry<Student, CoursePage>) iterator.next();
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					Student s = entry.getKey();
//					System.out.println(s+"开始测试选课");
//					if(s.getsID()%2==0) {
//						cache.addCourse(s, courseList.get(0));
//						cache.addCourse(s, courseList.get(1));
//						cache.addCourse(s, courseList.get(2));
//						System.out.println("课余量2："+ courseList.get(0).getLeftCapacity() 
//								+" "+courseList.get(1).getLeftCapacity()
//								+" "+courseList.get(2).getLeftCapacity());
//					}else{
//						cache.addCourse(s, courseList.get(0));
//						cache.addCourse(s, courseList.get(2));
//						System.out.println("课余量1："+ courseList.get(0).getLeftCapacity() 
//								+" "+courseList.get(1).getLeftCapacity()
//								+" "+courseList.get(2).getLeftCapacity());
//					}
//				}
//			}).start();
//		}
		
		
	}

	
}
