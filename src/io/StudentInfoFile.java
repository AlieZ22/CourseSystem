package io;

import basis.Course;
import basis.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import static io.FileProperty.*;

public class StudentInfoFile {
		static long countsOfStudent;          //学生记录的条数
		static long delCountsofStudent;       //删除学生的个数
		static RandomAccessFile raf;          //学生信息文件
		
		static {
			try {
				raf=new RandomAccessFile(studentFile,studentFIleMode);
				countsOfStudent=raf.length()/FileProperty.EACH_PIECE_STUDENTINFO_SIZE;
				delCountsofStudent=0;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		public StudentInfoFile() throws Exception {

		}
		
		

		
		/**
		 * 管理员录入学生信息
		 * @param stu
		 */
		public static void addStudentInfo(Student stu) throws Exception {//增
			synchronized (raf){

				int size= FileProperty.EACH_PIECE_STUDENTINFO_SIZE;
				raf.seek((stu.getsID()-1)*size);
				raf.writeLong(stu.getsID());
				raf.write(stu.getName().getBytes());
				raf.writeInt(stu.getClassID());
				raf.writeBoolean(stu.getGender());
				
				for(int i=0;i<FileProperty.MAX_ADDRESS_NUM;i++) {
					raf.writeLong(-1);
				}
				raf.writeUTF("\r\n");
				countsOfStudent++;
			}
		}
		
		
		
		/**
		 * 根据学号删除学生信息
		 * @param sID
		 */
		static public void deleteStudentInfo(long  sID) throws Exception {
			raf.seek((sID-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
			for(int i=0;i<FileProperty.EACH_PIECE_STUDENTINFO_SIZE-2;i++) {
				raf.writeByte(0);
			}		
			raf.writeUTF("\r\n");
			delCountsofStudent++;
		}
		
		

		/**
		 * 修改学生信息
		 * @param newStudent
		 */
		public void modifyStudentInfo(Student newStudent) throws Exception {//改
			raf.seek((newStudent.getsID()-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
			raf.writeLong(newStudent.getsID());
			raf.write(newStudent.getName().getBytes());
			raf.writeInt(newStudent.getClassID());
			raf.writeBoolean(newStudent.getGender());
			for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++)
			{
				raf.writeLong(newStudent.courseIdx[i]);
			}
		}
		
		
		
		/**
		 * 当学生信息在缓存中未找到时，根据学号在文件中搜索学生信息
		 * @param sID
		 * @return 
		 */
		public static Student searchStudentInfo(long sID) throws Exception {
			if(sID<=0) {
				System.out.println("学生记录中无该学生！！！");
				return null;
			}
			else {
				synchronized (raf){
					raf.seek((sID-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
					long readSid = 0;
					try{
						readSid=raf.readLong();
					}
					catch (Exception e){
						return null;
					}
					if(readSid==0) {
						System.out.println("该学生已被删除！！！");
						return null;
					}
					else {
						byte[] buffer=new byte[32];
						raf.read(buffer);
						String readSName=new String(buffer);
						int readSclass=raf.readInt();
						boolean readGender=raf.readBoolean();
						long[] sSelectInfoAddress=new long[FileProperty.MAX_COURSE_NUM];
						for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
							sSelectInfoAddress[i]=raf.readLong();
						}
						return new Student(readSid,readSName,readSclass,readGender,sSelectInfoAddress);
					}
				}
			}
		}
		
		
		
		/**
		 * 读取学生信息
		 * @param stuID
		 * @return 
		 */
		public static Student readOnePiece(long stuID) throws IOException {
			synchronized (raf){
				raf.seek((stuID-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
				long sID=raf.readLong();
				byte[] buffer=new byte[32];
				raf.read(buffer);
				String sName=new String(buffer);
				int sClassId=raf.readInt();
				boolean sGender=raf.readBoolean();
				long[] sSelectInfoAddress=new long[FileProperty.MAX_COURSE_NUM];
				for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
					sSelectInfoAddress[i]=raf.readLong();
				}
				return new Student(sID,sName,sClassId,sGender,sSelectInfoAddress);
			}

		}
		
			
		
		/**
		 * 选课时需先判断该门课程是否已选；若缓存选课信息被改写，则重写学生选课地址
		 * @param stuID
		 */
		public static void retrive(Student modifiedStudent, int oldLength) throws Exception {
			synchronized (raf){
				raf.seek((modifiedStudent.getsID()-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
				raf.skipBytes(45);
				for(int i=0;i<oldLength;i++) {
					raf.writeLong(modifiedStudent.courseIdx[i]);
				}
			}
		}
		
		
	 
		/**
		 * 添加选课信息
		 * @param stuID
		 */
		public void addSelectInfoAddress(long sID,long address,int courseNum) throws Exception {
			raf.seek((sID-1)*FileProperty.EACH_PIECE_STUDENTINFO_SIZE);
			raf.skipBytes(45);
			for(int i=0;i<FileProperty.MAX_COURSE_NUM;i++) {
				if(courseNum == i+1) {
					raf.writeLong(address);
				}else {
					raf.skipBytes(8);
				}
				
			}
		}

				
	
     
		/**
		 * 添加初始学生信息
		 * @param stuID
		 */
		public static void addOriginalStudentInfo(Student stu) throws Exception {//增
			synchronized (raf){
				int size=FileProperty.EACH_PIECE_STUDENTINFO_SIZE;  //97
				raf.seek((stu.getsID()-1)*size);
				raf.writeLong(stu.getsID());
				raf.write(stu.getName().getBytes());
				raf.writeInt(stu.getClassID());
				raf.writeBoolean(stu.getGender());
				
				for(int i=0;i<FileProperty.MAX_ADDRESS_NUM;i++) {
					raf.writeLong(-1);
				}
				raf.writeUTF("\r\n");
				countsOfStudent++;
			}

		}	
		
		
		
		/**
		 * 得到随机字符串作为姓名
		 * @param stuID
		 * @return 
		 */
		public static String getRandomString(int length){
			String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			Random random=new Random();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<length;i++){
				int number=random.nextInt(62);
				sb.append(str.charAt(number));
			}
			return sb.toString();
		}
		
	
	
	
		/**
		 * 生成1000000条初始数据
		 * @param stuID
		 */
		public static void WriteOriginalStudentInfo() throws Exception {
			raf.seek(0);
			java.util.Random r=new java.util.Random();
			for (long i = 1; i <= 1000000; i++) {
				String name=getRandomString(8);
				int classID=(int) (i%3+1);
				boolean gender=r.nextBoolean();
				addOriginalStudentInfo(new Student(i,name,classID,gender));
			}
		}
	
	
		public static void main(String[] args) throws Exception {
			StudentInfoFile sif=new StudentInfoFile();
			sif.WriteOriginalStudentInfo();
			
			sif.raf.close();
			CourseFile cf=new CourseFile();
			Course course1=new Course(1, "语文", 100);
			Course course2=new Course(2, "数学", 100);
			Course course3=new Course(3, "英语", 100);
			cf.writeOriginalThreeCourses(course1);
			cf.writeOriginalThreeCourses(course2);
			cf.writeOriginalThreeCourses(course3);
			cf.raf.close();
		}
		
}


