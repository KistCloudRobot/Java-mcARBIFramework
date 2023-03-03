package kr.ac.uos.ai.arbi.interaction.proxy;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.log.MessageLog;
import kr.ac.uos.ai.arbi.model.log.SystemLog;


public class MonitorFilter {

	private HashMap<SystemLog, Boolean> systemLogFilter;
	private HashMap<MessageLog, Boolean> messageLogFilter;

	public MonitorFilter() {
		systemLogFilter = new HashMap<>();
		messageLogFilter = new HashMap<>();
	}

	public void setMonitorFilter(JSONObject filterObject) {
		String logType = filterObject.get("LogType").toString();

		if (logType.toLowerCase().equals("systemlog")) {
			setSystemLogFilter(filterObject);
		} else if (logType.toLowerCase().equals("messagelog")) {
			setMessageLogFilter(filterObject);
		}
	}

	public boolean getMonitorFilter(JSONObject message) {
		String logType = message.get("LogType").toString();

		if (logType.toLowerCase().equals("systemlog")) {
			return getSystemLogFilter(message);
		} else if (logType.toLowerCase().equals("messagelog")) {
			return getMessageLogFilterFilter(message);
		}

		return false;
	}

	private void setSystemLogFilter(JSONObject filterObject) {

		String filterActor = filterObject.get("Actor").toString();
//		String filterType = filterObject.get("Type").toString();
		String filterAction = filterObject.get("Action").toString();
		boolean filterFlag = Boolean.parseBoolean(filterObject.get("Flag").toString());

		for (SystemLog systemLog : systemLogFilter.keySet()) {
			
			if (systemLog.getActor().toLowerCase().equals(filterActor.toLowerCase()) 
//					&& systemLog.getType().toLowerCase().equals(filterType.toLowerCase())
					&& systemLog.getAction().toLowerCase().equals(filterAction.toLowerCase())) {
				systemLogFilter.replace(systemLog, filterFlag);
				return;
			}
		}

		systemLogFilter.put(new SystemLog(filterActor, filterAction), filterFlag);
	}

	private void setMessageLogFilter(JSONObject filterObject) {
		String filterType = filterObject.get("LogType").toString();
		String filterAction = filterObject.get("Action").toString();
		boolean filterFlag = Boolean.parseBoolean(filterObject.get("Flag").toString());

		for (MessageLog messageLog : messageLogFilter.keySet()) {
			if (messageLog.getType().toLowerCase().equals(filterType.toLowerCase()) 
					&& messageLog.getAction().toLowerCase().equals(filterAction.toLowerCase())) {
				messageLogFilter.replace(messageLog, filterFlag);
				return;
			}
		}

		messageLogFilter.put(new MessageLog(filterType, filterAction), filterFlag);

	}

	private boolean getSystemLogFilter(JSONObject message) {

		String messageActor = message.get("Actor").toString().replaceAll("\"", "");
		String messageAction = message.get("Action").toString().replaceAll("\"", "");
		//System.out.println(messageActor + " " + messageAction);
		for (SystemLog systemLog : systemLogFilter.keySet()) {

			//System.out.println("[Filter Set] \t" +" < Action > " +systemLog.getAction() + "< Actor > "+systemLog.getActor());
			
			if (systemLog.getActor().toLowerCase().equals(messageActor.toLowerCase())
					
					&& systemLog.getAction().toLowerCase().equals(messageAction.toLowerCase())) {

				return systemLogFilter.get(systemLog);
			}
		}

		return false;
	}

	private boolean getMessageLogFilterFilter(JSONObject message) {
		String messageAction = message.get("Action").toString().replaceAll("\"", "");
		//System.out.println("send " + messageAction);
		
		for (MessageLog messageLog : messageLogFilter.keySet()) {
			//System.out.println("filter action? " + messageLog.getAction());
			
			if (messageLog.getAction().toLowerCase().equals(messageAction.toLowerCase())) {
				return messageLogFilter.get(messageLog);
			}
		}

		return false;
	}

	public JSONArray getFilterToJSON() {
		JSONArray filter = new JSONArray();

		for (SystemLog systemLog : systemLogFilter.keySet()) {
			JSONObject filterObject = new JSONObject();
			filterObject.put("LogType", "SystemLog");
			filterObject.put("Action", systemLog.getAction());
			filterObject.put("Actor", systemLog.getActor());
			filterObject.put("Flag", "false");
			filter.add(filterObject);
		}

		for (MessageLog messageLog : messageLogFilter.keySet()) {
			JSONObject filterObject = new JSONObject();
			filterObject.put("LogType", "MessageLog");
			filterObject.put("Action", messageLog.getAction());
			filterObject.put("Flag", "false");
			filter.add(filterObject);
		}

		return filter;
	}
}
