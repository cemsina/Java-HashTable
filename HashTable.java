import java.util.Iterator;
import java.util.LinkedList;
/**
* Author : Cemsina Guzel
* See : https://github.com/cemsina/Java-HashTable/edit/master/HashTable.java
*/
public class HashTable<K,V> {
	private class Entry{
		private K key;
		private V value;
		public Entry(K key,V value) {
			this.key = key;
			this.value = value;
		}
	}
	private LinkedList<Entry>[] entries;
	private static final double LOAD_FACTOR = 0.7;
	private int size;
	public double currentLoad() {
		return (double)this.size / this.entries.length;
	}
	@SuppressWarnings("unchecked")
	public HashTable(int s) {
		this.entries = new LinkedList[s];
		for(int i=0;i<this.entries.length;i++)
			this.entries[i] = new LinkedList<Entry>();
		this.size = 0;
	}
	public int size() {
		return this.size;
	}
	public Iterator<V> iterator(){
		LinkedList<V> list = new LinkedList<V>();
		for(int i=0;i<this.entries.length;i++) {
			Iterator<Entry> it = this.entries[i].iterator();
			while(it.hasNext())
				list.add(it.next().value);
		}
		return list.iterator();
	}
	public Iterator<K> keys(){
		LinkedList<K> list = new LinkedList<K>();
		for(int i=0;i<this.entries.length;i++) {
			Iterator<Entry> it = this.entries[i].iterator();
			while(it.hasNext())
				list.add(it.next().key);
		}
		return list.iterator();
	}
	public HashTable() {
		this(10);
	}
	private void addRow(int count) {
		if(count > 0) {
			HashTable<K,V> h = new HashTable<K,V>(this.entries.length + count);
			for(int i=0;i<this.entries.length;i++) {
				Iterator<Entry> it = this.entries[i].iterator();
				while(it.hasNext()) {
					Entry e = it.next();
					h.put(e.key, e.value);
				}
			}
			this.entries = h.entries;
		}
	}
	public V get(K key) {
		Entry e = this.getEntry(key);
		if(e == null)
			return null;
		return e.value;
	}
	private Entry getEntry(K key){
		if(key == null)
			return null;
		LinkedList<Entry> list = this.entries[Math.abs(key.hashCode()) % this.entries.length];
		Iterator<Entry> it = list.iterator();
		while(it.hasNext()){
			Entry temp = it.next();
			if(temp.key.equals(key))
				return temp;
		}
		return null;
	}
	public boolean isEmpty() {
		return this.size == 0;
	}
	public boolean exists(K key) {
		return this.getEntry(key) != null;
	}
	public void put(K key,V value) {
		if(key == null)
			return;
		Entry check = this.getEntry(key);
		if(check != null) {
			check.value = value;
			return;
		}
		Entry e = new Entry(key,value);
		int newLen = (int)((double)((this.size+1) / HashTable.LOAD_FACTOR)) + 1;
		this.addRow(newLen - this.entries.length);
		int index = Math.abs(key.hashCode()) % this.entries.length;
		this.entries[index].add(e);
		this.size++;
		return;
	}
	public V remove(K key) {
		LinkedList<Entry> list = this.entries[Math.abs(key.hashCode()) % this.entries.length];
		Iterator<Entry> it = list.iterator();
		while(it.hasNext()) {
			Entry e = it.next();
			if(e.key.equals(key)) {
				V temp = e.value;
				list.remove(e);
				this.size--;
				return temp;
			}
		}
		return null;
	}
}
