package io;

public interface Cacheable<K, V> {
	
	void preLoad(int preSize);   // 预加载缓存
	void add(K key, V value); 
	V query(K key);
	void displace(K key, V value);    // 置换
}
