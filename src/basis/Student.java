package basis;

import io.FileProperty;

/**
 * 学生类
 */
public class Student {
	private long sID; //8
	private String name;//32
	private int classID;//4
	private boolean gender;   // true为男  1
	public static long[] courseIdx = new long[FileProperty.MAX_COURSE_NUM]; //
	
	static {
		for(int i = 0; i< FileProperty.MAX_COURSE_NUM; i++) {
			courseIdx[i] = -1;
		}
	}
	
	public Student() {}
	
	public Student(long sID, String name, int classID, boolean gender) {
		super();
		while(name.getBytes().length<32) {
			name=name+"\u0000";
		}
		this.sID = sID;
		this.name = name;
		this.classID = classID;
		this.gender = gender;
		for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++){
			courseIdx[i]=-1;
		}
	}
	
	//这个构造函数用来提取学生文件信息的时候用
	public Student(long sID, String name, int classID, boolean gender,long[] selectInfoAddress) {
		super();
		this.sID = sID;
		this.classID = classID;
		this.gender = gender;
		//对姓名的操作需要处理一波
		while(name.getBytes().length<32) {
			name=name+"\u0000";
		}
		this.name = name;
		for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
			courseIdx[i]=selectInfoAddress[i];
		}
	}
	
	public long getsID() {
		return sID;
	}
	public void setsID(long sID) {
		this.sID = sID;
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
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public boolean getGender() {
		return gender;
	}
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "Student [sID=" + sID + ", name=" + name.trim()+ ", classID=" + classID + ", gender=" + gender + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == Student.class) {
			Student s = (Student)obj;
			return this.sID == s.sID;
		}
		return false;
	}

	public void showAddress(){
		for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
			System.out.println(courseIdx[i]);
		}
	}

	public static void main(String[] args) {

	}





}
