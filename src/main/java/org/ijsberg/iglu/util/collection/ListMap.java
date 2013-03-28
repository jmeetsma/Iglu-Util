package org.ijsberg.iglu.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class ListMap<K, V> {
	
	protected TreeMap<K, List<V>> internalMap = new TreeMap<K, List<V>>();
	private int loadFactor = 10;
	//protected List<K> keyList = new ArrayList<K>();
	
	public ListMap() {
	}
	
	public ListMap(int loadFactor) {
		this.loadFactor = loadFactor;
	}

	protected List<V> createOrRetrieveList(K key) {
		List<V> list = internalMap.get(key);
		if(list == null) {
			list = new ArrayList<V>(loadFactor);
			internalMap.put(key, list);
			//keyList = new ArrayList<K>(internalMap.keySet());
		}
		return list;
	}
	
	public List<V> put(K key, V value) {
        if(!(key instanceof Comparable)) {
            throw new ClassCastException("key K must implement Comparable");
        }
		List<V> list = createOrRetrieveList(key);
		list.add(value);
		return list;
	}
	
	public TreeMap<K, List<V>> getMap() {
		return internalMap;
	}
	
	public List<V> get(K key) {
		return internalMap.get(key);
	}
	
	public List<V> getByIndex(int index) {
		return internalMap.get(new ArrayList<K>(internalMap.keySet()).get(index));
	}

	public String toString() {
		return internalMap.toString();
	}

	public Set<K> keySet() {
		return internalMap.keySet();
	}
	
	public Set<K> descendingKeySet() {
		return internalMap.descendingKeySet();
	}

	//TODO expensive
	public int size() {
		//nr of items
		int retval = 0;
		for(List<V> list : internalMap.values()) {
			retval += list.size();
		}
		return retval;
	}


	//TODO expensive

	public Collection<V> values() {
		List<V> retval = new ArrayList<V>();
		for(List<V> list : internalMap.values()) {
			retval.addAll(list);
		}
		return retval;
	}
	
	public List<V> getTop(int x) {
		
		List<V> retval = new ArrayList<V>();
		for(List<V> values : internalMap.descendingMap().values()) {
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

	public V removeFirst(K key) {
		
		List<V> list = internalMap.get(key);
		if(!list.isEmpty()) {
			return list.remove(0);
		}
		return null;
		
	}
	
	public List<V> removeAll(K key) {
		
		return internalMap.remove(key);
		
	}

	public boolean containsRelation(K key, V value) {
		List<V> values;
		return ((values = get(key)) != null && values.contains(value));
	}
	

}
