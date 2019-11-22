package io;

import basis.Student;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class LRUCache<K, V> extends LinkedHashMap<K, V> 
										implements Cacheable<K, V>{
	
	private static final long serialVersionUID = 1L;
	protected int capacity;
	private final Lock lock = new ReentrantLock();
	
	public LRUCache(int capacity) {
		super(16, 0.75f, true);
		this.capacity = capacity;
	}
	
	
	/**
	 * 重写LinkedHashMap的removeEldestEntry，实现LRU置换
	 */
	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		if(size()>capacity) {
			Student s = (Student) eldest.getKey();
//			System.out.println(s.getName()+"被置换");
			CoursePage cp = (CoursePage)eldest.getValue();
			if(cp.isModified) {
				displace(eldest.getKey(), eldest.getValue());
			}
		}
		return size()>capacity;
	}

	@Override
	public void add(K key, V value) {
		try {
			lock.lock();
			put(key, value);
		}finally {
			lock.unlock();
		}
	}

	@Override
	public V query(K key) {
		try {
			lock.lock();
			if(key==null){
				return null;
			}
			return get(key);
		}finally {
			lock.unlock();
		}
	}
	
}
