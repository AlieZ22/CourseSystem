package io;

import basis.SelectInfo;

import java.util.Arrays;

public class CoursePage {
	public SelectInfo[] page;
	public int[] modified;   // 表示修改位 0：不动， 1：添加，2：删除
	public boolean isModified = false;

	public CoursePage(int[] index) {
		super();
		init(index);
		modified = new int[index.length];
		for(int i=0;i<index.length;i++) {
			modified[i] = 0;
		}
		isModified = false;
	}
	
	public CoursePage(SelectInfo[] page) {
		this.page = page;
		modified = new int[page.length];
		for(int i=0;i<page.length;i++) {
			modified[i] = 0;
		}
		isModified = false;
	}
	
	public void init(int[] index) {
		page = new SelectInfo[index.length];
		for(int i=0;i<index.length;i++) {
			// 将文件中对应于indx[i]行的选课信息 加载进来
			
		}
	}

	@Override
	public String toString() {
		return "CoursePage [page=" + Arrays.toString(page) ;
	}
	
	
}
