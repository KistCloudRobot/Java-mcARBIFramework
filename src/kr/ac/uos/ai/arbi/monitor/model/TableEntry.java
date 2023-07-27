package kr.ac.uos.ai.arbi.monitor.model;

public abstract interface TableEntry<K> {

	public abstract K getKey();
	
	public abstract Object getValue(int paramInt);
	
	public abstract void setValue(int paramInt, Object paramObject);
}
