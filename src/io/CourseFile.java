package io;

import basis.Course;

import java.io.*;
import java.util.List;
import static io.FileProperty.*;

public class CourseFile {

	public static RandomAccessFile raf;    ///存放课程信息的文件
	static {
		try{
			raf=new RandomAccessFile(courseFile, courseFileMode);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public CourseFile() throws Exception {
		
	}

	
	/**
	 * 写入最初三门课
	 * @param course
	 */
	public static void writeOriginalThreeCourses(Course course) throws Exception {
		raf.writeLong(course.getCourseID());
		raf.write(course.getName().getBytes());
		raf.writeInt(course.getTotalCapacity());
		raf.writeInt(course.getLeftCapacity());
		raf.writeInt(course.getMembers()); 
	}

	

	/**
	 * 缓存中要读取全部课程信息
	 * @param courseList
	 */
	public static void getAllCourses(List<Course> courseList) throws Exception{
		raf.seek(0);
		for(int i = 0; i< FileProperty.MAX_COURSE_NUM; i++) {
			long courseID=raf.readLong();
			byte[] buffer=new byte[32];
			raf.read(buffer);
			String courseName=new String(buffer);

			int totalCapacity=raf.readInt();
			int leftCapacity=raf.readInt();
			int members = raf.readInt();
			Course c =  new Course(courseID,courseName,totalCapacity,leftCapacity,members);
			courseList.add(c);
		}
	}
	
		
	/**
	 * 当添加课程或删除课程时，重新设置全部选课信息；
	 * @param courseList
	 */
	public static void setAllCourses(List<Course> courseList) throws Exception {
		raf.seek(0);
		raf.setLength(courseList.size()*52);
		for(int i=0;i<courseList.size();i++) {
			raf.writeLong(courseList.get(i).getCourseID());
			raf.write(courseList.get(i).getName().getBytes());
			raf.writeInt(courseList.get(i).getTotalCapacity());	
		 	raf.writeInt(courseList.get(i).getLeftCapacity());
		 	raf.writeInt(courseList.get(i).getMembers());
		}
	}
}