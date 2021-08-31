package kr.ac.uos.ai.arbi.interaction.proxy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class MonitorProxy {

	private String monitorID;
	private String protocol;
	private MonitorFilter monitorFilter;

	public MonitorProxy(String monitorID, String protocol, JSONArray filter ) {
		this.monitorID = monitorID;
		this.protocol = protocol;
		this.monitorFilter = new MonitorFilter();
		setFilter(filter);
	}

	public void setFilter(JSONArray filter) {
		for(int i=0; i<filter.size(); i++) {
			monitorFilter.setMonitorFilter((JSONObject)filter.get(i));
		}
	}
	
	public boolean isFilterON(JSONObject message) {
		return monitorFilter.getMonitorFilter(message);
	}

	public String getMonitorID() {
		return monitorID;
	}
	
	public String getMonitorProtocol() {
		return protocol;
	}
	
	public JSONArray getFilterToJSON() {
		return monitorFilter.getFilterToJSON();
	}

}
