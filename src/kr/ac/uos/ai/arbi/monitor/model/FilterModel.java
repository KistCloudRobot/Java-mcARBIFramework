package kr.ac.uos.ai.arbi.monitor.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import kr.ac.uos.ai.arbi.monitor.Utility;

public class FilterModel {
	public Map<String, Boolean> filterMap;
	
	public FilterModel() {
		this.filterMap = new ConcurrentHashMap();
	}
	
	public void initialize(String[] ts) {
		if(ts == null)
			return;
		for(String t : ts)
			this.filterMap.put(t, new Boolean(true));
	}
	
	public void enableFilter(String t) {
		if(t == null)
			return;
		this.filterMap.put(t, Boolean.TRUE);
	}
	
	public void disableFilter(String t) {
		if(t == null)
			return;
		this.filterMap.put(t, Boolean.FALSE);
	}
	
	public boolean isEnabled(String t) {
		if(t == null)
			return false;
		return ((Boolean) this.filterMap.get(t)).booleanValue();
	}
	
	public String[] getTypes() {
		List result = new CopyOnWriteArrayList();
		result.addAll((Collection)this.filterMap.keySet());
		return ((String[]) result.toArray(Utility.STR_NULL_ARRAY));
	}
}
