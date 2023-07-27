package kr.ac.uos.ai.arbi.interaction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LogManager {

	private InteractionManager interactionManager;
	private GlobalFilter globalFilter;
	
	public LogManager(InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
		this.globalFilter = new GlobalFilter(this);
	}
	
	public JSONObject logParseToJSON(String data) {
		return LogParser.LogParseFromGL(data);
	}
	
	public void setGlobalFilter(JSONArray filter) {
		for(int i=0; i<filter.size(); i++)
			globalFilter.setGlobalFilter((JSONObject)filter.get(i));
	}
	
	public int getInteractionManagerProxySize() {
		return interactionManager.getProxySize();
	}
	
	public boolean checkInteractionManagerProxyFilter(int i, JSONObject filterObject) {
		return interactionManager.checkProxyFilter(i, filterObject);
	}
	
	public void globalFilterChange(JSONObject filterObject) {
		interactionManager.globalFilterChange(filterObject);
	}
}
