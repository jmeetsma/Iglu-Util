package org.ijsberg.iglu.util.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SortedListMap<K, V> {
	
	TreeMap<K, Set<V>> impl = new TreeMap<K, Set<V>>();
	
	public SortedListMap() {
		
	}
	
	public void put(K key, V value) {
		Set<V> set = impl.get(key);
		if(set == null) {
			set = new HashSet<V>();
			impl.put(key, set);
		}
		set.add(value);
	}
	
	public TreeMap<K, Set<V>> getMap() {
		return impl;
	}
	

}
