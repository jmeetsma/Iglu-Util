package org.ijsberg.iglu.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SortedListMap<K, V> {
	
	private TreeMap<K, List<V>> impl = new TreeMap<K, List<V>>();
	
	public SortedListMap() {
		
	}
	
	public List<V> put(K key, V value) {
		List<V> list = impl.get(key);
		if(list == null) {
			list = new ArrayList<V>();
			impl.put(key, list);
		}
		list.add(value);
		return list;
	}
	
	public TreeMap<K, List<V>> getMap() {
		return impl;
	}
	
	public List<V> get(K key) {
		return impl.get(key);
	}
	
	public String toString() {
		return impl.toString();
	}

	public Set<K> keySet() {
		return impl.keySet();
	}
	
	public int size() {
		//nr of items
		int retval = 0;
		for(List<V> list : impl.values()) {
			retval += list.size();
		}
		return retval;
	}


	public Collection<V> values() {
		List<V> retval = new ArrayList<V>();
		for(List<V> list : impl.values()) {
			retval.addAll(list);
		}
		return retval;
	}
	
	public List<V> getTop(int x) {
		
		List<V> retval = new ArrayList<V>();
		for(List<V> values : impl.descendingMap().values()) {
			if(retval.size() == x) {
				return retval;
			} else if (values.size() < x - retval.size()) {
				retval.addAll(values);
			} else {
				retval.addAll(values.subList(0, x - retval.size()));
			}
		}
		return retval;
	}

}
