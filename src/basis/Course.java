package basis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 课程类
 */
public class Course {
	private long courseID;//8
	private String name;//32
	private int totalCapacity;//4
	private volatile int leftCapacity;//4
	private volatile int members = 0;   // 4  total 52bytes


	public Course() {}
	
	public Course(long courseID, String name, int totalCapacity) {
		super();
		while(name.getBytes().length<32) {
			name=name+"\u0000";
		}
		this.courseID = courseID;
		this.name = name;
		this.totalCapacity = totalCapacity;
		this.leftCapacity = totalCapacity;
	}
	
	
	public Course(long courseID, String name, int totalCapacity, int leftCapacity, int members) {
		super();
		while(name.getBytes().length<32) {
			name=name+"\u0000";
		}
		this.courseID = courseID;
		this.name = name;
		this.totalCapacity = totalCapacity;
		this.leftCapacity = leftCapacity;
		this.members = members;
	}




	public long getCourseID() {
		return courseID;
	}
	public void setCourseID(long courseID) {
		this.courseID = courseID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		while(name.getBytes().length<32) {
			name=name+"\u0000";
		}
		this.name = name;
	}
	public int getTotalCapacity() {
		return totalCapacity;
	}
	synchronized public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}
	synchronized public int getLeftCapacity() {
		return leftCapacity;
	}

	public void setLeftCapacity(int leftCapacity) {
		this.leftCapacity = leftCapacity;
		this.members = totalCapacity - leftCapacity;
	}
	public int getMembers() {
		return members;
	}
	
	@Override
	public String toString() {
		return "Course [courseID=" + courseID + ", name=" + name.trim() + ", totalCapacity=" + totalCapacity
				+ ", leftCapacity=" + leftCapacity + ", members=" + members + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == Course.class) {
			Course c = (Course)obj;
			return this.courseID == c.courseID;
		}
		return false;
	}

	//结合选课记录输出课程信息
	public String toString(SelectInfo selectInfo){
		return "Course [courseId="+ courseID +", name="+ name.trim()+ ", date: " + selectInfo.getDate()+"]";
	}

}
