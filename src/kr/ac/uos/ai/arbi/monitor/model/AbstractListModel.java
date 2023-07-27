package kr.ac.uos.ai.arbi.monitor.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class AbstractListModel<T> implements ListModel, List<T> {
	private static final ListDataListener[] LISTNER_NULL_ARRAY = new ListDataListener[0];
	protected List<T> dataList;
	protected List<ListDataListener> listenerList;
	
	public AbstractListModel() {
		this.dataList = new CopyOnWriteArrayList();
		this.listenerList = new CopyOnWriteArrayList();
	}
	
	public void add(int index, T element) {
		if(element == null)
			return;
		if(!(isLegalIndex(index))) 
			return;
		
		this.dataList.add(index, element);
		fireIntervalAdded(this, index, index);
	}
	
	public boolean add(T element) {
		if(element == null)
			return false;
		this.dataList.add(element);
		int index = this.dataList.size() -1;
		fireIntervalAdded(this, index, index);
		return true;
	}
	
	public T set(int index, T element) {
		if(element == null)
			return null;
		if(!(isLegalIndex(index)))
			return null;
		T e = this.dataList.set(index, element);
		fireContentsChanged(this, index, index);
		return e;
	}
	
	public boolean contains(Object o) {
		if(o == null)
			return false;
		return this.dataList.contains(o);
	}
	
	public T get(int index) {
		if(!(isLegalIndex(index)))
			return null;
		return this.dataList.get(index);
	}
	
	public T remove(int index) {
		if(!(isLegalIndex(index))) {
			return null;
		}
		T obj = this.dataList.remove(index);
		fireIntervalRemoved(this, index, index);
		return obj;
	}
	
	public boolean remove(Object o) {
		if(o == null)
			return false;
		int index = this.dataList.indexOf(o);
		if(index < 0) 
			return false;
		this.dataList.remove(o);
		fireIntervalRemoved(this, index, index);
		return true;
	}
	
	public void clear() {
		int last = this.dataList.size() -1;
		this.dataList.clear();
		if(last < 0)
			return;
		fireIntervalRemoved(this, 0 , last);
	}
	
	public Object[] toArray() {
		return this.dataList.toArray();
	}
	
	public <E> E[] toArray(E[] a) {
		return this.dataList.toArray(a);
	}
	
	public int size() {
		return this.dataList.size();
	}
	
	public int indexOf(Object o) {
		return this.dataList.indexOf(o);
	}
	
	private boolean isLegalIndex(int index) {
		return ((index >= 0) && (index < this.dataList.size()));
	}
	
	public T getElementAt(int index) {
		return get(index);
	}
	
	public int getSize() {
		return size();
	}
	
	public void addListDataListener(ListDataListener listener) {
		if(listener == null)
			return;
		this.listenerList.add(listener);
	}
	
	public void removeListDataListener(ListDataListener listener) {
		if(listener == null)
			return;
		this.listenerList.remove(listener);
	}
	
	public boolean addAll(Collection<? extends T> c) {
		if(c == null)
			return false;
		if(c.size() == 0)
			return true;
		
		int sIndex = this.dataList.size();
		this.dataList.addAll(c);
		int eIndex = this.dataList.size() -1;
		fireIntervalAdded(this, sIndex, eIndex);
		return true;
	}
	
	public boolean addAll(T[] list) {
		if(list == null)
			return false;
		if(list.length == 0)
			return true;
		int sIndex = this.dataList.size();
		for(T t : list) {
			this.dataList.add(t);
		}
		
		int eIndex = this.dataList.size() -1;
		fireIntervalAdded(this, sIndex, eIndex);
		return true;
	}
	
	public boolean addAll(int index, Collection<? extends T> c) {
		if(c == null)
			return false;
		if(c.size() == 0)
			return true;
		if(!(isLegalIndex(index)))
			return false;
		int size = c.size();
		this.dataList.addAll(index, c);
		fireIntervalAdded(this, index, index + size);
		return true;
	}
	
	public boolean containsAll(Collection<?> c) {
		if( c== null)
			return false;
		return this.dataList.containsAll(c);
	}
	
	public boolean isEmpty() {
		return this.dataList.isEmpty();
	}
	
	public Iterator<T> iterator() {
		return this.dataList.iterator();
	}
	
	public int lastIndexOf(Object o) {
		return this.dataList.lastIndexOf(o);
	}
	
	public ListIterator<T> listIterator() {
		return this.dataList.listIterator();
	}
	
	public ListIterator<T> listIterator(int index) {
		return this.dataList.listIterator(index);
	}
	
	public boolean removeAll(Collection<?> c) {
		if(c == null)
			return false;
		if(c.size() == 0)
			return true;
		
		int index = this.dataList.size() -1;
		this.dataList.removeAll(c);
		fireIntervalRemoved(this, 0, index);
		return true;
	}
	
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	public List<T> subList(int fromIndex, int toIndex) {
		return this.dataList.subList(fromIndex, toIndex);
	}
	
	protected void fireContentsChanged(Object source, int index0, int index1) {
		ListDataListener[] listeners = (ListDataListener[]) this.listenerList.toArray(LISTNER_NULL_ARRAY);
		ListDataEvent e = new ListDataEvent(source, 0, index0, index1);
		for(ListDataListener listener : listeners)
			listener.contentsChanged(e);
	}
	
	protected void fireIntervalAdded(Object source, int index0, int index1) {
		ListDataListener[] listeners = (ListDataListener[]) this.listenerList.toArray(LISTNER_NULL_ARRAY);
		ListDataEvent e = new ListDataEvent(source, 1, index0, index1);
		for (ListDataListener listener : listeners)
			listener.intervalAdded(e);
	}

	protected void fireIntervalRemoved(Object source, int index0, int index1) {
		ListDataListener[] listeners = (ListDataListener[]) this.listenerList.toArray(LISTNER_NULL_ARRAY);
		ListDataEvent e = new ListDataEvent(source, 2, index0, index1);
		for (ListDataListener listener : listeners)
			listener.intervalRemoved(e);
	}
	
}
