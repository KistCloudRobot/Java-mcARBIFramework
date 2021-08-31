package kr.ac.uos.ai.arbi.monitor.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public abstract class AbstractTableModel<K, V extends TableEntry<K>> implements TableModel {
	private static final TableModelListener[] LISTNER_NULL_ARRAY = new TableModelListener[0];
	protected List<V> dataList;
	protected Map<K, Integer> indexMap;
	protected List<TableModelListener> listenerList;
	
	public AbstractTableModel() {
		this.dataList = new CopyOnWriteArrayList();
		this.indexMap = new ConcurrentHashMap();
		this.listenerList = new CopyOnWriteArrayList();
		
	}
	
	public void addRow(V entry) {
		if(entry == null)
			return;
		if(entry.getKey() == null)
			return;
		if(containsKey(entry.getKey()))
			return;
		this.dataList.add(entry);
		int index = this.dataList.size() -1;
		this.indexMap.put(entry.getKey(), Integer.valueOf(index));
		fireTableRowsInserted(index, index);
	}
	
	public void addAllRow(Collection<V> c) {
		if (c == null)
			return;
		int index = this.dataList.size();
		for (V entry : c) {
			if (containsKey(entry.getKey()))
				continue;
			this.dataList.add(entry);
			this.indexMap.put(entry.getKey(), Integer.valueOf(this.dataList.size() - 1));
		}
		fireTableRowsInserted(index, index + c.size());
	}

	public void addAllRow(V[] c) {
		if (c == null)
			return;
		int index = this.dataList.size();
		for (V entry : c) {
			if (containsKey(entry.getKey()))
				continue;
			this.dataList.add(entry);
			this.indexMap.put(entry.getKey(), Integer.valueOf(this.dataList.size() - 1));
		}
		fireTableRowsInserted(index, index + c.length);
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (value == null)
			return;
		if (!(isLegalRowIndex(rowIndex)))
			return;
		if (!(isLegalColunmIndex(columnIndex)))
			return;
		TableEntry entry = (TableEntry) this.dataList.get(rowIndex);
		entry.setValue(columnIndex, value);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public V getRow(int rowIndex) {
		if (!(isLegalRowIndex(rowIndex)))
			return null;
		return this.dataList.get(rowIndex);
	}

	public V getRow(K key) {
		Integer index = (Integer) this.indexMap.get(key);
		if (index == null)
			return null;
		return this.dataList.get(index.intValue());
	}

	public V removeRow(int rowIndex) {
		if (!(isLegalRowIndex(rowIndex)))
			return null;
		V entry = this.dataList.remove(rowIndex);
		this.indexMap.remove(entry.getKey());
		fireTableRowsDeleted(rowIndex, rowIndex);
		return entry;
	}

	public V remove(V element) {
		int rowIndex = this.dataList.indexOf(element);
		if (rowIndex < 0) {
			return null;
		}
		this.dataList.remove(element);
		this.indexMap.remove(element.getKey());
		fireTableRowsDeleted(rowIndex, rowIndex);
		return element;
	}

	public void clear() {
		this.dataList.clear();
		this.indexMap.clear();
		int last = this.dataList.size() - 1;
		fireTableRowsDeleted(0, last);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (!(isLegalRowIndex(rowIndex)))
			return null;
		if (!(isLegalColunmIndex(columnIndex)))
			return null;
		TableEntry entry = (TableEntry) this.dataList.get(rowIndex);
		return entry.getValue(columnIndex);
	}

	public Object[] toArray() {
		return this.dataList.toArray();
	}

	public V[] toArray(V[] a) {
		return ((V[]) this.dataList.toArray(a));
	}

	public int indexOf(V element) {
		return this.dataList.indexOf(element);
	}

	protected boolean isLegalRowIndex(int index) {
		return ((index >= 0) && (index < this.dataList.size()));
	}

	protected boolean isLegalColunmIndex(int columnIndex) {
		return ((columnIndex >= 0) && (columnIndex < getColumnCount()));
	}

	public boolean containsKey(Object o) {
		if (o == null)
			return false;
		return this.indexMap.containsKey(o);
	}

	public int indexOfKey(K key) {
		Integer index = (Integer) this.indexMap.get(key);
		return ((index == null) ? -1 : index.intValue());
	}

	public int size() {
		return this.dataList.size();
	}

	public int getRowCount() {
		return size();
	}

	public void addTableModelListener(TableModelListener listener) {
		if (listener == null)
			return;
		this.listenerList.add(listener);
	}

	public void removeTableModelListener(TableModelListener listener) {
		if (listener == null)
			return;
		this.listenerList.remove(listener);
	}

	protected void fireTableDataChanged() {
		fireTableChanged(new TableModelEvent(this));
	}

	protected void fireTableStructureChanged() {
		fireTableChanged(new TableModelEvent(this, -1));
	}

	protected void fireTableRowsInserted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, 1));
	}

	protected void fireTableRowsUpdated(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, 0));
	}

	protected void fireTableRowsDeleted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, -1));
	}

	protected void fireTableCellUpdated(int row, int column) {
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	protected void fireTableChanged(TableModelEvent e) {
		TableModelListener[] listeners = (TableModelListener[]) this.listenerList.toArray(LISTNER_NULL_ARRAY);
		for (TableModelListener listener : listeners)
			listener.tableChanged(e);
	}

}
