package io;

import basis.SelectInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static io.FileProperty.*;

public class SelectInfoFile {
	static long countsOfInfo;//记录当前选课记录的条数
	static RandomAccessFile raf;//选课信息文件
	static int[] lock = new int[0];//锁

	static{
		try {
			raf=new RandomAccessFile(selectInfoFile,selectInfoFileMode);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SelectInfoFile() throws Exception {
		countsOfInfo=raf.length()/32;
	}
	
	
	
	public void delOnePiece() {//覆盖，选课记录应该是删不了的，相当于日志
		                       //当你删课的时候就是将学生信息里面的选课信息地址置为0
	    	                   
	}
	
	/**
	 * 增加选课信息
	 * @param sID,cID,date
	 * @return 
	 */
	public long  addOnePiece(long sID,long cID,String date) throws Exception {//添加一条选课记录
		raf.seek(raf.length());
		if(date.length()<16) {
			while(date.length()<16) {
				date += "\u0000";
			}
		}else {
			date = date.substring(0, 16);
		}
		raf.writeLong(sID);
		raf.writeLong(cID);
		raf.write(date.getBytes());
		countsOfInfo = raf.length()/32;
		return countsOfInfo;//返回选课信息地址
	}
	
	
	/**
	 * 重写选课信息
	 * @param newSelectInfo
	 * @return 
	 */
	public static long retrive(SelectInfo newSelectInfo) throws Exception {
		synchronized (lock){
			raf.seek(raf.length());
			raf.writeLong(newSelectInfo.getsID());
			raf.writeLong(newSelectInfo.getcID());
			raf.write(newSelectInfo.getDate().getBytes());
			countsOfInfo = raf.length()/32;
		}
		return countsOfInfo;
		//选课信息的address条数从1开始，然后返回该条信息在文件中的条数，
		//写回student地址数组，然后再调用studentFile的retrive函数；
	}
	
	
	
	/**
	 * 获取当前选课信息条数
	 * @return 
	 */
	public long getCurrentCountsOfInfo() throws IOException {
		countsOfInfo=raf.length()/32;
		return countsOfInfo;
	}
	
	
	/**
	 * 获取学生选课信息
	 * @param addressOfStudent
	 * @return 
	 */
	public static SelectInfo getOnePiece(long addressOfStudent) throws Exception {
		if(addressOfStudent==-1) {
			return null;
		}

		raf.seek((addressOfStudent-1)*32);//由于条数从1开始
		long sID=raf.readLong();
		long cID=raf.readLong();
		String date="";
		
		for(int i=0;i<16;i++) {
			int temp;
			if((temp=raf.readByte())!=-1)	date=date+(char)temp;
			else  break;
		}
	    
		return new SelectInfo(sID, cID, date);
	}

}
